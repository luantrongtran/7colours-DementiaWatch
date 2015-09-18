package ifn372.sevencolors.dementiawatch.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.SharedPreferencesUtitlies;

public class CurrentLocationPreferences {
    SharedPreferences sharedPreferences;

    public CurrentLocationPreferences(Context applicationContext) {
        sharedPreferences = applicationContext
                .getSharedPreferences(Constants.sharedPreferences_current_location,
                        Context.MODE_PRIVATE);
    }

    public void setLat(double lat) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferencesUtitlies
                .putDoubleIntoSharedPreferences(editor,
                        Constants.sharedPreferences_current_location_lat, lat);

        editor.apply();
    }

    public double getLat() {
        return SharedPreferencesUtitlies.getDoubleFromSharedPreferences(sharedPreferences,
                Constants.sharedPreferences_current_location_lat,
                Constants.sharedPreferences_float_default_value);
    }

    public void setLon(double lon) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        SharedPreferencesUtitlies
                .putDoubleIntoSharedPreferences(editor,
                        Constants.sharedPreferences_current_location_lon, lon);

        editor.apply();
    }

    public double getLon() {
        return SharedPreferencesUtitlies.getDoubleFromSharedPreferences(sharedPreferences,
                Constants.sharedPreferences_current_location_lon,
                Constants.sharedPreferences_float_default_value);
    }
}
