package ifn372.sevencolors.dementiawatch.webservices;

import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Carer;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;

/**
 * Created by lua on 8/10/2015.
 */
public class LoginService extends AsyncTask<Carer, Void, Carer> {
    ILoginService interfaceLoginService;
    public LoginService(ILoginService interfaceLoginService){
        this.interfaceLoginService = interfaceLoginService;
    }
    @Override
    protected Carer doInBackground(Carer... params) {
        Carer carer = params[0];

        MyApi myApi = BackendApiProvider.getPatientApi();
        try {
            carer = myApi.authenticateUser(carer).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return carer;
    }

    @Override
    protected void onPostExecute(Carer carer) {
        interfaceLoginService.OnLoginServiceFinished(carer);
    }
}
