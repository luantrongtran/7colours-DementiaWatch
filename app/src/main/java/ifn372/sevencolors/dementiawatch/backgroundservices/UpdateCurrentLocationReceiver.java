package ifn372.sevencolors.dementiawatch.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lua on 24/08/2015.
 */
public class UpdateCurrentLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, UpdateCurrentLocationService.class);
        context.startService(in);
    }
}
