package ifn372.sevencolors.dementiawatch.webservices;

import android.content.Context;
import android.os.AsyncTask;

import java.util.logging.Logger;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.JsonMap;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;


/**
 * Created by Jhonatan Calle on 08/10/2015.
 */
public class LocationHistoryService extends AsyncTask<String, Void, JsonMap> {

    private ILocationHistoryService ilhs = null;
    private Context context;

    public LocationHistoryService(ILocationHistoryService ilhs) {
        this.ilhs = ilhs;
    }
    private static final Logger logger = Logger.getLogger(LocationHistoryService.class.getName());

    @Override
    protected JsonMap doInBackground(String... params) {

        logger.info("LocationHistoryService class doInBackground() method called");
        int patientId = Integer.parseInt(params[0]);

        logger.info("req params:" + "patientId");

        MyApi patientEndpoint = BackendApiProvider.getPatientApi();
        JsonMap myLocHis = null;
        try
        {
            myLocHis = patientEndpoint.getLocationHistory(patientId).execute();
            logger.info("location map size: " + myLocHis.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myLocHis;
    }

    @Override
    protected void onPostExecute(JsonMap myLocHis) {
        if (ilhs != null) {
            ilhs.processAfterLocationHistory(myLocHis.size());
        }
    }
}
