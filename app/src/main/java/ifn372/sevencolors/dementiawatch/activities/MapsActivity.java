package ifn372.sevencolors.dementiawatch.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
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

import ifn372.sevencolors.dementiawatch.CheckReceiver;
import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.dementiawatch.CreateFenceActivity;
import ifn372.sevencolors.dementiawatch.PatientManager;
import ifn372.sevencolors.dementiawatch.R;
import ifn372.sevencolors.dementiawatch.parcelable.PatientListParcelable;
import ifn372.sevencolors.dementiawatch.webservices.UpdatePatientsListReciever;
import ifn372.sevencolors.dementiawatch.webservices.UpdatePatientsListService;


public class MapsActivity extends AppCompatActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    public long updatePatientsListInterval = 6*1000; //seconds
    public long outOfBoundCheckInterval = 8*1000; // seconds

    public static PatientManager patientManager = new PatientManager();

    //Navigation menu
    String NAME = "Carer 1";
    String EMAIL = "carer1@gmail.com";
    int PROFILE = R.drawable.profile;

    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mLeftMenuAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout drawer;

    ActionBarDrawerToggle mDrawerToggle;
    //end navigation menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);

        setUpDummyData();

        scheduleAlarm();

        IntentFilter intentFilter = new IntentFilter(UpdatePatientsListService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onPatientsListUpdateReceiver, intentFilter);

        setUpNavigationMenu();
    }

    private void setUpNavigationMenu() {
        //Navigation Menu
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mLeftMenuAdapter = new LeftMenuAdapter(NAME,EMAIL,PROFILE);

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
                    int clickedIndex = rv.getChildAdapterPosition(view);
                    if (clickedIndex == 0) {
                        //If the header was clicked
                        return false;
                    }

                    int index = clickedIndex - 1;
                    Intent intent = new Intent(MapsActivity.this, CreateFenceActivity.class);
                    intent.putExtra(Constants.create_new_fence_intent_data, index);
                    startActivity(intent);


                    return true;
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

    public void scheduleAlarm() {
        scheduleAutoUpdatePatientsList();
//        scheduleAutoCheckPatientsOutOfBound();
    }

    public void setUpDummyData() {
        //The user, a carer, information
        UserInfoPreferences userPrefs = new UserInfoPreferences(getApplicationContext());
        userPrefs.setUserId(3);
        userPrefs.setRole(2);
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
    }


    public void scheduleAutoUpdatePatientsList() {
        Intent intent = new Intent(getApplicationContext(), UpdatePatientsListReciever.class);
        PendingIntent pIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, updatePatientsListInterval, pIntent);
    }

    public void scheduleAutoCheckPatientsOutOfBound(){
        // Retrieve a PendingIntent that will perform a broadcast
        Intent alarmIntent = new Intent(this, CheckReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        long interval = outOfBoundCheckInterval;

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                interval, pendingIntent);
    }

    private BroadcastReceiver onPatientsListUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(Constants.application_id, "Maps Activity received patient list update event");
            PatientListParcelable p = intent.getParcelableExtra("patientList");
            patientManager.setPatientList(p.getPatientList());
            updateMap();
            mLeftMenuAdapter.notifyDataSetChanged(); //update patient list on left menu
        }
    };

    public void updateMap() {
        patientManager.updatePatientsMarkerOnMap(mMap);
    }
}