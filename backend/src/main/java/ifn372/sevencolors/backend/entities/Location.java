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
}