package ifn372.sevencolors.dementiawatch.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.Vector;

import ifn372.sevencolors.backend.myApi.model.Fence;
import ifn372.sevencolors.backend.myApi.model.FenceList;

/**
 * Created by lua on 6/09/2015.
 */
public class FenceListParcelable implements Parcelable {

    FenceList fenceList;
    FenceParcelable[] fenceParcelableList;
    public FenceListParcelable(FenceList fences) {
        fenceList = fences;
        fenceParcelableList = new FenceParcelable[fenceList.getItems().size()];
        int k = 0;
        for(Fence fence : fenceList.getItems()) {
            FenceParcelable fenceParcelable = new FenceParcelable(fence);
            fenceParcelableList[k++] = fenceParcelable;
        }
    }

    public FenceListParcelable(Parcel in) {
        in.readTypedArray(fenceParcelableList, CREATOR);

        List<Fence> fences = new Vector<>();
        for(int i = 0; i < fenceParcelableList.length; i++) {
            Fence fence = fenceParcelableList[i].getFence();
            fences.add(fence);
        }
        FenceList fenceList = new FenceList();
        fenceList.setItems(fences);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(fenceParcelableList, flags);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FenceListParcelable createFromParcel(Parcel in) {
            FenceListParcelable lp = new FenceListParcelable(in);
            return lp;
        }

        public FenceListParcelable[] newArray(int size) {
            return new FenceListParcelable[size];
        }
    };
}
