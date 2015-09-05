package ifn372.sevencolors.backend.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by lua on 6/09/2015.
 */
public class FenceList {

    public FenceList() {
        fenceList = new Vector<>();
    }
    public List<Fence> getItems() {
        return fenceList;
    }

    public void setItems(List<Fence> fenceList) {
        this.fenceList = fenceList;
    }

    List<Fence> fenceList;
}
