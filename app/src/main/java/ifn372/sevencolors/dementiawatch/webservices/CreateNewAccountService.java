package ifn372.sevencolors.dementiawatch.webservices;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Carer;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;

/**
 * Created by lua on 8/10/2015.
 */
public class CreateNewAccountService extends AsyncTask<Carer, Void, Carer> {

    ICreateNewAccountService interfaceCreateNewAccount;
    public CreateNewAccountService(ICreateNewAccountService interfaceCreateNewAccount)
    {
        this.interfaceCreateNewAccount = interfaceCreateNewAccount;
    }
    @Override
    protected Carer doInBackground(Carer... params) {
        Carer newCarer = params[0];

        MyApi myApi = BackendApiProvider.getPatientApi();
        try {
            newCarer = myApi.createCarer(newCarer).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newCarer;
    }

    @Override
    protected void onPostExecute(Carer carer) {
        interfaceCreateNewAccount.OnAfterNewAccountCreated(carer);
    }
}
