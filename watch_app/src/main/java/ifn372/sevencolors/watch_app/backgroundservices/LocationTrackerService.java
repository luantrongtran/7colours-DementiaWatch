package ifn372.sevencolors.watch_app.backgroundservices;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.watch_app.GPSTracker;
import ifn372.sevencolors.watch_app.SharedPreferencesUtitlies;

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
