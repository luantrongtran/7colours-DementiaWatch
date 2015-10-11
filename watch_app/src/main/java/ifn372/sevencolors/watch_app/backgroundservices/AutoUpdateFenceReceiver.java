package ifn372.sevencolors.watch_app.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ifn372.sevencolors.watch_app.webservices.GetFencesService;

/**
 * Created by lua on 11/10/2015.
 */
public class AutoUpdateFenceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, GetFencesService.class);
        context.startService(in);
    }
}
