package ifn372.sevencolors.dementiawatch.webservices;

import android.content.Context;
import android.os.AsyncTask;

import java.util.logging.Logger;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;

/**
 * Created by Jhonatan Calle on 15/09/2015.
 */
public class UpdateFenceService extends AsyncTask<String, Void, Fence> {

    private IUpdateFenceService iufs = null;
    private Context context;

    public UpdateFenceService(IUpdateFenceService iufs) {
        this.iufs = iufs;
    }
    private static final Logger logger = Logger.getLogger(UpdateFenceService.class.getName());

    @Override
    protected Fence doInBackground(String... params) {

        logger.info("UpdateFenceService class doInBackground() method called");
        int fenceId = Integer.parseInt(params[0]);
        double lat = Double.parseDouble(params[1]);
        double lon = Double.parseDouble(params[2]);

        logger.info( "req params:" + "fenceId " + fenceId + ",lat " + lat + ",lon " + lon );

        MyApi fenceEndpoint = BackendApiProvider.getPatientApi();
        Fence myFence = null;
        try
        {
            myFence = fenceEndpoint.updateFenceById(fenceId, lat, lon).execute();
            logger.info("return value: " + myFence.getSuccess());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myFence;

    }

    @Override
    protected void onPostExecute(Fence myFence) {
        if (iufs != null) {
            iufs.processAfterUpdatingFence(myFence.getSuccess());
        }
    }
}