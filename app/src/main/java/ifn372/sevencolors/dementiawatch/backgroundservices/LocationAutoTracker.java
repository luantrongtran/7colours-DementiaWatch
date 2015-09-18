package ifn372.sevencolors.dementiawatch.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.dementiawatch.GPSTracker;

/**
 * This class track the current location based on what scheduled in the MapsActivity
 */
public class LocationAutoTracker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(Constants.application_id, "Auto tracker receiver");

        GPSTracker gpsTracker = new GPSTracker(context);
        CurrentLocationPreferences cur = new CurrentLocationPreferences(context);
        cur.setLat(gpsTracker.getLatitude());
        cur.setLon(gpsTracker.getLongitude());

        Intent in = new Intent(context, LocationTrackerService.class);
        context.startService(in);
    }
}