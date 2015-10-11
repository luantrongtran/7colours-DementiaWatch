package ifn372.sevencolors.watch_app.webservices;

import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Carer;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.watch_app.BackendApiProvider;

/**
 * Created by lua on 8/10/2015.
 */
public class LoginService extends AsyncTask<Patient, Void, Patient> {
    ILoginService interfaceLoginService;
    public LoginService(ILoginService interfaceLoginService){
        this.interfaceLoginService = interfaceLoginService;
    }
    @Override
    protected Patient doInBackground(Patient... params) {
        Patient patient = params[0];

        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        try {
            patient = myApi.authenticatePatient(patient).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    protected void onPostExecute(Patient patient) {
        interfaceLoginService.OnLoginServiceFinished(patient);
    }
}
