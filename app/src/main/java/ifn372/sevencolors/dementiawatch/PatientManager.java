package ifn372.sevencolors.dementiawatch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Vector;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.Location;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.PatientList;

/**
 * Created by lua on 28/08/2015.
 */
public class PatientManager {

    private PatientList patientList;

    public void setPatientMarkers(Vector<Marker> patientMarkers) {
        this.patientMarkers = patientMarkers;
    }

    Vector<Marker> patientMarkers ;

    Vector<Circle> patientFences;

    public static int[] patientColors = {0xFF0000, 0x00FF00, 0x0000FF, 0xFFFF00, 0xCCCCCC};
    public static int[] fenceColors = {0x4DFF0000, 0x4D00FF00, 0x4D0000FF, 0x4DFFFF00, 0x4DCCCCCC};;

    public PatientManager() {
        patientList = new PatientList();
        patientList.setItems(new Vector<Patient>());
        patientMarkers = new Vector<Marker>();
        patientFences = new Vector<>();
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
}