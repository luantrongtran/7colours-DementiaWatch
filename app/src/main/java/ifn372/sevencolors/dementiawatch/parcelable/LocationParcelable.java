package ifn372.sevencolors.dementiawatch.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import ifn372.sevencolors.backend.myApi.model.Patient;
import ifn372.sevencolors.backend.myApi.model.Location;
/**
 * Created by lua on 28/08/2015.
 */
public class LocationParcelable implements Parcelable{
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    Location location;

    @Override
    public int describeContents() {
        return 0;
    }

    public LocationParcelable(Location loc) {
        this.location = loc;
    }

    public LocationParcelable(Parcel in) {
        location = new Location();
        location.setLat(in.readDouble());
        location.setLon(in.readDouble());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(location.getLat());
        dest.writeDouble(location.getLon());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public LocationParcelable createFromParcel(Parcel in) {
            LocationParcelable lp = new LocationParcelable(in);
            return lp;
        }

        public LocationParcelable[] newArray(int size) {
            return new LocationParcelable[size];
        }
    };
}
