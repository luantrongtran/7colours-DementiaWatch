package ifn372.sevencolors.dementiawatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by zach on 29/08/15.
 */
public class CheckReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        // For our recurring task, we'll just display a message
        //Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();

        Intent in = new Intent(arg0, OutOfBoundCheck.class);
        arg0.startService(in);
    }
}
