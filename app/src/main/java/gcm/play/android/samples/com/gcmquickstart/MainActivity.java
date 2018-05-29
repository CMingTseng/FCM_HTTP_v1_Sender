/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gcm.play.android.samples.com.gcmquickstart;

import com.google.gson.Gson;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import fcm.android.play.google.quickstart.FirebaseInstanceIDBaseService;
import gcm.play.android.samples.com.gcmquickstart.notification.MyDefinitionReceiveMessage;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ProgressBar mRegistrationProgressBar;
    private TextView mInformationTextView;
    private boolean isReceiverRegistered;
    private BroadcastReceiver mPushNotificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(C.PUSH_NOTIFICATION)) {
                handleNotification(intent);
                mInformationTextView.setText("Get PUSHNOTIFICATION");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                if (action.equals(FirebaseInstanceIDBaseService.FIREBASE_TOKEN_REFRESH)) {
                    final Fragment fragment = new PreloadFragment();
                    final Bundle token = new Bundle();
                    token.putString(FirebaseInstanceIDBaseService.FIREBASE_TOKEN, intent.getStringExtra(FirebaseInstanceIDBaseService.FIREBASE_TOKEN));
                    fragment.setArguments(token);
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                } else {
                    final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    final boolean sentToken = sharedPreferences.getBoolean(RegistrationIntentService.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        mInformationTextView.setText(getString(R.string.gcm_send_message));
                    } else {
                        mInformationTextView.setText(getString(R.string.token_error_message));
                    }
                }
            }
        };
        mInformationTextView = (TextView) findViewById(R.id.informationTextView);
        if (checkPlayServices()) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(this, RegistrationIntentService.class);
//            startService(intent);
        }
    }

    private void handleNotification(Intent intent) {
        Log.e(TAG, "Get Notification  TODO something ");
    }

    private void processPushMessage(Intent intent) {
        final Uri extrauri = intent.getData();
        if (extrauri != null) {
            Log.d(TAG, "processPushMessage Show Uri : " + extrauri.toString());
            final Gson gson = new Gson();
            final MyDefinitionReceiveMessage pushmessage = gson.fromJson(extrauri.toString(), MyDefinitionReceiveMessage.class);
            final FragmentManager fm = getSupportFragmentManager();
            final Fragment fragment = fm.findFragmentById(R.id.content);
            if (fragment != null) {
                Bundle arguments = fragment.getArguments();
                if (arguments == null) {
                    arguments = new Bundle();
                }
                arguments.putParcelable(C.PUSH_NOTIFICATION_PAYLOAD, pushmessage);
                fragment.setArguments(arguments);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "Show NewIntent : " + intent);
        if (intent != null) {
            processPushMessage(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        final Intent intent = getIntent();
        Log.d(TAG, "Show intent : " + intent);
        if (intent != null) {
            processPushMessage(intent);
        }
        // Registering BroadcastReceiver
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushNotificationReceiver);
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            final IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(RegistrationIntentService.REGISTRATION_COMPLETE);
            intentFilter.addAction(FirebaseInstanceIDBaseService.FIREBASE_TOKEN_REFRESH);
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, intentFilter);
            LocalBroadcastManager.getInstance(this).registerReceiver(mPushNotificationReceiver, new IntentFilter(C.PUSH_NOTIFICATION));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
//                        .show();
//            } else {
//                Log.i(TAG, "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
        return true;
    }
}
