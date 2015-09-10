package ifn372.sevencolors.dementiawatch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ifn372.sevencolors.dementiawatch.webservices.ISayHi;
import ifn372.sevencolors.dementiawatch.webservices.SayHi;

public class MainActivity extends AppCompatActivity implements ISayHi {

    private PendingIntent pendingIntent;
    private AlarmManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void sayHi(View view) {
        EditText nameEdt = (EditText)findViewById(R.id.txtName);
        String name = nameEdt.getText().toString();
        SayHi sh = new SayHi(this);
        sh.execute(name);
    }

    @Override
    public void processData(String greeting) {
        Toast.makeText(this, greeting, Toast.LENGTH_SHORT).show();
    }

    public void btnChangeEvent(View view)
    {
        Intent intent = new Intent(MainActivity.this, CreateFenceActivity.class);
        startActivity(intent);
    }
}
