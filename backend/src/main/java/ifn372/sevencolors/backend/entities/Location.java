package ifn372.sevencolors.backend.entities;

/**
 * Created by lua on 15/08/2015.
 */
public class Location {
    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    private float lat;
    private float lon;

    public Location(){}
    public Location(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Location)) {
            return false;
        }
        Location location = (Location)obj;
        if(this.lat == location.lat) {
            if(this.lon != location.getLon()){
                return false;
            }
        } else {
            return false;
        }
        return true;
    }
}