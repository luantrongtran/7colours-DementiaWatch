package ifn372.sevencolors.watch_app.webservices;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.watch_app.BackendApiProvider;
import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.FenceSharedPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.watch_app.PatientLostChecker;

/**
 * Get fences of the current user which is the current patient
 */
public class GetFencesService extends IntentService {
    public static String ACTION = GetFencesService.class.getCanonicalName();

    public GetFencesService() {
        super("GetFencesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
        int patientId = userInfoPreferences.getUserId();
        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        FenceList fenceList = null;
        try {
            fenceList = myApi.getFenceByPatientId(patientId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(fenceList == null || fenceList.getItems() == null) {
            Log.e(Constants.application_id, "Couldn't get fence list");
            return;
        }
        Log.e(Constants.application_id, "" + fenceList.size());

        FenceSharedPreferences fenceSharedPreferences = new FenceSharedPreferences(this);
        fenceSharedPreferences.setFences(fenceList);

        PatientLostChecker.checkPatientOutOfFences(this);

        //Sends broadcast for interested Activity objects.
        Intent broadcastIntent = new Intent(ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
