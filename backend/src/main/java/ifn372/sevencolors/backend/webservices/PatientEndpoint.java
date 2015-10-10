package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;

import javax.inject.Named;

import ifn372.sevencolors.backend.dao.FenceDao;
import ifn372.sevencolors.backend.dao.PatientDao;
import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.entities.Fence;
import ifn372.sevencolors.backend.entities.FenceList;
import ifn372.sevencolors.backend.entities.Location;
import ifn372.sevencolors.backend.entities.LocationList;
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

    /**
     * Created by Zachary Tang
     * Send an alert to the carer of the patient
     * @param patientId
     */
    @ApiMethod (name = "panicButton")
    public void panicButton(@Named("patientId") int patientId, @Named("panicStatus") String panicStatus) {
        PatientDao patientDao = new PatientDao();
        Carer carer = patientDao.getCarerByPatientId(patientId);
        String gcmId = carer.getGCMId(), patientName = patientDao.getPatientById(patientId).getFullName();
        String url = "https://gcm-http.googleapis.com/gcm/send";
        logger.info("panic button started");
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "key=AIzaSyCcuttbUWKQkGjBHhSwWcG8HL-PFOZdIsg");

            Gson gson = new Gson();
            Map<String, String> data = new HashMap<String, String>();
            data.put("title", panicStatus);
            data.put("message", patientName + " is asking for your help!");
            Map<String, Object> finalData = new HashMap<String, Object>();
            finalData.put("data", data);
            finalData.put("to", gcmId);

            //String urlParameters = "{ \"data\": {\"title\": \"InPanic\",\"message\": \"testing\"},\"to\" : \"fC5r8Ch6ozw:APA91bFUd0PdzES6KkdMLPT1BLwex9MaNKCMdI_ANP6jNr6SdmUomkgphAgUgBPfCCMkS9kCfVp1-JhStLm_mPZzfdGtqH985iLLYMH1QNzObM70PHUmqqMJCZPD7BHH5RgAIKV7AdTV\"}";

            // Send post request
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(gson.toJson(finalData).toString());
            //wr.writeBytes(URLEncoder.encode(urlParameters, "UTF-8"));
            wr.flush();
            wr.close();

            logger.info(gson.toJson(finalData).toString());
            logger.info(String.valueOf(con.getResponseCode()));
            logger.info("Panic button ended");

            /* for checking if send successfully
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiMethod(name = "getFenceByPatientId")
    public FenceList getFenceByPatientId(@Named("patientId") int patientId){
        FenceList fenceList = new FenceList();

        FenceDao fenceDao = new FenceDao();
        List<Fence> lstFence = fenceDao.getFences(patientId);
        fenceList.setItems(lstFence);

        return fenceList;
    }


    /**
     * Created by Koji Nishimoto
     * Get location history of a user
     * @param patientId Patient ID
     * @return TreeMap object that contains locations of the patient. The return value is sorted by time asc (Older location first).
     */
    @ApiMethod (name = "getLocationHistory")
    public TreeMap<String, Location> getLocationHistory(@Named("patientId") int patientId)
    {
        logger.info("getLocationHistory() method started");
        PatientDao dao = new PatientDao();
        TreeMap history = dao.getLocationHistory(patientId);
        logger.info("getLocationHistory() method end");
        return history;
    }
}