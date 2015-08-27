package ifn372.sevencolors.backend.entities;

/**
<<<<<<< HEAD
 * Created by koji on 2015/08/28.
=======
 * Created by koji on 2015/08/27.
>>>>>>> master
 */
public class Fence {

    //Return value (creation successful/fail)
    boolean success;

    String userId;
    String fenceName;
    double lon;
    double lat;
    float radius;
    String address;

<<<<<<< HEAD
    public Fence() {
    }

    public Fence(boolean success) {
=======
    public Fence(){
    }
    public Fence(boolean success)
    {
>>>>>>> master
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

<<<<<<< HEAD

=======
>>>>>>> master
}
