package ifn372.sevencolors.watch_app.CustomSharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.model.FenceList;
import ifn372.sevencolors.watch_app.Constants;

/**
 * This class store all the patient's fences into SharedPreferences.
 * The value stored in the SharedPreferences is JSon string which is converted
 * from FenceList object.
 */
public class FenceSharedPreferences {
    private SharedPreferences sharedPreferences;
    JacksonFactory jacksonFactory;

    public FenceSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.sharedPreferences_user_fences,
                Context.MODE_PRIVATE);
        jacksonFactory = new JacksonFactory();
    }

    /**
     * Gets the JSon string from SharedPreferences and converts it into the object of
     * FenceList.
     * @return
     */
    public FenceList getFences(){
        String str = sharedPreferences.getString(Constants.sharedPreferences_user_fences,
                Constants.sharedPreferences_string_default_value);

        if(str.equals(Constants.sharedPreferences_string_default_value)) {
            return null;
        }

        FenceList fenceList = null;
        try {
            fenceList = jacksonFactory.createJsonParser(str).parse(FenceList.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fenceList;
    }

    /**
     * Auto convert the given FenceList Object into Json String and store it into
     * SharedPreferences.
     * @param fenceList
     */
    public void setFences(FenceList fenceList) {
        sharedPreferences.edit().putString(Constants.sharedPreferences_user_fences,
                fenceList.toString()).apply();
    }
}
