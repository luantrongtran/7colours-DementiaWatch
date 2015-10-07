package ifn372.sevencolors.watch_app.backgroundservices;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Location;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.watch_app.BackendApiProvider;
import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.watch_app.SharedPreferencesUtitlies;

/**
 * This class update current location to the backend automatically on scheduled time
 */
public class UpdateCurrentLocationService extends IntentService {

    public UpdateCurrentLocationService() {
        super("UpdateCurrentLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();

        SharedPreferences currentLocationSharedPref = getApplicationContext().
                getSharedPreferences(Constants.sharedPreferences_current_location, MODE_PRIVATE);

        double lat = SharedPreferencesUtitlies.getDoubleFromSharedPreferences(currentLocationSharedPref,
                Constants.sharedPreferences_current_location_lat, 0);
        double lon = SharedPreferencesUtitlies.getDoubleFromSharedPreferences(currentLocationSharedPref,
                Constants.sharedPreferences_current_location_lon, 0);

        Location currentLocation = new Location();
        currentLocation.setLat(lat);
        currentLocation.setLon(lon);

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);

        Patient patient = new Patient();
        patient.setId(userInfoPreferences.getUserId());

        patient.setCurrentLocation(currentLocation);

        Log.e(Constants.application_id, userInfoPreferences.getUpdateLocationToServer()+"");
        if (userInfoPreferences.getUpdateLocationToServer()) {
            MyApi patientApi = BackendApiProvider.getPatientApiBuilder();
            try {
                patientApi.updatePatientCurrentLocation(patient).execute();
                Log.e(Constants.application_id, "Update location to backend");

            } catch (IOException e) {
                Log.e(Constants.application_id, "Failed update location to backend");
                e.printStackTrace();
            }
        }
    }
}
