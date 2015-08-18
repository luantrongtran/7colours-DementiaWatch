package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;

import ifn372.sevencolors.backend.entities.Location;
import ifn372.sevencolors.backend.entities.Patient;
import ifn372.sevencolors.backend.entities.User;

public class PatientDao extends DAOBase {

    public static String tableName = "patient";
    public static String colId = "id";
    public static String colFullName = "fullname";
    public static String colCurrentLocationLat = "current_location_lat";
    public static String colCurrentLocationLon = "current_location_lon";
    public static String colCurrentLocationLastUpdateTime = "current_location_last_update_time";

    /**
     *
     * @param id
     * @return null if cannot find a user with the given id.
     */
    public Patient findById(int id) {
        Connection con = getConnection();
        Patient patient = null;
        String sql = "SELECT * FROM " + tableName + " WHERE " + colId + " = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                patient = new Patient();
                patient.setId(rs.getInt(colId));
                patient.setFullName(rs.getString(colFullName));

                Location location = new Location();
                location.setLat(rs.getFloat(colCurrentLocationLat));
                location.setLon(rs.getFloat(colCurrentLocationLon));

                patient.setCurrentLocation(location);

                patient.setLocation_last_update(rs.getTimestamp(colCurrentLocationLastUpdateTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patient;
    }

    /**
     *
     * @param patient
     * @return return -1 if couldn't insert
     */
    public int insertANewPatient(Patient patient) {
        Connection con = getConnection();
        String sql = "INSERT INTO " + tableName + " (" + colFullName + ") VALUES(?)";
        PreparedStatement ps = null;
        int last_inserted_id = -1;
        try {
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, patient.getFullName());

            ps.executeUpdate();

            //return the id of the inserted object
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
            {
                last_inserted_id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return last_inserted_id;
    }

    public void delete(int id) {
        Connection con = getConnection();
        String sql = "DELETE FROM " + tableName + " WHERE " + colId + "= ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateCurrentLocation(Patient patient) {
        Connection con = getConnection();
        String sql = "UPDATE " + tableName + " SET " + colCurrentLocationLat + " = ?, " + colCurrentLocationLon
                + " = ? WHERE " + colId + " = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setFloat(1, patient.getCurrentLocation().getLat());
            ps.setFloat(2, patient.getCurrentLocation().getLat());
            ps.setFloat(3, patient.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}