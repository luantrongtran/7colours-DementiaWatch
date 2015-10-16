package ifn372.sevencolors.dementiawatch.webservices;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.UserInfoPreferences;

/**
 * Created by lua on 16/10/2015.
 */
public class UnregisterGCMService extends AsyncTask <Void, Void, Void> {

    Context context;
    public UnregisterGCMService(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
        int userId = userInfoPreferences.getUserId();

        MyApi myApi = BackendApiProvider.getPatientApi();

        try {
            myApi.removeGcmId(userId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
