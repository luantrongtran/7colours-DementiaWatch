package ifn372.sevencolors.dementiawatch.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.dementiawatch.Constants;
import ifn372.sevencolors.dementiawatch.CreateFenceActivity;
import ifn372.sevencolors.dementiawatch.R;

/**
 * This activity is called from another activity, and the extra information passed into
 * this activity should be patient id
 */
public class PatientSettingActivity extends AppCompatActivity {

    Patient patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final int patientId = intent.getIntExtra(Constants.create_new_fence_intent_data,
                Constants.sharedPreferences_integer_default_value);
        this.patient = MapsActivity.patientManager.getPatientById(patientId);

        setTitle(patient.getFullName() +"'s "
                + getResources().getString(R.string.patient_setting_activity_title));

        ToggleButton pickedUpModeButton = (ToggleButton)findViewById(R.id.pickedUpModeToggleButton);
        pickedUpModeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    boolean isEnabled = MapsActivity.patientManager
                            .enablePickedUpMode(patientId, getApplicationContext());

                    if(isEnabled == true) {
                        Toast.makeText(PatientSettingActivity.this,
                                R.string.patient_setting_enable_picked_up_mode_success,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PatientSettingActivity.this,
                                R.string.patient_setting_enable_picked_up_mode_failed,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // The toggle is disabled
                    boolean isDisabled = MapsActivity.patientManager
                            .disablePickedUpMode(patientId);

                    if(isDisabled == true) {
                        Toast.makeText(PatientSettingActivity.this,
                                R.string.patient_setting_disable_picked_up_mode_success,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_setting, menu);
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

    public void goToCreateFenceActivity(View view) {
        Intent intent = new Intent(this, CreateFenceActivity.class);
        intent.putExtra(Constants.create_new_fence_intent_data, patient.getId());
        startActivity(intent);
    }
}