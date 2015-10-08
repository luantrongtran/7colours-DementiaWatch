package ifn372.sevencolors.watch_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import ifn372.sevencolors.watch_app.R;
import ifn372.sevencolors.watch_app.webservices.PanicButtonService;

public class MainActivity extends AppCompatActivity {

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
        startActivity(intent);
    }
}
