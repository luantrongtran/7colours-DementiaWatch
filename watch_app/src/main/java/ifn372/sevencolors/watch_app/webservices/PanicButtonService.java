package ifn372.sevencolors.watch_app.webservices;

import android.app.IntentService;
import android.content.Intent;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.watch_app.BackendApiProvider;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;

/**
 * Created by zach on 14/09/15.
 */
public class PanicButtonService extends IntentService {

    private static final String TAG = "PanicButtonService";

    public PanicButtonService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(this);
        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        try {
            myApi.panicButton(userInfoPreferences.getUserId(), "InPanic").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}