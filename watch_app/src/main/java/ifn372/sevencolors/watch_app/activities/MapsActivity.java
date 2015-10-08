package ifn372.sevencolors.watch_app.activities;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.watch_app.FenceManager;
import ifn372.sevencolors.watch_app.R;
import ifn372.sevencolors.watch_app.backgroundservices.LocationAutoTracker;
import ifn372.sevencolors.watch_app.backgroundservices.LocationTrackerService;
import ifn372.sevencolors.watch_app.backgroundservices.UpdateCurrentLocationReceiver;
import ifn372.sevencolors.watch_app.webservices.GetFencesService;
import ifn372.sevencolors.watch_app.webservices.PanicButtonService;


public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    GoogleApiClient googleApiClient;
    LocationRequest mLocationRequest;

    FenceManager fenceManager;

    public long autoUpdateCurrentLocationInterval = 15*1000;//15s
    public long locationTrackerInterval = 10*1000;//10s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // mapFragment.getMapAsync(this);
        setUpDummyData();
        scheduleAlarm();

        setUpGoogleApiClient();
        //sendPanicAlert();

        registerOnFencesUpdated();
    }

    public void sendPanicAlert() {
        Intent regIntent = new Intent(this, PanicButtonService.class);
        startService(regIntent);
    }

    public void scheduleAlarm() {
//        scheduleAutoLocationTrackerAlarm();
//        scheduleAutoUpdateCurrentLocationAlarm();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    public void setUpDummyData() {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(getApplicationContext());
        userInfoPreferences.setUserId(1);
    }

    //    @Override
//    public void onMapReady(GoogleMap map) {
//        // Add a marker in Sydney, Australia, and move the camera.
//        LatLng sydney = new LatLng(-34, 151);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//    }
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker").snippet("Snippet"));

        // Enable MyLocation Layer of Google Map
        mMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
//        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!").snippet("Consider yourself located"));

        //Update again the list of fences when resumes activity.
        updateFenceList();
        fenceManager = new FenceManager(getApplicationContext(), mMap);
    }

    public void scheduleAutoTask(PendingIntent pendingIntent, long interval) {
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                interval, pendingIntent);
    }

    public void registerCurrentLocationUpdatedReceiver() {
        IntentFilter onCurrentLocationUpdatedFilter
                = new IntentFilter(LocationTrackerService.ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onCurrentLocationUpdated, onCurrentLocationUpdatedFilter);
    }

    private BroadcastReceiver onCurrentLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CurrentLocationPreferences currLocPrefs
                    = new CurrentLocationPreferences(getApplicationContext());
            Log.i(Constants.application_id, "Receive current location updated:" +
                    currLocPrefs.getLat() + ", " + currLocPrefs.getLon());
        }
    };

    //Google Api clients callback methods
    @Override
    public void onConnected(Bundle bundle) {
        Log.e(Constants.application_id, "Google API client connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(Constants.application_id, "Google API client suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(Constants.application_id, "Google API client failed");
    }

    protected void startLocationUpdates() {
        Intent intent = new Intent(getApplicationContext(), LocationAutoTracker.class);
        PendingIntent pIntent = PendingIntent.
                getBroadcast(getApplicationContext(), 0
                        , intent, PendingIntent.FLAG_UPDATE_CURRENT);

        LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, mLocationRequest, pIntent);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    //End google Api clients callback methods

    public void setUpGoogleApiClient() {
        createLocationRequest();//set up for location request

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void updateFenceList() {
        Log.i(Constants.application_id, "update Fence list");
        Intent intent = new Intent(getApplicationContext(), GetFencesService.class);
        startService(intent);
    }

    private void registerOnFencesUpdated() {
        IntentFilter intentFilter = new IntentFilter(GetFencesService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onFencesUpdated, intentFilter);
    }

    private BroadcastReceiver onFencesUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fenceManager.updateFences();
        }
    };
}