package ifn372.sevencolors.backend.entities;

/**
 * Created by lua on 15/08/2015.
 */
public class Patient extends User {

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    String gcmId;

    private boolean safety;

    public void setSafety(boolean safety) { this.safety = safety; }
    public boolean getSafety() { return safety; }

    public long getLocation_last_update() {
        return location_last_update;
    }

    public void setLocation_last_update(long location_last_update) {
        this.location_last_update = location_last_update;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public FenceList getFenceList() {
        return fenceList;
    }

    public void setFenceList(FenceList fenceList) {
        this.fenceList = fenceList;
    }


    public int getCarer_id() {
        return carer_id;
    }

    public void setCarer_id(int carer_id) {
        this.carer_id = carer_id;
    }

    Location currentLocation;
    private long location_last_update;
    int carer_id;

    FenceList fenceList = new FenceList();
}
