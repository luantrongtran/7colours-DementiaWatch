package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

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
    public static final int CODE_ERR_UPDATE_USER_FAILED = -2;
    public static final int CODE_ERR_DELETE_USER_FAILED = -3;


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

    @ApiMethod(name = "updatePatient")
    public Patient updatePatient(Patient patient)
    {
        return new UserDao().updatePatient(patient);
    }

    @ApiMethod(name = "updateCarer")
    public Carer updateCarer(Carer carer)
    {
        if (carer.getPatientIds() == null || carer.getPatientIds().size() == 0)
        {
            return null;
        }
        return new UserDao().updateCarer(carer);
    }

    @ApiMethod(name = "deleteUser")
    public User deleteUser(@Named("useId")int userId)
    {
        User user = new User();
        user.setId(new UserDao().deleteUser(userId));
        return user;
    }

}
