package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Vector;

import ifn372.sevencolors.backend.entities.Location;
import ifn372.sevencolors.backend.entities.Patient;

public class PatientDao extends DAOBase {

    public static String tableName = "user";
    public static String colPatientId = "id";
    public static String colFullName = "fullname";
    public static String colRoes = "roles";
    public static String colCarer = "carer_id";


    public static String currentLocationTableName = "current_location";
    public static String cl_colId = "id";
    public static String cl_colPatientId = "patient_id";
    public static String cl_colUpdateTime = "time";
    public static String cl_colLat = "lat";
    public static String cl_colLon = "lon";

    public static String patientView = "patient_view";

    /**
     *
     * @param id
     * @return null if cannot find a user with the given id.
     */
//    public Patient findById(int id) {
//        Connection con = getConnection();
//        Patient patient = null;
//        String sql = "SELECT * FROM " + tableName + " WHERE " + colPatientId + " = ?";
//        PreparedStatement ps = null;
//        try {
//            ps = con.prepareStatement(sql);
//            ps.setInt(1, id);
//
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                patient = new Patient();
//                patient.setId(rs.getInt(colPatientId));
//                patient.setFullName(rs.getString(colFullName));
//
//                Location location = new Location();
//                location.setLat(rs.getFloat(colCurrentLocationLat));
//                location.setLon(rs.getFloat(colCurrentLocationLon));
//
//                patient.setCurrentLocation(location);
//
//                patient.setLocation_last_update(rs.getTimestamp(colUpdateTime));
//            }
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return patient;
//    }

    /**
     *
     * @param patient
     * @return return -1 if couldn't insert
     */
//    public int insertANewPatient(Patient patient) {
//        Connection con = getConnection();
//        String sql = "INSERT INTO " + tableName + " (" + colFullName + ") VALUES(?)";
//        PreparedStatement ps = null;
//        int last_inserted_id = -1;
//        try {
//            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//            ps.setString(1, patient.getFullName());
//
//            ps.executeUpdate();
//
//            //return the id of the inserted object
//            ResultSet rs = ps.getGeneratedKeys();
//            if(rs.next())
//            {
//                last_inserted_id = rs.getInt(1);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return last_inserted_id;
//    }

//    public void delete(int id) {
//        Connection con = getConnection();
//        String sql = "DELETE FROM " + tableName + " WHERE " + colPatientId + "= ?";
//        try {
//            PreparedStatement ps = con.prepareStatement(sql);
//            ps.setInt(1, id);
//
//            ps.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void updateCurrentLocation(Patient patient) {
        Connection con = getConnection();
        String sql = "INSERT INTO " + currentLocationTableName + " (" + cl_colPatientId + ", " + cl_colLat
                + ", " + cl_colLon + " ," + cl_colUpdateTime + ") VALUES (?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, patient.getId());
            ps.setDouble(2, patient.getCurrentLocation().getLat());
            ps.setDouble(3, patient.getCurrentLocation().getLon());

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            ps.setTimestamp(4, timestamp);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vector<Patient> getPatientsListByRelative(int relativeId) {
        Vector<Patient> patientList = new Vector<Patient>();

        //need implementing later

        return patientList;
    }

    public Vector<Patient> getPatientsListByCarer(int carerId) {
        Connection con = getConnection();
        String sql = "SELECT * FROM " + patientView + " WHERE " + colCarer + " = ?";
        PreparedStatement ps = null;
        Vector<Patient> patientList = new Vector<Patient>();
        try{
            ps = con.prepareStatement(sql);
            ps.setInt(1, carerId);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Patient patient = new Patient();
                patient.setId(rs.getInt(colPatientId));
                patient.setFullName(rs.getString(colFullName));
                patient.setRole(rs.getInt(colRoes));

                Location location = new Location();
                location.setLat(rs.getDouble(cl_colLat));
                location.setLon(rs.getDouble(cl_colLon));
                patient.setCurrentLocation(location);

                patient.setLocation_last_update(rs.getTimestamp(cl_colUpdateTime).getTime());

                patient.setCarer_id(carerId);

                patientList.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patientList;
    }
}