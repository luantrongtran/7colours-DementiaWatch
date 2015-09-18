package ifn372.sevencolors.dementiawatch.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.dementiawatch.GPSTracker;

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

        Intent broadcastIntent = new Intent(ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }
}
