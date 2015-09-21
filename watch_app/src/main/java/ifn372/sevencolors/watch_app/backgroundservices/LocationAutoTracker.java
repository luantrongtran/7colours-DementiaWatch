package ifn372.sevencolors.watch_app.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ifn372.sevencolors.watch_app.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.watch_app.GPSTracker;

/**
 * This class track the current location based on what scheduled in the MapsActivity
 */
public class LocationAutoTracker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GPSTracker gpsTracker = new GPSTracker(context);
        CurrentLocationPreferences cur = new CurrentLocationPreferences(context);
        cur.setLat(gpsTracker.getLatitude());
        cur.setLon(gpsTracker.getLongitude());

        Intent in = new Intent(context, LocationTrackerService.class);
        context.startService(in);
    }
}
