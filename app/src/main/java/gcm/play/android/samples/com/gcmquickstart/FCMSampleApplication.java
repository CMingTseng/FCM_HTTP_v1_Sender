package gcm.play.android.samples.com.gcmquickstart;


import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.Set;

import fcm.android.play.google.quickstart.FirebaseBaseApplication;
import fcm.android.play.google.quickstart.FirebaseInstanceIDBaseService;
import gcm.play.android.samples.com.gcmquickstart.notification.MyDefinitionReceiveMessage;

/**
 * Created by Neo on 2018/4/16.
 */

public class FCMSampleApplication extends FirebaseBaseApplication {
    private static final String TAG = "FCMSampleApplication";
    private static FCMSampleApplication mContext;
    private final static Gson sGson = new Gson();

    public static FCMSampleApplication getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        startService(new Intent(this, FirebaseInstanceIDBaseService.class));
    }

    @Override
    protected void handleNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            final String message = remoteMessage.getNotification().getBody().replace("=", "");
            if (!TextUtils.isEmpty(message)) {
                Log.e(TAG, "RemoteMessage push getNotification String : " + message);
                final Intent pushNotification = new Intent(C.PUSH_NOTIFICATION);
                try {
                    pushNotification.putExtra(C.PUSH_NOTIFICATION_PAYLOAD, message);
                    final JSONObject json = new JSONObject(message);
                    final MyDefinitionReceiveMessage pushmessage = sGson.fromJson(message, MyDefinitionReceiveMessage.class);
                    if (pushmessage != null) {
                        pushNotification.putExtra(C.PUSH_NOTIFICATION_PAYLOAD, pushmessage);
                        Log.d(TAG, "Show sss : " + sGson.toJson(pushmessage).toString());
                    }
                    broadcastNotificationMessage(pushNotification);
                    final Intent showNotifiIntent = new Intent(this, MainActivity.class);
                    showNotifiIntent.putExtra(C.PUSH_NOTIFICATION_PAYLOAD, pushmessage);
                    showNotificationMessage(showNotifiIntent);
                } catch (Exception e) {
                    Log.e(TAG, "Exception : " + e.getMessage());
                    pushNotification.putExtra(C.PUSH_NOTIFICATION_BODY, message);
                    broadcastNotificationMessage(pushNotification);
                }
            }
        }
    }

    @Override
    protected void handleDataMessage(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            final Intent pushNotification = new Intent(C.PUSH_NOTIFICATION);
            MyDefinitionReceiveMessage pushmessage = null;
            final String jsonstring = remoteMessage.getData().toString().replace("=", "");
            Log.e(TAG, "RemoteMessage push Data Message : " + jsonstring);
            if (!TextUtils.isEmpty(jsonstring)) {
                pushNotification.putExtra(C.PUSH_NOTIFICATION_BODY, jsonstring);
                try {
                    final JSONObject json = new JSONObject(jsonstring);
                    pushmessage = sGson.fromJson(jsonstring, MyDefinitionReceiveMessage.class);
                    Log.d(TAG, "Show sss : " + sGson.toJson(pushmessage).toString());
                    if (pushmessage != null) {
                        pushNotification.putExtra(C.PUSH_NOTIFICATION_PAYLOAD, pushmessage);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception : " + e.getMessage());
                    final JsonObject json = new JsonObject();
                    final Set<String> keys = remoteMessage.getData().keySet();
                    for (String s : keys) {
                        json.addProperty(s, String.valueOf(remoteMessage.getData().get(s)));
                    }
                    try {
                        pushmessage = sGson.fromJson(json, MyDefinitionReceiveMessage.class);
                        if (pushmessage != null) {
                            pushNotification.putExtra(C.PUSH_NOTIFICATION_PAYLOAD, pushmessage);
                        }
                    } catch (Exception e2) {
                        Log.e(TAG, "Exception  : " + e2.getMessage());
                    }
                }
                broadcastNotificationMessage(pushNotification);
                final Intent showNotifiIntent = new Intent(this, MainActivity.class);
                showNotifiIntent.putExtra(C.PUSH_NOTIFICATION_PAYLOAD, pushmessage);
                showNotificationMessage(showNotifiIntent);
            }
        }
    }

    @Override
    protected void handleFCMTokenRefresh(String token) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "TODO something FCM Token Refresh : " + token);
        }
        //TODO this is Demo for notify Activity
        final Intent notifytokenrefresh = new Intent(FirebaseInstanceIDBaseService.FIREBASE_TOKEN_REFRESH);
        notifytokenrefresh.putExtra(FirebaseInstanceIDBaseService.FIREBASE_TOKEN, token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(notifytokenrefresh);
    }

    private void broadcastNotificationMessage(Intent notify) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(notify);
        try {
//            Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/raw/notification");
            final Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final Ringtone r = RingtoneManager.getRingtone(this, alarmSound);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNotificationMessage(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final MyDefinitionReceiveMessage pushmessage = intent.getParcelableExtra(C.PUSH_NOTIFICATION_PAYLOAD);
        final Bundle extra = new Bundle();
        extra.putParcelable(C.PUSH_NOTIFICATION_PAYLOAD, pushmessage);
        final Uri extrauri = Uri.parse(sGson.toJson(pushmessage));
        intent.setData(extrauri);
        intent.putExtras(extra);
        String message = "YOOOOO TEST BAR";
//        switch (pushmessage.getMsgType()) {
//            case 0:
//                break;
//            case 1:
//            default:
//                break;
//        }
        final NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();
        bigStyle.bigText(message);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, C.NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setWhen(System.currentTimeMillis()).setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent).setStyle(bigStyle)
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setLights(Color.GREEN, 1000, 1000)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel channel = new NotificationChannel(getString(R.string.app_name), getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(getString(R.string.app_name));
            channel.enableLights(true);
            mBuilder.setChannelId(getString(R.string.app_name));
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(C.NOTIFICATION_ID, mBuilder.build());
    }
}
