package ifn372.sevencolors.dementiawatch.webservices;

/**
 * Created by zach on 13/09/15.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import android.os.Handler;

import java.util.concurrent.RunnableFuture;

import ifn372.sevencolors.dementiawatch.activities.MapsActivity;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private static Handler mHandler = new Handler();
    private static Runnable runnable;

    /*
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();
    }*/

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        final String title = data.getString("title");
        final String message = data.getString("message");
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        mHandler.post(runnable = new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                try {
                    Toast.makeText(MyGcmListenerService.this, "Caution! Your patient need your help!", Toast.LENGTH_SHORT).show();
                    PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification notification = new android.support.v7.app.NotificationCompat.Builder(context)
                            .setTicker(message)
                            .setSmallIcon(android.R.drawable.ic_menu_report_image)
                            .setContentTitle("Your patient need your help!")
                            .setContentText(message)
                            .setContentIntent(pi)
                            .setAutoCancel(true)
                            .setVisibility(1)
                            .setSound(defaultSoundUri)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(999, notification);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /* for recursive panic status
        mHandler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                Context context = getApplicationContext();
                try {
                    if (!title.equals("InPanic")) {
                        Toast.makeText(MyGcmListenerService.this, "Your patient is fine now.", Toast.LENGTH_SHORT).show();
                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.cancel(999);
                        mHandler.removeCallbacks(runnable);
                    } else {
                        Toast.makeText(MyGcmListenerService.this, "Caution! Your patient need your help!", Toast.LENGTH_SHORT).show();
                        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);
                        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Notification notification = new android.support.v7.app.NotificationCompat.Builder(context)
                                .setTicker("Your patient need your help!")
                                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                                .setContentTitle("Your patient need your help!")
                                .setContentText(message)
                                .setContentIntent(pi)
                                .setAutoCancel(false)
                                .setVisibility(1)
                                .setSound(defaultSoundUri)
                                .build();

                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        notificationManager.notify(999, notification);
                        mHandler.postDelayed(runnable, 2000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);
        */
        //sendNotification((title == "InPanic"), message);
        // [END_EXCLUDE]
    }
    // [END receive_message]
}
