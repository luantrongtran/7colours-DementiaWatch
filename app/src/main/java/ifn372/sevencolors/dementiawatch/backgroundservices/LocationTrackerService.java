package ifn372.sevencolors.dementiawatch.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.dementiawatch.GPSTracker;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService  {
    public LocationTrackerService(){
        super("UpdateLocationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(Constants.application_id, "update location service");
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());

        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(getApplicationContext());

        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();
        Log.e(Constants.application_id, lat + ", " + lon);

        currentLocationPreferences.setLat(lat);
        currentLocationPreferences.setLon(lon);
    }
}
