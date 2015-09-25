package ifn372.sevencolors.watch_app.backgroundservices;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.FenceSharedPreferences;
import ifn372.sevencolors.watch_app.GPSTracker;
import ifn372.sevencolors.watch_app.R;
import ifn372.sevencolors.watch_app.SharedPreferencesUtitlies;
import ifn372.sevencolors.watch_app.activities.MapsActivity;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService  {
    public static String ACTION = LocationTrackerService.class.getCanonicalName();
    public static int PATIENT_LOST_NOTIFICATION_ID = 1;

    public LocationTrackerService(){
        super("UpdateLocationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(Constants.application_id, "update location service");

        Intent updateLocation = new Intent(this, UpdateCurrentLocationService.class);
        startService(updateLocation);

        checkIsOutOfFences();

        Intent broadcastIntent = new Intent(ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    public void checkIsOutOfFences() {

        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(this);
        FenceSharedPreferences fenceSharedPreferences = new FenceSharedPreferences(this);
        FenceList fenceList = fenceSharedPreferences.getFences();
        if(fenceList == null || fenceList.getItems()==null) {
            Log.e(Constants.application_id, "Cannot connect to the server");
            return;
        }

        boolean isSafe = false;
        for(Fence fence : fenceList.getItems()) {
            float[] distances = new float[2];
            Location.distanceBetween(currentLocationPreferences.getLat(),
                    currentLocationPreferences.getLon(), fence.getLat(),
                    fence.getLon(), distances);

            if(distances[0] < fence.getRadius()) {
                //If the patient is inside one of the fences then he/she is safe
                isSafe = true;
                break;
            }
        }

        Log.e(Constants.application_id, "Is user Safe: " + isSafe);
        if(!isSafe) {
            sendPatientLostNotification(getString(R.string.notification_title),
                    getString(R.string.notification_text));
        }
    }

    public void sendPatientLostNotification(String title, String content) {
//        Drawable warningIcon = this.getResources().getDrawable(R.drawable.ic_warning_black_24dp);
//        Bitmap bm = BitMapUtils
//                .getMutableBitmapFromResourceFromResource(warningIcon,
//                        0x00FF0000);
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
