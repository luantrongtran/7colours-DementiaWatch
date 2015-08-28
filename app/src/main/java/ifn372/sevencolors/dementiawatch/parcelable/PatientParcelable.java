package ifn372.sevencolors.dementiawatch.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.json.GenericJson;

import ifn372.sevencolors.backend.myApi.model.Patient;

public class PatientParcelable implements Parcelable {

    Patient patient;
    public PatientParcelable(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                patient.getId().toString(),
                patient.getFullName(),
                patient.getCarerId().toString(),
                patient.getRole().toString(),
                patient.getLocationLastUpdate().toString(),
                patient.getCurrentLocation().getLat().toString(),
                patient.getCurrentLocation().getLon().toString()
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Patient createFromParcel(Parcel in) {
            return new Patient();
        }

        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };
}
