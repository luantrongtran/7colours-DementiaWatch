package ifn372.sevencolors.watch_app.webservices;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.ResultCode;
import ifn372.sevencolors.watch_app.BackendApiProvider;
import ifn372.sevencolors.watch_app.Constants;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.InvitationSharedPreferences;
import ifn372.sevencolors.watch_app.CustomSharedPreferences.UserInfoPreferences;

/**
 * Created by lua on 16/10/2015.
 */
public class AcceptInvitationService extends AsyncTask<Void, Void, ResultCode> {

    Context context;
    IAcceptInvitationService interfaceAcceptInvitation;

    public AcceptInvitationService(Context context,
                                   IAcceptInvitationService interfaceAcceptInvitation) {
        this.context = context;
        this.interfaceAcceptInvitation = interfaceAcceptInvitation;
    }

    @Override
    protected ResultCode doInBackground(Void... params) {
        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
        InvitationSharedPreferences invitationSharedPreferences
                = new InvitationSharedPreferences(context);

        int patientId = userInfoPreferences.getUserId();
        int carerId = invitationSharedPreferences.getCarerId();
        if(carerId == Constants.sharedPreferences_integer_default_value){
            return null;
        }


        MyApi myApi = BackendApiProvider.getPatientApiBuilder();
        ResultCode rc = new ResultCode();
        try {
            rc = myApi.acceptInvitation(patientId, carerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rc;
    }

    @Override
    protected void onPostExecute(ResultCode resultCode) {
        if(resultCode == null) {
            resultCode = new ResultCode();
        }
        interfaceAcceptInvitation.onInvitationAccepted(resultCode);
    }
}
