package ifn372.sevencolors.dementiawatch.webservices;

import ifn372.sevencolors.backend.myApi.model.Carer;

/**
 * Created by lua on 8/10/2015.
 */
public interface ICreateNewAccountService {
    public void OnAfterNewAccountCreated(Carer carer);
}
