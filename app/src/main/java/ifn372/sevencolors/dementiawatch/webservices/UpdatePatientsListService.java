package ifn372.sevencolors.dementiawatch.webservices;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;
import java.util.Vector;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.PatientList;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;
import ifn372.sevencolors.dementiawatch.BitMapUtils;
import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.PatientManager;
import ifn372.sevencolors.dementiawatch.R;
import ifn372.sevencolors.dementiawatch.activities.MapsActivity;
import ifn372.sevencolors.dementiawatch.parcelable.PatientListParcelable;

/**
 * Created by lua on 27/08/2015.
 */
public class UpdatePatientsListService extends IntentService {

    public static String ACTION = UpdatePatientsListService.class.getCanonicalName();
    public static int PATIENT_LOST_NOTIFICATION_ID = 1;

    public UpdatePatientsListService() {
        super("UpdatePatientsListService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(Constants.application_id, "update patients list service");
        MyApi patientApi = BackendApiProvider.getPatientApi();

        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences(Constants.sharedPreferences_user_info, MODE_PRIVATE);
        int userId = sharedPref.getInt(Constants.sharedPreferences_user_info_id,
                Constants.sharedPreferences_integer_default_value);
        int role = sharedPref.getInt(Constants.sharedPreferences_user_info_role,
                Constants.sharedPreferences_integer_default_value);

        try {
            PatientList patientList = patientApi.getPatientListByCarerOrRelative(userId, role).execute();
            if(patientList.getItems() == null){
                patientList.setItems(new Vector<Patient>());
            }

            Log.i(Constants.application_id, patientList.toString());
            checkPatientsLost(patientList);
            Log.i(Constants.application_id, patientList.toString());

            Intent broadCastIntent = new Intent(ACTION);

            PatientListParcelable patientListParcelable = new PatientListParcelable(patientList);
            broadCastIntent.putExtra("patientList", patientListParcelable);
            LocalBroadcastManager.getInstance(this).sendBroadcast(broadCastIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkPatientsLost(PatientList patientList) {
        if(patientList == null || patientList.getItems() == null) {
            return;
        }
        int numOfLostPatients = 0;
        for(Patient patient : patientList.getItems()) {
            FenceList fenceList = patient.getFenceList();
            if(fenceList == null || fenceList.getItems() == null){
                patient.setSafety(true);//doesn't have any fence
                continue;
            }
            boolean isSafe = false;
            for(Fence fence : fenceList.getItems()) {
                float[] distance = new float[2];
                Location
                        .distanceBetween(patient.getCurrentLocation().getLat(),
                                patient.getCurrentLocation().getLon(),
                                fence.getLat(), fence.getLon(), distance);
//                        (fence.getLat() - patient.getCurrentLocation().getLat())
//                        * (fence.getLat() - patient.getCurrentLocation().getLat())
//                        + (fence.getLon() - patient.getCurrentLocation().getLon())
//                        * (fence.getLon() - patient.getCurrentLocation().getLon());
                isSafe = distance[0] < fence.getRadius();
                if(isSafe==true){
                    break;
                }
                Log.i(Constants.application_id, "Distance: " + distance[0]);
            }
            patient.setSafety(isSafe);
            if(!isSafe) {
                numOfLostPatients++;
            }
        }

        if(numOfLostPatients > 0) {
            sendPatientLostNotification("!Alert", numOfLostPatients + " Patient(s) lost");
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