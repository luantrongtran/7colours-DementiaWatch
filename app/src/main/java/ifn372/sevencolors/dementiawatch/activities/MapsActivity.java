/**
 * References:
 * http://android-developers.blogspot.com.au/2011/06/deep-dive-into-location.html
 */

package ifn372.sevencolors.dementiawatch.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Carer;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;
import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.dementiawatch.CreateFenceActivity;
import ifn372.sevencolors.dementiawatch.PatientManager;
import ifn372.sevencolors.dementiawatch.R;
import ifn372.sevencolors.dementiawatch.backgroundservices.LocationAutoTracker;
import ifn372.sevencolors.dementiawatch.backgroundservices.LocationTrackerService;
import ifn372.sevencolors.dementiawatch.backgroundservices.UpdateCurrentLocationReceiver;
import ifn372.sevencolors.dementiawatch.parcelable.PatientListParcelable;
import ifn372.sevencolors.dementiawatch.webservices.DeleteFenceService;
import ifn372.sevencolors.dementiawatch.webservices.IDeleteFenceService;
import ifn372.sevencolors.dementiawatch.webservices.IFenceService;
import ifn372.sevencolors.dementiawatch.webservices.IUpdateFenceService;
import ifn372.sevencolors.dementiawatch.webservices.FenceService;
import ifn372.sevencolors.dementiawatch.webservices.UnregisterGCMService;
import ifn372.sevencolors.dementiawatch.webservices.UpdateFenceService;
import ifn372.sevencolors.dementiawatch.webservices.RegistrationIntentService;
import ifn372.sevencolors.dementiawatch.webservices.UpdatePatientsListReciever;
import ifn372.sevencolors.dementiawatch.webservices.UpdatePatientsListService;


