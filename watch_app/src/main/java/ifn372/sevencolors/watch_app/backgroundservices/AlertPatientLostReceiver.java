package ifn372.sevencolors.watch_app.backgroundservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlertPatientLostReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent in = new Intent(context, AlertPatientLostService.class);
        context.startService(in);
    }

}
