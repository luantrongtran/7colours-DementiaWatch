package ifn372.sevencolors.dementiawatch.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.api.client.json.GenericJson;

import ifn372.sevencolors.backend.myApi.model.Location;
import ifn372.sevencolors.backend.myApi.model.Patient;

public class PatientParcelable implements Parcelable {

    Patient patient;
    public PatientParcelable(Patient patient) {
        this.patient = patient;
    }

    public PatientParcelable (Parcel in) {
        patient.setId(in.readInt());
        patient.setFullName(in.readString());
        patient.setCarerId(in.readInt());
        patient.setRole(in.readInt());
        patient.setLocationLastUpdate(in.readLong());

        Location loc = in.readParcelable(Location.class.getClassLoader());
        patient.setCurrentLocation(loc);
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
        dest.writeInt(patient.getId());
        dest.writeString(patient.getFullName());
        dest.writeInt(patient.getCarerId());
        dest.writeInt(patient.getRole());
        dest.writeLong(patient.getLocationLastUpdate());
        dest.writeParcelable(new LocationParcelable(patient.getCurrentLocation()), flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public PatientParcelable createFromParcel(Parcel in) {
            return new PatientParcelable(in);
        }

        public PatientParcelable[] newArray(int size) {
            return new PatientParcelable[size];
        }
    };
}
