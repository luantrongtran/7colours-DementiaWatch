package ifn372.sevencolors.watch_app.webservices;

/**
 * Created by zach on 13/09/15.
 */

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.InvitationSharedPreferences;

public class MyGcmListenerService extends GcmListenerService {
    public static String ACTION = "NotificationInvitation";

    private static final String TAG = "MyGcmListenerService";
    private static Handler mHandler = new Handler();
    private static Runnable runnable;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        final String title = data.getString("title");
        final String message = data.getString("message");
        final String strCarerId = data.getString("carer_id");
        int carerId = 0;
        if(strCarerId != null || !strCarerId.isEmpty()) {
            carerId = Integer.valueOf(strCarerId);
        }
        Log.i(TAG, "title: " + title);
        Log.i(TAG, "From: " + from);
        Log.i(TAG, "Message: " + message);

        InvitationSharedPreferences invitationSharedPreferences
                = new InvitationSharedPreferences(this);
        invitationSharedPreferences.setCarerId(carerId);
        invitationSharedPreferences.setInvitationMessage(message);

        Intent intent = new Intent(ACTION);
        intent.putExtra(Constants.gcm_title, title);
        intent.putExtra(Constants.gcm_message, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
