package ifn372.sevencolors.watch_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by lua on 13/10/2015.
 */
public class InvitationConfirmBox {
    AlertDialog.Builder builder;


    public void show(Context context, Intent intent) {
        builder = new AlertDialog.Builder(context);

        String title = intent.getStringExtra(Constants.gcm_title);
        String message = intent.getStringExtra(Constants.gcm_message);
        int carerId = Integer.valueOf(intent.getStringExtra(Constants.gcm_carer_id));

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        builder.setMessage(message).setPositiveButton("Accept", dialogClickListener)
                .setNegativeButton("Deny", dialogClickListener).show();
    }
}
