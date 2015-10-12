package ifn372.sevencolors.watch_app.webservices;

import ifn372.sevencolors.backend.myApi.model.Carer;
import ifn372.sevencolors.backend.myApi.model.Patient;

/**
 * Created by lua on 8/10/2015.
 */
public interface ICreateNewAccountService {
    public void OnAfterNewAccountCreated(Patient patient);
}
