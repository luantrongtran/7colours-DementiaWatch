package ifn372.sevencolors.backend.entities;

/**
 * Assuming that the role id is as following:
 * 1: is patient
 * 2: is carer
 * 3: is relative
 */
public abstract class User {
    private int id;
    private int role;
    private String fullName;
    private String userName;
    private String password;

    public static int PATIENT_ROLE = 1;
    public static int CARER_ROLE = 2;
    public static int RELATIVE_ROLE = 3;

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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() { return userName; }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
