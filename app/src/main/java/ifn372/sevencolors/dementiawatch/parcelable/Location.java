package ifn372.sevencolors.dementiawatch.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import ifn372.sevencolors.backend.myApi.model.Patient;

/**
 * Created by lua on 28/08/2015.
 */
public class Location implements Parcelable{
//    Location
    @Override
    public int describeContents() {
        return 0;
    }

    public Location() {

    }

    public Location (Parcel in) {

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

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
