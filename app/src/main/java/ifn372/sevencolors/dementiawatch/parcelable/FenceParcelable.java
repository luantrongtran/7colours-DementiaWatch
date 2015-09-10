package ifn372.sevencolors.dementiawatch.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import ifn372.sevencolors.backend.myApi.model.Fence;

/**
 * Created by lua on 6/09/2015.
 */
public class FenceParcelable implements Parcelable {
    public Fence getFence() {
        return fence;
    }

    public void setFence(Fence fence) {
        this.fence = fence;
    }

    Fence fence;

    public FenceParcelable(Fence fence) {
        this.fence = fence;
    }

    public FenceParcelable(Parcel in) {
        fence.setUserId(in.readInt());
        fence.setFenceName(in.readString());
        fence.setLat(in.readDouble());
        fence.setLon(in.readDouble());
        fence.setRadius(in.readFloat());
        fence.setAddress(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fence.getUserId());
        dest.writeString(fence.getFenceName());
        dest.writeDouble(fence.getLat());
        dest.writeDouble(fence.getLon());
        dest.writeFloat(fence.getRadius());
        dest.writeString(fence.getAddress());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FenceParcelable createFromParcel(Parcel in) {
            return new FenceParcelable(in);
        }

        public FenceParcelable[] newArray(int size) {
            return new FenceParcelable[size];
        }
    };
}
