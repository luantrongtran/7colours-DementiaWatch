package ifn372.sevencolors.watch_app.webservices;

import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.watch_app.BackendApiProvider;

/**
 * Created by lua on 13/10/2015.
 */
public class UpdateAssignedCarerService extends AsyncTask<Integer, Void, Patient> {

    /**
     * The first element in params is the carerId and the second one is the patientId.
     * @param params
     * @return
     */
    @Override
    protected Patient doInBackground(Integer... params) {
        int carerId = params[0].intValue();
        int patientId = params[1].intValue();

        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setCarerId(carerId);
        try {
            myApi.updatePatient(patient);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
