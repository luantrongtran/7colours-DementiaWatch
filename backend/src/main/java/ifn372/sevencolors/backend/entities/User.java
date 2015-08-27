package ifn372.sevencolors.backend.entities;

/**
 * Created by lua on 15/08/2015.
 */
public abstract class User {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    private int id;
    private int role;
    private String fullName;
}
