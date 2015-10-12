package ifn372.sevencolors.dementiawatch.webservices;

import ifn372.sevencolors.backend.myApi.model.ResultCode;

/**
 * Created by lua on 12/10/2015.
 */
public interface ISendPatientInvitationService {
    public void onInvitationSent(ResultCode resultCode);
}
