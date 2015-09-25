package ifn372.sevencolors.watch_app.backgroundservices;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.FenceSharedPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences. Also, this service will check if the patient is safe ( the patient is inside
 * one of the fences), and store the value indicating if the patient
 * is safe or not is stored into SharedPreferences so that the AlertPatientLostService will
 * start the alarm according to the value being set.
 */
public class LocationTrackerService extends IntentService  {
    public static String ACTION = LocationTrackerService.class.getCanonicalName();
    public static int alarm_interval = 2*1000;//second


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

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
        boolean previousSafetyStatus = userInfoPreferences.isSafe();

        Log.e(Constants.application_id, isSafe + ", " + previousSafetyStatus);
        if(isSafe != previousSafetyStatus ) {
            //if the statue of safety changed
            if(isSafe == false) {
                //If the patient is no longer inside one of the fences

                //The first moment when the patient stepped outside the fences.
                userInfoPreferences.setFirstMomentOutside(System.currentTimeMillis());

                //Stop updating current location to give time for the patient to come back
                //to one of the fences
                userInfoPreferences.setUpdateLocationToServer(false);

                startAlarm();//start alarm
            } else {
                //If the patient has been back to one of the fences.

                cancelAlarm();//Cancel alarm
                //update current location to the server as normal
                userInfoPreferences.setUpdateLocationToServer(true);
            }
        } else {
            if(isSafe == false) {
                long firstMoment = userInfoPreferences.getFirstMomentOutside();
                long interval = System.currentTimeMillis() - firstMoment;
                if(interval > Constants.timeout_before_sending_alert_to_carer) {
                    userInfoPreferences.setUpdateLocationToServer(true);
                }
            }
        }

        userInfoPreferences.setSafe(isSafe);
    }

    /**
     * Start alarm if the patient is not safe
     */
    public void startAlarm() {
        PendingIntent pendingIntent = createPendingIntentForAlarm();

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                alarm_interval, pendingIntent);
    }

    /**
     * Cancel alarm if the patient goes back to one of the fences.
     */
    public void cancelAlarm() {
        PendingIntent pendingIntent = createPendingIntentForAlarm();

        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public PendingIntent createPendingIntentForAlarm() {
        Intent intent = new Intent(this, AlertPatientLostReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}