package ifn372.sevencolors.backend.entities;

import java.util.List;
import java.util.Vector;

/**
 * Created by lua on 28/08/2015.
 */
public class PatientList {
    List<Patient> patientList;

    public PatientList(){
        patientList = new Vector<>();
    }

    public void setItems(Vector<Patient> patientList) {
        this.patientList = patientList;
    }

    public List<Patient> getItems() {
        return patientList;
    }
}
