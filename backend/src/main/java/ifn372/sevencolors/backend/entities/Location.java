package ifn372.sevencolors.backend.entities;

/**
 * Created by lua on 15/08/2015.
 */
public class Location {
    private double lat;
    private double lon;
    private String time;

    public Location(){}

    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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