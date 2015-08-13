package ifn372.sevencolors.dementiawatch.webservices;

import android.os.AsyncTask;

/**
 * Created by lua on 7/08/2015.
 */
public class MyWebService extends AsyncTask<String, Void, String> {
    private String webservice_url = "https://dementiawatch-7colors.appspot.com//_ah/api/";
    private String webservice_localhost_url = "http://192.168.0.110:8080";

    @Override
    protected String doInBackground(String... params) {
        return null;
    }

    public String getWebservice_url() {
        return webservice_url;
    }

    public void setWebservice_url(String webservice_url) {
        this.webservice_url = webservice_url;
    }
}