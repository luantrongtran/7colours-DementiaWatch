package ifn372.sevencolors.dementiawatch;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Vector;

import ifn372.sevencolors.backend.myApi.model.Location;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.PatientList;

/**
 * Created by lua on 28/08/2015.
 */
public class PatientManager {

    private PatientList patientList;

    Vector<Marker> patientMarkers ;

    public PatientManager() {
        patientList = new PatientList();
        patientList.setItems(new Vector<Patient>());
        patientMarkers = new Vector<Marker>();
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

    public void updatePatientsMarkerOnMap(GoogleMap gMap) {
        Log.i(Constants.application_id, "Update map according to patient list");
//        gMap.clear();
        for(Marker marker : patientMarkers) {
            marker.remove();
        }
        patientMarkers.clear();

        List<Patient> patients = patientList.getItems();
        MarkerOptions markerOptions = new MarkerOptions();

        for(int i = 0; i < patients.size(); i++) {
            Patient p = patients.get(i);
            Location loc = p.getCurrentLocation();
            LatLng location = new LatLng(loc.getLat(), loc.getLon());

            markerOptions.position(location);
            markerOptions.title(p.getFullName());
            Marker marker = gMap.addMarker(markerOptions);

            patientMarkers.add(marker);
        }
    }

    public void updatePatientFenceOnMap(GoogleMap gMap){

    }
}
