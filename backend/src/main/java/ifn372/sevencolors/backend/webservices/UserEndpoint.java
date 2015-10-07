package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import ifn372.sevencolors.backend.dao.UserDao;
import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.entities.Patient;
import ifn372.sevencolors.backend.entities.User;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.sevencolors.ifn372",
                ownerName = "backend.sevencolors.ifn372",
                packagePath = ""
        )
)
public class UserEndpoint {
    public static int CODE_ERR_CREATE_USER_FAILED = -1;

    @ApiMethod(name = "createCarer")
    public Carer createCarer(Carer carer) {
        UserDao userDao = new UserDao();
        if (carer.getPatientIds() == null) {
            return null;
        }
        carer = userDao.createCarer(carer);
        return carer;
    }

    @ApiMethod(name = "createPatient")
    public Patient createPatient(Patient patient) {
        UserDao userDao = new UserDao();
        patient = userDao.createPatient(patient);
        return patient;
    }

    @ApiMethod(name = "authenticateUser")
    public Carer authenticateUser(Carer carer) {
        UserDao userDao = new UserDao();
        return userDao.carerAuthentication(carer.getUserName(), carer.getPassword());
    }
}
