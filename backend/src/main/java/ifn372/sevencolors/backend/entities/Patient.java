package ifn372.sevencolors.backend.entities;

import java.sql.Timestamp;

/**
 * Created by lua on 15/08/2015.
 */
public class Patient extends User {

    public Timestamp getLocation_last_update() {
        return location_last_update;
    }

    public void setLocation_last_update(Timestamp location_last_update) {
        this.location_last_update = location_last_update;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    Location currentLocation;
    private Timestamp location_last_update;
}
