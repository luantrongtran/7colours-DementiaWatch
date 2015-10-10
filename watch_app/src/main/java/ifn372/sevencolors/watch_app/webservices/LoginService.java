package ifn372.sevencolors.watch_app.webservices;

import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Carer;
import ifn372.sevencolors.watch_app.BackendApiProvider;

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

        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        try {
            carer = myApi.authenticateCarer(carer).execute();
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
