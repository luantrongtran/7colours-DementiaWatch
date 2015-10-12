package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;

import ifn372.sevencolors.backend.Constants;
import ifn372.sevencolors.backend.dao.CarerDao;
import ifn372.sevencolors.backend.dao.PatientDao;
import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.entities.Patient;
import ifn372.sevencolors.backend.entities.ResultCode;
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
public class CarerEndpoint {
    @ApiMethod(name = "updateGcmId")
    public Carer updateGcmId(@Named("carerId") int carerId, @Named("regId") String regId) {
        Carer carer = new Carer();
        carer.setId(carerId);
        try {
            CarerDao carerDao = new CarerDao();
            regId = URLDecoder.decode(regId, "UTF-8");
            carerDao.updateGCMId(carer, regId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return carer;
    }

    /**
     * This method is invoked when a carer/relative send an invitation to a patient
     * @param carerId carerid could be a carer or a relative of the patient.
     * @param patientUsername the invited patient's username.
     * @return
     */
    @ApiMethod(name = "sendPatientInvitation")
    public ResultCode sendPatientInvitation(@Named("carerId") int carerId,
            @Named("patientUsername") String patientUsername) {
        ResultCode resultCode = new ResultCode();

        CarerDao carerDao = new CarerDao();
        Carer carer = carerDao.getCarerById(carerId);
        String carername = carer.getFullName();


        PatientDao patientDao = new PatientDao();
        Patient patient = patientDao.getPatientByUsername(patientUsername);
        String gcmId = patient.getGcmId();

        if(carer.getRole() == User.CARER_ROLE && patient.getCarer_id() > 0) {
            //If the sender is a carer.f the patient is already being taken care by
            //another carer then don't send the invitation.
            resultCode.setResult(false);

            if (patient.getCarer_id() == carer.getId()) {
                resultCode.setMessage("The patient is already in your list");
            } else {
                resultCode.setMessage("The patient has been assigned to another carer");
            }
            return resultCode;

        }

        String url = Constants.gcm_url;
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            //add request header
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "key=" + Constants.gcm_api_key);

            Gson gson = new Gson();
            Map<String, String> data = new HashMap<String, String>();
            data.put("title", "Invitation");
            data.put("message", carername + " sent you an invitation");
            Map<String, Object> finalData = new HashMap<String, Object>();
            finalData.put("data", data);
            finalData.put("to", gcmId);

            // Send post request
            con.setDoOutput(true);
            con.setDoInput(true);
            //con.connect();
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(gson.toJson(finalData).toString());
            //wr.writeBytes(URLEncoder.encode(urlParameters, "UTF-8"));
            wr.flush();
            wr.close();

            // for checking if send successfully
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

        } catch (Exception e) {
            e.printStackTrace();
        }

        resultCode.setResult(true);
        return resultCode;
    }
}
