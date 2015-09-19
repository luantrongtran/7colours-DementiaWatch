package ifn372.sevencolors.dementiawatch.backgroundservices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.TemporaryFenceSharedPreferences;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.dementiawatch.GPSTracker;
import ifn372.sevencolors.dementiawatch.webservices.UpdateFenceService;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService  {
    public static String ACTION = LocationTrackerService.class.getCanonicalName();


    public LocationTrackerService(){
        super("UpdateLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(Constants.application_id, "update location service");

        UserInfoPreferences u = new UserInfoPreferences(this);
        Log.e(Constants.application_id, "number of pickedup patients: " + u.getFullName());
        updateTemporaryFence();
        Intent broadcastIntent = new Intent(ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    public void updateTemporaryFence() {
        TemporaryFenceSharedPreferences temporaryFenceSharedPreferences
                = new TemporaryFenceSharedPreferences(this);
        Map<String,?> temporaryFences = temporaryFenceSharedPreferences.getAllTemporaryFences();
        if(temporaryFences == null || temporaryFences.size() == 0) {
            return;
        }

        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(this);
        final LatLng curLocation = new LatLng(currentLocationPreferences.getLat(),
                currentLocationPreferences.getLon());

        //Update temporary fence to server
        UpdateFenceService updateFenceService = new UpdateFenceService(null);
        for (Map.Entry<String, ?> entry : temporaryFences.entrySet())
        {
            int fenceId = Integer.parseInt(entry.getValue().toString());
            updateFenceService.execute(Integer.toString(fenceId),
                    Double.toString(curLocation.latitude),
                    Double.toString(curLocation.longitude));
        }
    }
}
