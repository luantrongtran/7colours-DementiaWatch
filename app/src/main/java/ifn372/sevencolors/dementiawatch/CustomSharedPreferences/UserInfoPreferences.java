package ifn372.sevencolors.dementiawatch.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn372.sevencolors.dementiawatch.Constants;

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
}
