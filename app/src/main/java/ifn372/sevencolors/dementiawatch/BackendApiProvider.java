package ifn372.sevencolors.dementiawatch;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
/**
 * Created by lua on 27/08/2015.
 */
public class BackendApiProvider {
    public static MyApi getPatientApi() {
        MyApi.Builder myApi = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);

        myApi.setRootUrl(Constants.webServiceUrl);

        //when running on google app engine disable zip function
        myApi.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
            @Override
            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                abstractGoogleClientRequest.setDisableGZipContent(true);
            }
        });

        return myApi.build();
    }
}
