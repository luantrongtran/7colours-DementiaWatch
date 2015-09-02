package ifn372.sevencolors.dementiawatch.webservices;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ifn372.sevencolors.backend.carerApi.CarerApi;
import ifn372.sevencolors.backend.carerApi.model.Carer;
import ifn372.sevencolors.dementiawatch.Constants;

/**
 * Created by zach on 29/08/15.
 */
public class CheckService extends AsyncTask<String, Void, Carer> {
    //private ICheckPatientService iCheckP = null;

    //public CheckPatientService(ICheckPatientService iCheckP) { this.iCheckP = iCheckP; }

    @Override
    protected Carer doInBackground(String... id)
    {
        CarerApi.Builder apiBuilder = new CarerApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);
        apiBuilder.setRootUrl(Constants.webServiceUrl);
        //when running on google app engine disable zip function
        apiBuilder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });

        CarerApi carerEndpoint = apiBuilder.build();
        Carer carer = new Carer();
        carer.setId(Integer.parseInt(id[0]));
        try {
            carer = carerEndpoint.outOfBoundCheck(carer).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return carer;
    }
}
