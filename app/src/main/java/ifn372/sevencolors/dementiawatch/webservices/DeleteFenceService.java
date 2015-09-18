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
public class DeleteFenceService extends AsyncTask <String, Void, Fence> {

    private IDeleteFenceService idfs = null;
    private Context context;

    public DeleteFenceService(IDeleteFenceService idfs) {
        this.idfs = idfs;
    }
    private static final Logger logger = Logger.getLogger(DeleteFenceService.class.getName());

    @Override
    protected Fence doInBackground(String... params) {

        logger.info("DeleteFenceService class doInBackground() method called");
        int fenceId = Integer.parseInt(params[0]);
//        double lat = Double.parseDouble(params[1]);
//        double lon = Double.parseDouble(params[2]);

        logger.info( "req params:" + "fenceId " + fenceId );

        MyApi fenceEndpoint = BackendApiProvider.getPatientApi();
        Fence myFence = null;
        try
        {
            myFence = fenceEndpoint.deleteFenceById(fenceId).execute();
            logger.info("return value: " + myFence.getSuccess());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myFence;

    }

    @Override
    protected void onPostExecute(Fence myFence) {
        if(idfs != null) {
            idfs.processAfterDeletingFence(myFence.getSuccess());
        }
    }

}
