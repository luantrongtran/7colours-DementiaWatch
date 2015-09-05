package ifn372.sevencolors.dementiawatch.webservices;

import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.PatientList;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;
import ifn372.sevencolors.dementiawatch.activities.MapsActivity;

public class UpdatePatientList extends AsyncTask<Integer, Void, PatientList> {
    @Override
    protected PatientList doInBackground(Integer... params) {
        MyApi myApiService = BackendApiProvider.getPatientApi();

        PatientList patientList = null;
        try {
            patientList = myApiService
                    .getPatientListByCarerOrRelative(params[0], params[1]).execute();
            MapsActivity.patientManager.setPatientList(patientList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    @Override
    protected void onPostExecute(PatientList patientList) {
        MapsActivity.mLeftMenuAdapter.notifyDataSetChanged();
    }
}
