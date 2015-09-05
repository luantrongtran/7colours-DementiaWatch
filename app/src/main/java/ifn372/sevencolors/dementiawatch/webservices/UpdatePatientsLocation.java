package ifn372.sevencolors.dementiawatch.webservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ifn372.sevencolors.dementiawatch.Constants;

/**
 * Created by lua on 27/08/2015.
 */
public class UpdatePatientsLocation extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(Constants.application_id, "update patients list receiver");
        Intent in = new Intent(context, UpdatePatientsLocationService.class);
        context.startService(in);
    }
}
