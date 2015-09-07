package ifn372.sevencolors.dementiawatch.webservices;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.PatientList;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;
import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.parcelable.PatientListParcelable;

/**
 * Created by lua on 27/08/2015.
 */
public class UpdatePatientsListService extends IntentService {

    public static String ACTION = UpdatePatientsListService.class.getCanonicalName();

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
        }
    }
}