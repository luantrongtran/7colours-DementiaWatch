package ifn372.sevencolors.backend.entities;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Jhonatan Calle on 10/10/2015.
 */
public class LocationList {

    public LocationList(){
        locationList = new TreeMap<>();
    }
    public Map<String, Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(Map<String, Location> locationList) {
        this.locationList = locationList;
    }

    Map<String,Location> locationList;
}
