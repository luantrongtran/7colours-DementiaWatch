package ifn372.sevencolors.watch_app.activities;
import android.content.Intent;
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
import ifn372.sevencolors.watch_app.webservices.ILoginService;
import ifn372.sevencolors.watch_app.webservices.LoginService;

public class LoginActivity extends AppCompatActivity implements ILoginService {

    EditText txtUsername;
    EditText txtPassword;

    Patient patient = new Patient();
    UserInfoPreferences userInfoPreferences;

    private boolean isLoggedIn() {
        return userInfoPreferences.isLoggedIn();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoPreferences
                = new UserInfoPreferences(getApplicationContext());

        txtUsername = (EditText)findViewById(R.id.username);
        txtPassword = (EditText)findViewById(R.id.password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isLoggedIn()){
            //if the user is logged in
            goToMainActivity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view){
        String errorMsg = validateForm();
        if(!errorMsg.isEmpty()){
            Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            return;
        }

        LoginService loginService = new LoginService(this);
        loginService.execute(patient);
    }

    private String validateForm() {
        String errorMsg = "";

        String username = txtUsername.getText().toString().trim();
        if(username.isEmpty()) {
            errorMsg += getString(R.string.username_input_empty_error) + "\n";
        }

        String password = txtPassword.getText().toString().trim();
        if(password.isEmpty()){
            errorMsg += getString(R.string.password_input_empty_error) + "\n";
        }

        patient.setUserName(username);
        patient.setPassword(password);

        return errorMsg;
    }

    @Override
    public void OnLoginServiceFinished(Patient patient) {
        if(patient == null){
            //Login failed
            Toast.makeText(LoginActivity.this, getString(R.string.login_failed),
                    Toast.LENGTH_SHORT).show();
        } else
        {
            Toast.makeText(LoginActivity.this, getString(R.string.login_successful),
                    Toast.LENGTH_SHORT).show();

            //Store user info into SharedPreferences
            userInfoPreferences.signIn(patient);

            //Start MapsActivity
            goToMainActivity();
        }
    }

    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}