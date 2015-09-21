package ifn372.sevencolors.watch_app.webservices;

import android.app.IntentService;
import android.content.Intent;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.watch_app.BackendApiProvider;

/**
 * Created by zach on 14/09/15.
 */
public class PanicButtonService extends IntentService {

    private static final String TAG = "PanicButtonService";
    private static final int PATIENT_ID = 1;

    public PanicButtonService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        try {
            myApi.panicButton(PATIENT_ID, "InPanic").execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
