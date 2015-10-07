package ifn372.sevencolors.watch_app.backgroundservices;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.watch_app.R;
import ifn372.sevencolors.watch_app.activities.MapsActivity;

public class AlertPatientLostService extends IntentService {
    public static String TAG = "AlertPatientLostService";
    public static int PATIENT_LOST_NOTIFICATION_ID = 1;

    public AlertPatientLostService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
        long firstMoment = userInfoPreferences.getFirstMomentOutside();
        long interval =  System.currentTimeMillis() - firstMoment;
        long timeRemaining = Constants.timeout_before_sending_alert_to_carer - interval;

        String timeRemainingText = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(timeRemaining),
                TimeUnit.MILLISECONDS.toSeconds(timeRemaining) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeRemaining))
        );

        String txt = "";
        if(timeRemaining < 0) {
            txt += "Carer alerted";
        } else {
            Timestamp ts = new Timestamp(interval);
            txt += "Remaining Time: " + timeRemainingText ;
        }

        sendPatientLostNotification(getString(R.string.notification_title),
                txt);
    }

    public void sendPatientLostNotification(String title, String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setPriority(2)
                        .setSmallIcon(R.drawable.ic_warning_black_24dp)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setDefaults(Notification.DEFAULT_SOUND);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MapsActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MapsActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(PATIENT_LOST_NOTIFICATION_ID, mBuilder.build());
    }
}
