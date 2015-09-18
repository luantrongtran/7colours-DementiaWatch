package ifn372.sevencolors.dementiawatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.Location;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.PatientList;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.dementiawatch.webservices.DeleteFenceService;
import ifn372.sevencolors.dementiawatch.webservices.FenceService;
import ifn372.sevencolors.dementiawatch.webservices.UpdateFenceService;

/**
 * Created by lua on 28/08/2015.
 */
public class PatientManager {

    public static int[] patientColors = {0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xCCCCCC};
    public static int[] fenceColors = {0x4DFF0000, 0x4D00FF00, 0x4D0000FF, 0x4DFFFF00, 0x4DCCCCCC};
    public static float temporaryFenceRadius = 50;
    public static int createTemporaryFenceTimeOut = 10000;

    private PatientList patientList;

    public void setPatientMarkers(Vector<Marker> patientMarkers) {
        this.patientMarkers = patientMarkers;
    }

    Vector<Marker> patientMarkers ;

    Vector<Circle> patientFences;

    /**
     * patients picked up with the carer/family member
     * Key: patient id, Value: temporary fence id
     */
    Map<Integer, Integer> pickedUpPatients;

    public PatientManager() {
        patientList = new PatientList();
        patientList.setItems(new Vector<Patient>());
        patientMarkers = new Vector<Marker>();
        patientFences = new Vector<>();
        pickedUpPatients = new HashMap<>();
    }

    public PatientList getPatientList() {
        return patientList;
    }

    public void setPatientList(PatientList patientList) {
        this.patientList = patientList;
    }

    public Vector<Marker> getPatientMarkers() {
        return patientMarkers;
    }

    public Patient getPatientById(int id) {
        for(Patient p : patientList.getItems()) {
            if(p.getId() == id) {
                return p;
            }
        }

        return null;
    }

    public void updatePatientsMarkerOnMap(GoogleMap gMap, Context context) {
        Log.i(Constants.application_id, "Update map according to patient list");
        gMap.clear();
//        for(Marker marker : patientMarkers) {
//            marker.remove();
//        }
        patientMarkers.clear();

        List<Patient> patients = patientList.getItems();
        MarkerOptions markerOptions = new MarkerOptions();
        Drawable drawable  = context.getResources()
                .getDrawable(R.drawable.ic_room_black_24dp);


        for(int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            Location loc = p.getCurrentLocation();
            LatLng location = new LatLng(loc.getLat(), loc.getLon());

            markerOptions.position(location);
            markerOptions.title(p.getFullName());
            Bitmap b = BitMapUtils.getMutableBitmapFromResourceFromResource(drawable, patientColors[i]);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
            Marker marker = gMap.addMarker(markerOptions);

            patientMarkers.add(marker);
        }

        updatePatientFenceOnMap(gMap);
    }

    public void updatePatientFenceOnMap(GoogleMap gMap){
        patientFences.clear();

        for(int j = 0; j < patientList.getItems().size() ; j++) {
            Patient patient = patientList.getItems().get(j);
            if(patient.getFenceList() == null){
                continue;
            }
            List<Fence> fences = patient.getFenceList().getItems();
            if(fences == null) {
                continue;
            }

            for(int i = 0; i < fences.size() ; i++) {
                Fence fence = fences.get(i);
                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(fence.getLat(), fence.getLon()))
                        .strokeColor(patientColors[j])
                        .fillColor(fenceColors[j])//(patientColors[j])
                        .radius(fence.getRadius());

                patientFences.add(gMap.addCircle(circleOptions));
            }
        }
    }

    /**
     * Enable a patient so that he/she can be outside with a carer/family member.
     * @param patientId
     * @return true if enable successfully, on contrary, return false
     */
    public boolean enablePickedUpMode(int patientId, Context context) {
        Patient patient = getPatientById(patientId);
        if(patient == null) {
            return false;
        }
        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);

        //Create new temporary fence
        FenceService fenceService = new FenceService(null);
        Fence temporaryFence = null;
        try {
            temporaryFence = fenceService.execute(
                    Integer.toString(patient.getId()),
                    "",     // Fence Name
                    Double.toString(currentLocationPreferences.getLat()),
                    Double.toString(currentLocationPreferences.getLon()),
                    Float.toString(temporaryFenceRadius),
                    "Temporary Fence").get(createTemporaryFenceTimeOut, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        if(temporaryFence == null){
            return false;
        }

        //Store the temporary fence corresponding to patient id
        pickedUpPatients.put(patientId, temporaryFence.getFenceId());

        return true;
    }

    public boolean disablePickedUpMode(int patientId) {
        Patient patient = getPatientById(patientId);
        if(patient == null) {
            return false;
        }

        Integer fenceId = pickedUpPatients.get(patientId);
        if(fenceId == null) {
            //if couldn't find the temporary fence id, assume that the temporary fence of
            // specified patient was already disabled
            return true;
        }

        DeleteFenceService deleteFenceService = new DeleteFenceService(null);
        Fence deletedFence = null;
        try {
            deletedFence = deleteFenceService.execute(fenceId.toString())
                    .get(createTemporaryFenceTimeOut, TimeUnit.MILLISECONDS);
        }  catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        pickedUpPatients.remove(patientId);

        return true;
    }

    /**
     * Temporary fences are fences surrounded the current location of the carer/family member and
     * will be updated based on the current location of the carer/family member
     */
    public void updateTemporaryFence(Context context) {
        if(pickedUpPatients == null || pickedUpPatients.size() == 0) {
            return;
        }

        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);
        UpdateFenceService updateFenceService = new UpdateFenceService(null);

        for (Map.Entry<Integer, Integer> entry : pickedUpPatients.entrySet())
        {
            int fenceId = entry.getValue().intValue();
            updateFenceService.execute(Integer.toString(fenceId),
                    Double.toString(currentLocationPreferences.getLat()),
                    Double.toString(currentLocationPreferences.getLon()));
        }
    }
}