public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static int PATIENT_SETTING_REQUEST_CODE = 1;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public long updatePatientsListInterval = 6*1000; //seconds
    public long outOfBoundCheckInterval = 3*1000; // seconds

    public long autoUpdateCurrentLocationInterval = 15*1000;//15s
    public long locationTrackerInterval = 10*1000;//10s
    UserInfoPreferences userPrefs;

    public static PatientManager patientManager = new PatientManager();

    GoogleApiClient googleApiClient;
    LocationRequest mLocationRequest;

    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mLeftMenuAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;
    //end navigation menu

    // Updating Fence Variables
    public int myFenceID = -1;
    public float myFenceRadius;
    // End Updating Fence Variables

    // Current location variables
    public double mcurLat;  // my current location Latitude
    public double mcurLng;  // my current location Longitude
    public LatLng mcurLatLng;   // my current location
    // End current location variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);
        userPrefs = new UserInfoPreferences(getApplicationContext());
        setUpGoogleApiClient();
        // get GCM token
        Intent regIntent = new Intent(this, RegistrationIntentService.class);
        startService(regIntent);

        //setUpDummyData();

        registerReceiver();

        scheduleAlarm();

        IntentFilter intentFilter = new IntentFilter(UpdatePatientsListService.ACTION);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(onPatientsListUpdateReceiver, intentFilter);

        setUpNavigationMenu();

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

    @Override
    protected void onDestroy() {
        patientManager.disableAllTemporaryFences(getApplicationContext());
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PATIENT_SETTING_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                patientManager.updateTemporaryFence(mMap, getApplicationContext());
                patientManager.updateLocationHistory(mMap, getApplicationContext());
            }
            Log.i(Constants.application_id, "Return from patient setting activity");
        }
    }

    /**
     * Register all necessary receivers
     */
    public void registerReceiver() {
        registerCurrentLocationUpdatedReceiver();
    }

    private void setUpNavigationMenu() {
        //Navigation Menu
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mLeftMenuAdapter = new LeftMenuAdapter(this);

        mRecyclerView.setAdapter(mLeftMenuAdapter);

        final GestureDetector oneTouchGesture = new GestureDetector(MapsActivity.this,
                new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View view = rv.findChildViewUnder(e.getX(), e.getY());

                if (view != null && oneTouchGesture.onTouchEvent(e)) {
                    drawer.closeDrawers();

//                    int clickedIndex = rv.getChildAdapterPosition(view);
//                    if (clickedIndex == 0) {
//                        //If the header was clicked
//                        View header = rv.getChildAt(clickedIndex);
//
//                        return true;
//                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);


        drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

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

    public void scheduleAlarm() {
        scheduleAutoUpdatePatientsList();
//        scheduleAutoCheckPatientLost();
//        scheduleAutoCheckPatientsOutOfBound();
//        scheduleAutoLocationTrackerAlarm();
//        scheduleAutoUpdateCurrentLocationAlarm();
    }

    public void stopAllScheduleAlarm(){
        Intent intent = new Intent(getApplicationContext(), UpdatePatientsListReciever.class);
        PendingIntent autoUpdatePatientsListPendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarm.cancel(autoUpdatePatientsListPendingIntent);
    }

//    public void setUpDummyData() {
//        //The user, a carer, information
//        UserInfoPreferences userPrefs = new UserInfoPreferences(getApplicationContext());
//        userPrefs.setUserId(3);
//        userPrefs.setRole(2);
//
//        userPrefs.setFullName("Carer");
//        userPrefs.setEmail("Carer@gmail.com");
////        userPrefs.setProfilePicture("");
//    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        patientManager.resume(this);
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
    }


    public void scheduleAutoUpdatePatientsList() {
        Intent intent = new Intent(getApplicationContext(), UpdatePatientsListReciever.class);
        PendingIntent autoUpdatePatientsListPendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        scheduleAutoTask(autoUpdatePatientsListPendingIntent, updatePatientsListInterval);
    }

    private BroadcastReceiver onPatientsListUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(Constants.application_id, "Maps Activity received patient list updated event");
            PatientListParcelable p = intent.getParcelableExtra("patientList");

            patientManager.setPatientList(p.getPatientList());
            patientManager.updatePatientsMarkerOnMap(mMap, getApplicationContext());
//            patientManager.updateLocationHistory(mMap, getApplicationContext());

            mLeftMenuAdapter.notifyDataSetChanged(); //update patient list on left menu
        }
    };

    public void scheduleAutoTask(PendingIntent pendingIntent, long interval) {
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                interval, pendingIntent);
    }

    public void scheduleAutoLocationTrackerAlarm() {
        Intent intent = new Intent(getApplicationContext(), LocationAutoTracker.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(this,
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        scheduleAutoTask(pIntent, locationTrackerInterval);
    }

    public void scheduleAutoUpdateCurrentLocationAlarm() {
        Intent intent = new Intent(getApplicationContext(), UpdateCurrentLocationReceiver.class);
        PendingIntent pIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        scheduleAutoTask(pIntent, autoUpdateCurrentLocationInterval);
    }

    public void retrieveMyCurrentLocation()
    {
        // We could get the location with the LocationManager
        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // Get latitude of the current location
        mcurLat = myLocation.getLatitude();

        // Get longitude of the current location
        mcurLng = myLocation.getLongitude();

//        // Or we could also get the location stored in the SharedPreferences
//        CurrentLocationPreferences currentLocationPreferences
//                = new CurrentLocationPreferences(getApplicationContext());
//
//        mcurLat = currentLocationPreferences.getLat();
//        mcurLng = currentLocationPreferences.getLon();

        // Store the LatLng object for the current location
        mcurLatLng = new LatLng(mcurLat, mcurLng);

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

            patientManager.updateTemporaryFence(mMap, getApplicationContext());
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

    protected void stopLocationUpdates(){
        Intent intent = new Intent(getApplicationContext(), LocationAutoTracker.class);
        PendingIntent pIntent = PendingIntent.
                getBroadcast(getApplicationContext(), 0
                        , intent, PendingIntent.FLAG_UPDATE_CURRENT);
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, pIntent);
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    //End google Api clients callback methods

    public void panToLatLng(LatLng latLng, boolean zoom) {
        if (zoom) {
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(getCameraPosition(latLng)));
        } else {
            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(getCameraPositionWithoutZoom(latLng)));
        }
    }

    public CameraPosition getCameraPosition(LatLng latLng) {
        return new CameraPosition.Builder()
                .target(latLng)
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
    }

    public CameraPosition getCameraPositionWithoutZoom(LatLng latLng) {
        return new CameraPosition.Builder()
                .target(latLng)
                .zoom(mMap.getCameraPosition().zoom)
                .build();
    }

    public void signOut(View view){
        //unregister GCM
        UnregisterGCMService unregisterGCMService
                = new UnregisterGCMService(this);
        unregisterGCMService.execute();

        stopLocationUpdates();
        stopAllScheduleAlarm();

        userPrefs.signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}