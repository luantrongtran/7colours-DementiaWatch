package ifn372.sevencolors.dementiawatch.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import ifn372.sevencolors.dementiawatch.Constants;

/**
 * Created by lua on 19/09/2015.
 *
 * This class store temporary fences when the carer/family member want to get the patient outside
 * fences. A patient only has 1 temporary fence
 */
public class TemporaryFenceSharedPreferences {
    SharedPreferences sharedPreferences;
    public TemporaryFenceSharedPreferences(Context context) {
        sharedPreferences
                = context.getSharedPreferences(Constants.sharedPreferences_temporary_fences,
                Context.MODE_PRIVATE);
    }

    public void addANewTemporaryFence(int patientId, int fenceId) {
        sharedPreferences.edit().putInt(Integer.toString(patientId), fenceId).apply();
    }

    public int getTemporaryFenceId(int patientId) {
        return sharedPreferences.getInt(Integer.toString(patientId),
                Constants.sharedPreferences_integer_default_value);
    }

    public void removeTemporaryFence(int patientId) {
        sharedPreferences.edit().remove(Integer.toString(patientId)).apply();
    }

    public Map<String, ?> getAllTemporaryFences() {
        return sharedPreferences.getAll();
    }
}
