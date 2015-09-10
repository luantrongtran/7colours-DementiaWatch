package ifn372.sevencolors.dementiawatch.webservices;

import android.content.Context;
import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.logging.Logger;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;
import ifn372.sevencolors.dementiawatch.Constants;

/**
 * Created by koji on 2015/08/27.
 */
public class FenceService extends AsyncTask<String, Void, Fence> {
    private IFenceService ifs = null;
    private Context context;

    public FenceService(IFenceService ifs) {
        this.ifs = ifs;
    }
    private static final Logger logger = Logger.getLogger(FenceService.class.getName());

    @Override
    protected Fence doInBackground(String... params)
    {
        logger.info("CreateFence class doInBackground() method called");
        String userId = params[0];
        String fenceName = params[1];
        double lat = Double.parseDouble(params[2]);
        double lon = Double.parseDouble(params[3]);
        float radius = Float.parseFloat(params[4]);
        String address = params[5];

        logger.info("req params:" + userId + ", " + fenceName + ", " + lat + ", " + lon + ", " + radius + ", " + address);

        MyApi fenceEndpoint = BackendApiProvider.getPatientApi();
        Fence info = null;
        try
        {
            info = fenceEndpoint.createFence(Integer.valueOf(userId), fenceName, lat, lon, radius, address).execute();
            logger.info("return value: " + info.getSuccess());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    @Override
    protected void onPostExecute(Fence info) {
//        super.onPostExecute(myBean);
        //System.out.println("Bean: " + myBean);
        ifs.processAfterCreatingFence(info.getSuccess());
    }
}
