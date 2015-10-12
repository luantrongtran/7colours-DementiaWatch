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
public class CreateNewAccountService extends AsyncTask<Patient, Void, Patient> {

    ICreateNewAccountService interfaceCreateNewAccount;
    public CreateNewAccountService(ICreateNewAccountService interfaceCreateNewAccount)
    {
        this.interfaceCreateNewAccount = interfaceCreateNewAccount;
    }
    @Override
    protected Patient doInBackground(Patient... params) {
        Patient newPatient = params[0];

        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        try {
            newPatient = myApi.createPatient(newPatient).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newPatient;
    }

    @Override
    protected void onPostExecute(Patient patient) {
        interfaceCreateNewAccount.OnAfterNewAccountCreated(patient);
    }
}
