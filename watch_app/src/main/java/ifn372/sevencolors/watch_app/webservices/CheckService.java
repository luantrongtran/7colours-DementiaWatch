package ifn372.sevencolors.watch_app.webservices;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.watch_app.BackendApiProvider;

/**
 * Created by zach on 30/08/15.
 */
public class CheckService extends AsyncTask<String, Void, Patient> {
    //private ICheckPatientService iCheckP = null;

    //public CheckPatientService(ICheckPatientService iCheckP) { this.iCheckP = iCheckP; }

    @Override
    protected Patient doInBackground(String... id)
    {
        MyApi patientEndpoint = BackendApiProvider.getPatientApiBuilder();
        Patient patient = new Patient();
        patient.setId(Integer.parseInt(id[0]));
        try {
            patient = patientEndpoint.outOfBoundCheck(patient).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return patient;
    }
}
