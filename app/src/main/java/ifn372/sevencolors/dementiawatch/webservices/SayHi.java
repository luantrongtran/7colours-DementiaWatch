package ifn372.sevencolors.dementiawatch.webservices;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;

/**
 * Created by lua on 2/08/2015.
 */
public class SayHi extends MyWebService {

    private ISayHi sayHi = null;

    public SayHi(ISayHi sh) {
        sayHi = sh;
    }

    @Override
    protected String doInBackground(String... params) {
        MyApi.Builder myApi = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                new AndroidJsonFactory(), null);

        myApi.setRootUrl(getWebservice_url());
        //when running on google app engine disable zip function
//        myApi.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//            @Override
//            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                abstractGoogleClientRequest.setDisableGZipContent(true);
//            }
//        });
        String name = params[0];
        System.out.println(getWebservice_url());
        System.out.println("Name: " + name);

        MyApi myApiService = myApi.build();

        try {
            String str = myApiService.sayHi(name).execute().getData();
            System.out.println("return string: " + str) ;
            return str;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return e.getMessage();
        }

    }

    @Override
    protected void onPostExecute(String s) {
        sayHi.processData(s);
    }
}
