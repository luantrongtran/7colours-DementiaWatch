package ifn372.sevencolors.watch_app;

import android.content.SharedPreferences;

/**
 * References:
 * put and get double using SharedPreferences
 * http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
 *
 */
public class Utitlies {
    public static void putDoubleIntoSharedPreferences(SharedPreferences.Editor editor, String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static double getDoubleFromSharedPreferences(SharedPreferences sharedPref, String key, double defaultValue) {
        if ( !sharedPref.contains(key)) {
            return defaultValue;
        }

        return Double.doubleToLongBits(sharedPref.getLong(key, 0));
    }
}
