package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.inject.Named;

import ifn372.sevencolors.backend.dao.FenceDao;
import ifn372.sevencolors.backend.dao.PatientDao;

import ifn372.sevencolors.backend.entities.Fence;
import ifn372.sevencolors.backend.entities.Location;
import ifn372.sevencolors.backend.entities.Patient;
import ifn372.sevencolors.backend.entities.PatientList;
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

    /**
     *
     * @param carerOrRelativeId
     * @param role indicating if the request sender is a carer or relative, see User class
     *             for more details about the role attribute
     * @return
     */
    @ApiMethod (name = "getPatientListByCarerOrRelative")
    public PatientList getPatientsListByCarerOrRelative(@Named("carerId") int carerOrRelativeId, @Named("role") int role) {
        if(role == User.CARER_ROLE) {
            Vector<Patient> patients = new Vector<Patient>();

            PatientDao patientDao = new PatientDao();
            patients = patientDao.getPatientsListByCarer(carerOrRelativeId);
            patientDao.getFencesForPatients(patients);

            PatientList patientList = new PatientList();
            patientList.setItems(patients);
            return patientList;
        } else if (role == User.RELATIVE_ROLE) {
            //return patient list related to the given relative.
        }

        return null;
    }

    /**
     * Created by Zachary
     * Receives outOfBound-checking request from carer's application
     * @param patient The patient object to be updated
     * @return patient Same patient object with updated safety property
     */
    @ApiMethod (name = "outOfBoundCheck", path = "outOfBoundCheck")
    public Patient outOfBoundCheck(Patient patient) {
        FenceDao fenceDao = new FenceDao();
        PatientDao patientDao = new PatientDao();
        boolean safety = true;
        int pId = patient.getId();
        Location location = patientDao.getPatientLocation(pId);
        double patientLat = location.getLat();
        double patientLon = location.getLon();
        List<Fence> fences = fenceDao.getFences(pId);
        for (Fence fence : fences) {
            double fenceLat = fence.getLat();
            double fenceLon = fence.getLon();
            float fenceRadius = fence.getRadius();
            Boolean inFence = ((patientLat - fenceLat) * (patientLat - fenceLat) + (patientLon - fenceLon) * (patientLon - fenceLon)) < (fenceRadius * fenceRadius);
            if (!inFence) {
                safety = false;
            }
        }
        patient.setSafety(safety);
        return patient;
    }
}