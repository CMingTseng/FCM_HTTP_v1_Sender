package fcm.sender.v1.demo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import fcm.token.credential.google.GetGoogleCredentialTokenTask;
import fcm.v1.sender.FCMV1Sender;

public class MainActivity extends AppCompatActivity {
    //FIXME ref:http://www.zoftino.com/android-notification-data-messages-from-app-server-using-firebase-cloud-messaging
    private static final String TAG = "MainActivity";
    private static final int FILE_SELECT_CODE = 0;
    private File mPrivateKeyFile;
    private FCMV1Sender mSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String receivertoken = getIntent().getStringExtra("FCM_Recive_Client");
        final Button select_pkf = findViewById(R.id.select_pkf);
        final TextView pkf_select_ok = findViewById(R.id.pkf_select_ok);
        select_pkf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showFileChooser();
                //FIXME ref  https://firebase.google.com/docs/cloud-messaging/auth-server#authorize_http_v1_send_requests
                //FIXME ref: https://support.appinstitute.com/hc/en-us/articles/115002827469-How-to-Get-Your-Google-Play-JSON-Key
//                String fileName = "firebase_project_admin_key.json";
                String fileName = "fcm-gcm-demo-firebase-adminsdk-o2zkf-16f15df8f6.json";
                String path = Environment.getExternalStorageDirectory() + "/" + fileName;
                mPrivateKeyFile = new File(path);
                if (mPrivateKeyFile != null) {
                    pkf_select_ok.setVisibility(View.VISIBLE);
                }
            }
        });
        final Button get_token = findViewById(R.id.get_token);
        get_token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pkf_select_ok.getVisibility() == View.VISIBLE) {
                    new GetGoogleCredentialTokenTask(new GetGoogleCredentialTokenTask.OnGetTokenCompleted() {
                        @Override
                        public void OnCompleted(String tokens) {
                            pkf_select_ok.setText(tokens);
                            mSender = null;
                            mSender = new FCMV1Sender(tokens);
                        }
                    }).execute(mPrivateKeyFile);
                }
            }
        });
        final EditText firebase_project_id = findViewById(R.id.firebase_project_id);
        final EditText input_key_1 = findViewById(R.id.input_key_1);
        final EditText input_value_1 = findViewById(R.id.input_value_1);
        final EditText input_key_2 = findViewById(R.id.input_key_2);
        final EditText input_value_2 = findViewById(R.id.input_value_2);
        final EditText input_key_3 = findViewById(R.id.input_key_3);
        final EditText input_value_3 = findViewById(R.id.input_value_3);
        final EditText input_key_4 = findViewById(R.id.input_key_4);
        final EditText input_value_4 = findViewById(R.id.input_value_4);
        final Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Gson gson = new Gson();
                String firebase_project_ids = firebase_project_id.getText().toString();
                if (TextUtils.isEmpty(firebase_project_ids)) {
                    firebase_project_ids = "fcm-gcm-demo";
                }
                String input_key_1s = input_key_1.getText().toString();
                if (TextUtils.isEmpty(input_key_1s)) {
                    input_key_1s = "fountain";
                }
                final String input_value_1s = input_value_1.getText().toString();
                String input_key_2s = input_key_2.getText().toString();
                if (TextUtils.isEmpty(input_key_2s)) {
                    input_key_2s = "message";
                }
                final String input_value_2s = input_value_2.getText().toString();
                String input_key_3s = input_key_3.getText().toString();
                if (TextUtils.isEmpty(input_key_3s)) {
                    input_key_3s = "msgType";
                }
                final String input_value_3s = input_value_3.getText().toString();
                String input_key_4s = input_key_4.getText().toString();
                if (TextUtils.isEmpty(input_key_4s)) {
                    input_key_4s = "device_id";
                }
                final String input_value_4s = input_value_4.getText().toString();
                final Map<String, String> detailcontentmap = new HashMap<String, String>();
                detailcontentmap.put(input_key_1s, input_value_1s);
                detailcontentmap.put(input_key_2s, input_value_2s);
                detailcontentmap.put(input_key_3s, input_value_3s);
                detailcontentmap.put(input_key_4s, input_value_4s);
                final Type type = new TypeToken<Map>() {
                }.getType();

                final JsonElement datadetail = gson.toJsonTree(detailcontentmap, type);

                JsonObject msgObj = new JsonObject();
                JsonObject jsonObj = new JsonObject();
                //FIXME  if use topic or condition can not ues token for one user
//                jsonObj.addProperty("topic", "deals");
                jsonObj.add("data", datadetail);
                //FIXME  if use notification can not go onMessageReceived
//                JsonObject notifiDetails = new JsonObject();
//                notifiDetails.addProperty("body", "bodyiiii");
//                notifiDetails.addProperty("title", "titleooooo");
//                jsonObj.add("notification", notifiDetails);
                if (!TextUtils.isEmpty(receivertoken)) {
                    jsonObj.addProperty("token", receivertoken);
                }
                msgObj.add("message", jsonObj);

                mSender.sendMessageToFcm(firebase_project_ids, msgObj);
//
//              mSender.sendMessageToFcm(firebase_project_ids, new RequestData(new RequestDetailData("deals",datadetail,new RequestNotificationData("titleooooo","bodyiiii"))));
            }
        });
    }

    private void showFileChooser() {
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to get Token"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = getPath(this, uri);
                        Log.d(TAG, "File Path: " + path);
                        // Get the file instance
                        // File file = new File(path);
                        // Initiate the upload
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
