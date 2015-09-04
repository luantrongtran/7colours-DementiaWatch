package ifn372.sevencolors.dementiawatch.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import ifn372.sevencolors.backend.myApi.model.Location;
import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.PatientList;

/**
 * Created by lua on 28/08/2015.
 */
public class PatientListParcelable implements Parcelable {

    private PatientList patientList;
    private PatientParcelable[] patientParcelableList;

    public PatientParcelable[] getPatientParcelableList() {
        return patientParcelableList;
    }

    public void setPatientParcelableList(PatientParcelable[] patientParcelableList) {
        this.patientParcelableList = patientParcelableList;
    }

    public PatientListParcelable(PatientList patientList) {
        this.patientList = patientList;
        patientParcelableList = new PatientParcelable[patientList.getItems().size()];
        int k = 0;
        for (Patient patient : patientList.getItems()) {
            patientParcelableList[k] = new PatientParcelable(patient);
            k++;
        }
    }

    public PatientListParcelable(Parcel in) {
        in.readTypedArray(patientParcelableList, CREATOR);

        Vector<Patient> arrPatients = new Vector<Patient>();

        for(int i = 0; i < patientParcelableList.length; i++){
            Patient patient = patientParcelableList[i].getPatient();
            arrPatients.add(patient);
        }
        PatientList patientList = new PatientList();
        patientList.setItems(arrPatients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(patientParcelableList, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PatientListParcelable createFromParcel(Parcel in) {
            PatientListParcelable lp = new PatientListParcelable(in);
            return lp;
        }

        public PatientListParcelable[] newArray(int size) {
            return new PatientListParcelable[size];
        }
    };

    public PatientList getPatientList() {
        return patientList;
    }

    public void setPatientList(PatientList patientList) {
        this.patientList = patientList;
    }
}