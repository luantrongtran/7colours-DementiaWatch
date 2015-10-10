package ifn372.sevencolors.dementiawatch.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn372.sevencolors.backend.myApi.model.Carer;
import ifn372.sevencolors.dementiawatch.Constants;

/**
 * Created by lua on 3/09/2015.
 */
public class UserInfoPreferences {
    public static int PATIENT_ROLE = 1;
    public static int CARER_ROLE = 2;
    public static int RELATIVE_ROLE = 3;

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

    public void setProfilePicture(String url) {
        sharedPreferences.edit().putString(Constants.sharedPreferences_user_info_profile_picture,
                url).apply();
    }

    public String getProfilePicture() {
        return sharedPreferences.getString(Constants.sharedPreferences_user_info_profile_picture,
                Constants.sharedPreferences_string_default_value);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(Constants.sharedPreferences_user_info_is_logged_in,
                false);
    }

    public void signIn(Carer carer) {
        setUserInfo(carer);
        sharedPreferences.edit().putBoolean(Constants.sharedPreferences_user_info_is_logged_in,
                true).apply();
    }

    public void signOut() {
        sharedPreferences.edit().clear().apply();
    }

    public void setUserInfo(Carer carer){
        setFullName(carer.getFullName());
        setUserId(carer.getId());
        setRole(carer.getRole());
    }
}
