package ifn372.sevencolors.dementiawatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.JsonMap;
import ifn372.sevencolors.backend.myApi.model.Location;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.PatientList;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.CurrentLocationPreferences;
import ifn372.sevencolors.dementiawatch.CustomSharedPreferences.TemporaryFenceSharedPreferences;
import ifn372.sevencolors.dementiawatch.webservices.DeleteFenceService;
import ifn372.sevencolors.dementiawatch.webservices.FenceService;
import ifn372.sevencolors.dementiawatch.webservices.LocationHistoryService;

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

    Circle temporaryFence;
    CircleOptions temporaryFenceOptions;

    /**
     * patients picked up with the carer/family member
     * Key: patient id, Value: temporary fence id
     */
    Map<Integer, Integer> pickedUpPatients;

    /**
     * because the information window of a marker will be removed after each refresh. Store
     * the patient id of patient to show the info window of corresponding marker of that patient
     * after refreshing the map.
     */
    int patientIdShowingInfoWindow = -1;

    Vector<Marker> mLocationHistoryMarkers ;

    public JsonMap mLocationHistory;

//    public boolean mIsShowingLocHis;

    Map<Integer, Boolean> mShowingLocHisPatients;

    public PatientManager() {
        patientList = new PatientList();
        patientList.setItems(new Vector<Patient>());
        patientMarkers = new Vector<Marker>();
        patientFences = new Vector<>();
        pickedUpPatients = new HashMap<>();

        mLocationHistoryMarkers = new Vector<Marker>();
        mShowingLocHisPatients = new HashMap<>();

        temporaryFenceOptions = new CircleOptions()
                .fillColor(0x4D000000)
                .strokeColor(0x4D000000)
                .radius(50);
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

    /**
     * Get the index position of the given patient id within patientList
     * @param id
     * @return -1 if couldn't find the given patient id, return the index if found.
     */
    public int getPatientIndexById(int id) {
        for(int i = 0; i < patientList.getItems().size(); i++) {
            Patient p = patientList.getItems().get(i);
            if(p.getId() == id) {
                return i;
            }
        }

        return -1;
    }

    public void updatePatientsMarkerOnMap(GoogleMap gMap, Context context) {
        Log.i(Constants.application_id, "Update map according to patient list");
//        gMap.clear();
        patientIdShowingInfoWindow = -1;
        for (int i = 0; i < patientMarkers.size(); i++) {
            Marker marker = patientMarkers.get(i);
            if (marker.isInfoWindowShown()) {
                patientIdShowingInfoWindow = patientList.getItems().get(i).getId();
            }
            marker.remove();
        }
        patientMarkers.clear();

        List<Patient> patients = patientList.getItems();
        MarkerOptions markerOptions = new MarkerOptions();
        Drawable drawable = context.getResources()
                .getDrawable(R.drawable.ic_room_black_24dp);


        for (int i = 0; i < patients.size(); i++) {
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

        updatePatientFenceOnMap(gMap, context);

        //Show the info window after refreshing the map
        // if there was a marker showing the info window

        int index = getPatientIndexById(patientIdShowingInfoWindow);
        if (index != -1) {
            patientMarkers.get(index).showInfoWindow();
        }
    }
    public void updatePatientFenceOnMap(GoogleMap gMap, Context context){
        for(Circle c : patientFences) {
            c.remove();
        }
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


            Integer temporaryFenceId = pickedUpPatients.get(patient.getId());
            Log.e(Constants.application_id, "Temporary fence id: " + temporaryFenceId);
            for(int i = 0; i < fences.size() ; i++) {
                Fence fence = fences.get(i);
                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(fence.getLat(), fence.getLon()))
                        .strokeColor(patientColors[j])
                        .fillColor(fenceColors[j])//(patientColors[j])
                        .radius(fence.getRadius());

                if(temporaryFenceId != null){
                    boolean b = temporaryFenceId.intValue() == fence.getFenceId();
                    Log.e(Constants.application_id, fence.getFenceId() + "="
                            + temporaryFenceId.intValue() + ": " + b);
                    if(b) {
                        //If the fence id is the temporary fence id created by the current user
                        if(temporaryFence != null) {
                            patientFences.add(temporaryFence);
                        }
                        continue;
                    }
                }
                Circle circle = gMap.addCircle(circleOptions);
                patientFences.add(circle);
            }
        }

        if(temporaryFence != null) {
            drawTemporaryFence(gMap, context);
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
                    "temporary_fence",     // Fence Name
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
        if(!pickedUpPatients.containsKey(patientId)) {
            pickedUpPatients.put(patientId, temporaryFence.getFenceId());

            //add temporary fence into SharedPreferences
            TemporaryFenceSharedPreferences temporaryFenceSharedPreferences
                    = new TemporaryFenceSharedPreferences(context);
            temporaryFenceSharedPreferences.addANewTemporaryFence(patientId,
                    temporaryFence.getFenceId());
        }

        return true;
    }

    public boolean disablePickedUpMode(int patientId, Context context) {
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
        //Remove in SharedPreferences
        TemporaryFenceSharedPreferences temporaryFenceSharedPreferences
                = new TemporaryFenceSharedPreferences(context);
        temporaryFenceSharedPreferences.removeTemporaryFence(patientId);

        if(temporaryFence != null) {
            temporaryFence.remove();
        }
        //temporaryFence should be null after disabling the picked up mode
        temporaryFence = null;

        return true;
    }

    /**
     * Temporary fences are fences surrounded the current location of the carer/family member and
     * will be updated based on the current location of the carer/family member.
     * This function updates the fences' position only
     */
    public void updateTemporaryFence(GoogleMap gMap, Context context) {
        if(pickedUpPatients == null || pickedUpPatients.size() == 0) {
            return;
        }

        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);
        final LatLng curLocation = new LatLng(currentLocationPreferences.getLat(),
                currentLocationPreferences.getLon());

        drawTemporaryFence(gMap, context);

        //Update temporary fence to server
//        UpdateFenceService updateFenceService = new UpdateFenceService(null);
//        for (Map.Entry<Integer, Integer> entry : pickedUpPatients.entrySet())
//        {
//            int fenceId = entry.getValue().intValue();
//            updateFenceService.execute(Integer.toString(fenceId),
//                    Double.toString(curLocation.latitude),
//                    Double.toString(curLocation.longitude));
//        }
    }

    public void drawTemporaryFence(GoogleMap gMap, Context context) {
        //Update on map
        CurrentLocationPreferences currentLocationPreferences
                = new CurrentLocationPreferences(context);
        final LatLng curLocation = new LatLng(currentLocationPreferences.getLat(),
                currentLocationPreferences.getLon());

        if(temporaryFence != null) {
            temporaryFence.remove();
        }
        temporaryFenceOptions = temporaryFenceOptions.center(curLocation);
        temporaryFence = gMap.addCircle(temporaryFenceOptions);
    }

    /**
     * Check if the given patient is enabled for picked up mode
     * @param patientId the patient Id
     * @return true if the picked up mode is enabled
     */
    public boolean isPickedUpModeEnabled(int patientId) {
        return pickedUpPatients.containsKey(patientId);
    }

    public void disableAllTemporaryFences(Context context) {
        for (Map.Entry<Integer, Integer> entry : pickedUpPatients.entrySet()){
            int patientId = entry.getKey();
            disablePickedUpMode(patientId, context);
        }
    }

    public void updateLocationHistory(GoogleMap gMap, Context context)
    {
        Log.i(Constants.application_id, "Update map with patient location history");
//        gMap.clear();
        patientIdShowingInfoWindow = -1;

        if ( mLocationHistoryMarkers != null )
        {
            for (int i = 0; i < mLocationHistoryMarkers.size(); i++) {
                Marker marker = mLocationHistoryMarkers.get(i);
                if (marker.isInfoWindowShown()) {
                    patientIdShowingInfoWindow = patientList.getItems().get(i).getId();
                }
                marker.remove();
            }
        }

        mLocationHistoryMarkers.clear();

        List<Patient> patients = patientList.getItems();
        MarkerOptions markerOptions = new MarkerOptions();
        Drawable drawable = context.getResources()
                .getDrawable(R.drawable.ic_room_black_24dp);

        for (int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            if( !isShowingLocHis(p.getId()) )
            {
                continue;
            }

            for (Map.Entry<String, Object> entry : mLocationHistory.entrySet()) {
//                Patient p = patients.get(i);
//                Location loc = p.getCurrentLocation();
                String sdf = entry.getKey();
                Location loc = (Location) entry.getValue();
                LatLng location = new LatLng(loc.getLat(), loc.getLon());

                markerOptions.position(location);
                markerOptions.title(p.getFullName());
//                markerOptions.title(sdf);
                Bitmap b = BitMapUtils.getMutableBitmapFromResourceFromResource(drawable, patientColors[1]);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(b));
                Marker marker = gMap.addMarker(markerOptions);

                mLocationHistoryMarkers.add(marker);
            }
        }

        // Show the info window after refreshing the map
        // if there was a marker showing the info window

        int index = getPatientIndexById(patientIdShowingInfoWindow);
        if (index != -1) {
            mLocationHistoryMarkers.get(index).showInfoWindow();
        }
    }

    public boolean enableLocationHistory(int patientId, Context context) {
        Patient patient = getPatientById(patientId);
        if(patient == null) {
            return false;
        }

        //Create new location history
        LocationHistoryService locationHistoryService = new LocationHistoryService(null);
        AsyncTask<String, Void, JsonMap> locHis = null;     // This should be just JsonMap!!!!
        try {
            locHis = locationHistoryService.execute(
                    Integer.toString(patient.getId()) );    // the execute won't return the Map!!!
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(locHis == null){
            return false;
        }

//        mLocationHistory = locHis;

        if(!mShowingLocHisPatients.containsKey(patientId)) {
            mShowingLocHisPatients.put(patientId, true);
        }

        return true;
    }

    public boolean disableLocationHistory(int patientId, Context context) {
        Patient patient = getPatientById(patientId);
        if(patient == null) {
            return false;
        }

        boolean isShowing = mShowingLocHisPatients.get(patientId);
        if(!isShowing) {
            //if couldn't find the value for this patient, assume that the location history of
            // specified patient was already disabled
            return true;
        }

        mShowingLocHisPatients.remove(patientId);

        //mLocationHistory should be null after disabling the picked up mode
        mLocationHistory = null;

        return true;
    }

    public boolean isShowingLocHis(int patientId) {
        return mShowingLocHisPatients.containsKey(patientId);
    }
}