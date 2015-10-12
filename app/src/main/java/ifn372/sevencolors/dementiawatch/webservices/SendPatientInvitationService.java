package ifn372.sevencolors.dementiawatch.webservices;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import ifn372.sevencolors.backend.myApi.MyApi;
import ifn372.sevencolors.backend.myApi.model.ResultCode;
import ifn372.sevencolors.dementiawatch.BackendApiProvider;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.UserInfoPreferences;

/**
 * Created by lua on 12/10/2015.
 */
public class SendPatientInvitationService extends AsyncTask<String, Void, ResultCode> {

    ISendPatientInvitationService interfaceInvitation;
    Context context;

    public SendPatientInvitationService(Context context,
                                        ISendPatientInvitationService interfaceInvitation) {
        this.interfaceInvitation = interfaceInvitation;

        this.context = context;
    }

    /**
     * The first string in params is the patient user name who the sender want to invite
     * @param params
     * @return
     */
    @Override
    protected ResultCode doInBackground(String... params) {

        UserInfoPreferences userInfoPreferences = new UserInfoPreferences(context);
        int carerId = userInfoPreferences.getUserId();
        String username = params[0];

        MyApi myApi = BackendApiProvider.getPatientApi();

        try {
            ResultCode resultCode = myApi.sendPatientInvitation(carerId, username).execute();
            return resultCode;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(ResultCode resultCode) {
        if(resultCode == null) {
            return;
        }
        interfaceInvitation.onInvitationSent(resultCode);
    }
}
