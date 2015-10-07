package ifn372.sevencolors.watch_app.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn372.sevencolors.watch_app.Constants;

/**
 * Created by lua on 3/09/2015.
 */
public class UserInfoPreferences {
    private SharedPreferences sharedPreferences;

    public UserInfoPreferences(Context applicationContext) {
        sharedPreferences = applicationContext
                .getSharedPreferences(Constants.sharedPreferences_user_info, Context.MODE_PRIVATE);
    }

    public void setUserId(int userId) {
        sharedPreferences.edit().putInt(Constants.sharedPreferences_user_info_id, userId).apply();
    }

    public int getUserId() {
        return sharedPreferences
                .getInt(Constants.sharedPreferences_user_info_id,
                        Constants.sharedPreferences_integer_default_value);
    }

    public void setRole(int role) {
        sharedPreferences.edit().putInt(Constants.sharedPreferences_user_info_role, role).apply();

    }

    public int getRole() {
        return sharedPreferences.getInt(Constants.sharedPreferences_user_info_role,
                Constants.sharedPreferences_integer_default_value);
    }

    public void setFullName(String fullName) {
        sharedPreferences.edit().putString(Constants.sharedPreferences_user_info_fullname,
                fullName).apply();
    }

    public String getFullName() {
        return sharedPreferences.getString(Constants.sharedPreferences_user_info_fullname,
                Constants.sharedPreferences_string_default_value);
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(Constants.sharedPreferences_user_info_email,
                email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(Constants.sharedPreferences_user_info_email,
                Constants.sharedPreferences_string_default_value);
    }

    public void setSafe(boolean isSafe) {
        sharedPreferences.edit().putBoolean(Constants.sharedPreferences_user_info_is_safe, isSafe)
        .apply();
    }

    public boolean isSafe() {
        return sharedPreferences.getBoolean(Constants.sharedPreferences_user_info_is_safe,
                true);
    }

    public void setFirstMomentOutside(long millisecond) {
        sharedPreferences.edit().putLong(Constants.sharedPreferences_user_info_first_moment_outside_fences,
                millisecond).apply();
    }

    public long getFirstMomentOutside() {
        return sharedPreferences
                .getLong(Constants.sharedPreferences_user_info_first_moment_outside_fences, 0);
    }

    public boolean getUpdateLocationToServer() {
        return sharedPreferences.getBoolean(Constants.sharedPreferences_user_info_update_location_to_server,
                true);
    }

    public void setUpdateLocationToServer(boolean value) {
        sharedPreferences.edit().putBoolean(Constants
                        .sharedPreferences_user_info_update_location_to_server, value).apply();
    }
}
