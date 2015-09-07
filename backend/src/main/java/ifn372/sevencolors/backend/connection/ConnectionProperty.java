package ifn372.sevencolors.backend.connection;

import java.util.Properties;

/**
 * Created by lua on 15/08/2015.
 */
public class ConnectionProperty {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public ConnectionProperty(Properties conProperties) {
        driverClassName = conProperties.getProperty("properties.driverClassName");
        url = conProperties.getProperty("properties.url");
        username = conProperties.getProperty("properties.username");
        password = conProperties.getProperty("properties.password");
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    @Override
    public String toString() {
        String str = getDriverClassName() + "\n" + url + "\nUsername123: " + getUsername()
                + "\npassword: " + getPassword().isEmpty();
        return str;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
