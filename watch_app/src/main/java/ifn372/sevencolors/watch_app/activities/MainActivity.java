package ifn372.sevencolors.watch_app.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.R;
import ifn372.sevencolors.watch_app.webservices.MyGcmListenerService;
import ifn372.sevencolors.watch_app.webservices.PanicButtonService;
import ifn372.sevencolors.watch_app.webservices.RegistrationIntentService;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button panicButton = (Button)findViewById(R.id.panicBtn);
        Button showMapButton = (Button)findViewById(R.id.showMapBtn);

        panicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sendPanicAlert();
                return false;
            }
        });

        showMapButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                navigateToMapActivity();
                return false;
            }
        });

        registerOnInvitationReceived();

        builder = new AlertDialog.Builder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // get GCM token
        Intent regIntent = new Intent(this, RegistrationIntentService.class);
        startService(regIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendPanicAlert() {
        Intent regIntent = new Intent(this, PanicButtonService.class);
        startService(regIntent);
    }

    public void navigateToMapActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 0);
    }

    public void registerOnInvitationReceived() {
        IntentFilter intentFilter = new IntentFilter(MyGcmListenerService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(onInvitationReceived, intentFilter);
    }

    private BroadcastReceiver onInvitationReceived = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String title = intent.getStringExtra(Constants.gcm_title);
            String message = intent.getStringExtra(Constants.gcm_message);

            builder.setMessage(message).setPositiveButton("Accept", dialogClickListener)
                    .setNegativeButton("Deny", dialogClickListener).show();
        }
    };
}
