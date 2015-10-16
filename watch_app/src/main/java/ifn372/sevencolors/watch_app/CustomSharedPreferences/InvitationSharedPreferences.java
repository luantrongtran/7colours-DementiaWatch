package ifn372.sevencolors.watch_app.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import ifn372.sevencolors.watch_app.Constants;

/**
 * Created by lua on 16/10/2015.
 */
public class InvitationSharedPreferences {
    SharedPreferences sharedPreferences;

    public InvitationSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.sharedPreferences_invitation,
                Context.MODE_PRIVATE);
    }

    public void setCarerId(int carerId) {
        sharedPreferences.edit().putInt(Constants.sharedPreferences_invitation_carer_id,
                carerId).apply();
    }

    public int getCarerId() {
        return sharedPreferences.getInt(Constants.sharedPreferences_invitation_carer_id,
                Constants.sharedPreferences_integer_default_value);
    }

    public void setInvitationMessage(String msg) {
        sharedPreferences.edit().putString(Constants.gcm_message, msg).apply();
    }

    public String getInvitationMessage() {
        return sharedPreferences.getString(Constants.gcm_message,
                Constants.sharedPreferences_string_default_value);
    }

    public void clear() {
        sharedPreferences.edit().clear();
    }
}
