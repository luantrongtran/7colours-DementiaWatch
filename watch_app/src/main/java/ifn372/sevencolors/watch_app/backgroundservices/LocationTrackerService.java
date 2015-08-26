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
import ifn372.sevencolors.watch_app.SharedPreferencesUtitlies;

/**
 * This service will be called by LocationAutooTracker and stores current location into
 * SharedPreferences
 */
public class LocationTrackerService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public GoogleApiClient mGoogleApiClient;
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10m

    public LocationTrackerService(){
        super("UpdateLocationService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("MyAPI", "update location service");
        buildGoogleApiClient();

//        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//
//        Log.e("MyAPI", "My current location: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("MyAPI", "Google api client connected");

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        Log.e("MyAPI", "My current location before storing: " + lastLocation.getLatitude() + ", " + lastLocation.getLongitude());


        SharedPreferences sharedPref = getApplicationContext().
                getSharedPreferences(Constants.sharedPreferences_current_location, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        SharedPreferencesUtitlies.putDoubleIntoSharedPreferences(editor,
                Constants.sharedPreferences_current_location_lat, lastLocation.getLatitude());
        SharedPreferencesUtitlies.putDoubleIntoSharedPreferences(editor,
                Constants.sharedPreferences_current_location_lon, lastLocation.getLongitude());

        editor.commit();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected synchronized void buildGoogleApiClient() {
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
        }
        if(mGoogleApiClient.isConnected() == false){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("MyAPI", "Google api client failed to connect");
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest LocationRequest = null;
        if(LocationRequest == null) {
            LocationRequest = new LocationRequest();
            LocationRequest.setInterval(UPDATE_INTERVAL);
            LocationRequest.setFastestInterval(FATEST_INTERVAL);
            LocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationRequest.setSmallestDisplacement(DISPLACEMENT);
        }

        return LocationRequest;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    createLocationRequest(), this);
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("MyAPI", "Location changed");
    }
}
