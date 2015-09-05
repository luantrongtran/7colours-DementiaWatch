package ifn372.sevencolors.backend.entities;

import java.util.Vector;

/**
 * Created by lua on 28/08/2015.
 */
public class PatientList {
    Vector<Patient> mPatientList;

    public PatientList(){
        mPatientList = new Vector<Patient>();
    }

    public void setItems(Vector<Patient> patientList) {
        mPatientList = patientList;
    }

    public Vector<Patient> getItems() {
        return mPatientList;
    }
}
