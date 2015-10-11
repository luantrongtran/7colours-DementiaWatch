package ifn372.sevencolors.watch_app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.FenceSharedPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.watch_app.backgroundservices.AlertPatientLostReceiver;
import ifn372.sevencolors.watch_app.backgroundservices.UpdateCurrentLocationService;

/**
 * Created by lua on 11/10/2015.
 */
public class PatientLostChecker {
    public static int alarm_interval = 2*1000;//second

    public static void checkPatientOutOfFences(Context context){
        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);
        FenceSharedPreferences fenceSharedPreferences = new FenceSharedPreferences(context);
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

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
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

                startAlarm(context);//start alarm
            } else {
                //If the patient has been back to one of the fences.

                cancelAlarm(context);//Cancel alarm
                //update current location to the server as normal
                userInfoPreferences.setUpdateLocationToServer(true);
            }
        } else {
            if(isSafe == false) {
                long firstMoment = userInfoPreferences.getFirstMomentOutside();
                long interval = System.currentTimeMillis() - firstMoment;
                if(interval > Constants.timeout_before_sending_alert_to_carer) {
                    userInfoPreferences.setUpdateLocationToServer(true);

                    //Update current location to server immediately after the time remaining had run out
                    Intent intent = new Intent(context, UpdateCurrentLocationService.class);
                    context.startService(intent);
                }
            }
        }

        userInfoPreferences.setSafe(isSafe);
    }

    /**
     * Start alarm if the patient is not safe
     */
    private static void startAlarm(Context context) {
        PendingIntent pendingIntent = createPendingIntentForAlarm(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                alarm_interval, pendingIntent);
    }

    /**
     * Cancel alarm if the patient goes back to one of the fences.
     */
    private static void cancelAlarm(Context context) {
        PendingIntent pendingIntent = createPendingIntentForAlarm(context);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    private static PendingIntent createPendingIntentForAlarm(Context context) {
        Intent intent = new Intent(context, AlertPatientLostReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
