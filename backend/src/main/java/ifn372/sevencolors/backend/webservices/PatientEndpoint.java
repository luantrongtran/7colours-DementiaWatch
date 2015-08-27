package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.Vector;
import java.util.logging.Logger;

import javax.inject.Named;

import ifn372.sevencolors.backend.dao.PatientDao;
import ifn372.sevencolors.backend.entities.Patient;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "patientApi",
        version = "v1",
        resource = "patient",
        namespace = @ApiNamespace(
                ownerDomain = "backend.sevencolors.ifn372",
                ownerName = "backend.sevencolors.ifn372",
                packagePath = ""
        )
)
public class PatientEndpoint {

    private static final Logger logger = Logger.getLogger(PatientEndpoint.class.getName());

    /**
     * Receives location-updating request from patients' watch application
     */
    @ApiMethod (name = "updatePatientCurrentLocation")
    public void updatePatientCurrentLocation(Patient patient) {
        PatientDao patientDao = new PatientDao();
        patientDao.updateCurrentLocation(patient);
    }

    @ApiMethod (name = "getPatientListByCarer")
    public Vector<Patient> getPatientsListByCarer(@Named("carerId") int carerId) {
        Vector<Patient> patientList = new Vector<Patient>();

        PatientDao patientDao = new PatientDao();
        patientList = patientDao.getPatientsListByCarer(carerId);

        return patientList;
    }
}