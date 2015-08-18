package ifn372.sevencolors.watch_app.backgroundservices;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by lua on 17/08/2015.
 */
public class UpdateCurrentLocationService extends IntentService {

    public UpdateCurrentLocationService() {
        super("UpdateCurrentLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
