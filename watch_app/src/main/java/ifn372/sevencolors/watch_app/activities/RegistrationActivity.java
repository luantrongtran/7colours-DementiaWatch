package ifn372.sevencolors.watch_app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;
import ifn372.sevencolors.watch_app.R;
import ifn372.sevencolors.watch_app.webservices.CreateNewAccountService;
import ifn372.sevencolors.watch_app.webservices.ICreateNewAccountService;


public class RegistrationActivity extends AppCompatActivity implements ICreateNewAccountService {
    EditText txtFullName;
    EditText txtUsername;
    EditText txtPassword;
    EditText txtConfirmPass;

    Patient patient = new Patient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        txtFullName = (EditText)findViewById(R.id.fullname);
        txtUsername = (EditText)findViewById(R.id.username);
        txtPassword = (EditText)findViewById(R.id.password);
        txtConfirmPass = (EditText)findViewById(R.id.confirm_password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
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

    public void createNewAccount(View view) {
        String errorMsg = validateForm();
        if(!errorMsg.isEmpty()){
            Toast.makeText(RegistrationActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        CreateNewAccountService createNewAccountService = new CreateNewAccountService(this);
        createNewAccountService.execute(patient);
        finish();
    }

    private String validateForm() {
        String errorMsg = "";
        String fullname = txtFullName.getText().toString();
        if(fullname.isEmpty()){
            errorMsg += getString(R.string.fullname_input_empty_error) + "\n";
        }
        String username = txtUsername.getText().toString();
        if(username.isEmpty()) {
            errorMsg += getString(R.string.username_input_empty_error) + "\n";
        }

        String password = txtPassword.getText().toString();
        if(password.isEmpty()){
            errorMsg += getString(R.string.password_input_empty_error) + "\n";
        }
        String confirmPass= txtConfirmPass.getText().toString();
        if(!confirmPass.equals(password)){
            errorMsg += getString(R.string.confirm_password_wrong) + "\n";
        }

        patient.setFullName(fullname);
        patient.setUserName(username);
        patient.setPassword(password);
        patient.setRole(UserInfoPreferences.PATIENT_ROLE);
        patient.setCarerId(-1);

        return errorMsg;
    }

    @Override
    public void OnAfterNewAccountCreated(Patient carer) {
        if(carer == null){
            Toast.makeText(RegistrationActivity.this, getString(R.string.registration_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RegistrationActivity.this, getString(R.string.registration_success),
                    Toast.LENGTH_SHORT).show();

            Toast.makeText(RegistrationActivity.this, getString(R.string.login_after_registration),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void goToSignIn(View view){
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
        finish();
    }
}
