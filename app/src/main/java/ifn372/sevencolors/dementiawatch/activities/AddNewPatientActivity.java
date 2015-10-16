package ifn372.sevencolors.dementiawatch.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ifn372.sevencolors.backend.myApi.model.ResultCode;
import ifn372.sevencolors.dementiawatch.R;
import ifn372.sevencolors.dementiawatch.webservices.ISendPatientInvitationService;
import ifn372.sevencolors.dementiawatch.webservices.SendPatientInvitationService;

public class AddNewPatientActivity extends AppCompatActivity
        implements ISendPatientInvitationService {

    TextView tvUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_patient);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        setTitle(R.string.add_new_patient_activity_title);

        tvUsername = (TextView) findViewById(R.id.patientUsername);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_new_patient, menu);
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

    public void sendInvitation(View view) {
        SendPatientInvitationService sendPatientInvitationService
                = new SendPatientInvitationService(getApplicationContext(),this);

        String patientUsername = (tvUsername.getText().toString());
        sendPatientInvitationService.execute(patientUsername);
    }

    @Override
    public void onInvitationSent(ResultCode resultCode) {
        if(resultCode.getResult() == true) {
            Toast.makeText(AddNewPatientActivity.this, R.string.invitation_sent_successfully,
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddNewPatientActivity.this, resultCode.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
