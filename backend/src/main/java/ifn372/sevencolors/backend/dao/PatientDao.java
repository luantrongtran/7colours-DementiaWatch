package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;

import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.entities.Fence;
import ifn372.sevencolors.backend.entities.FenceList;
import ifn372.sevencolors.backend.entities.Location;
import ifn372.sevencolors.backend.entities.Patient;

public class PatientDao extends DAOBase {

    private static final Logger logger = Logger.getLogger(PatientDao.class.getName());

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
    public static final String cl_arias_closest_to_interval = "closest_to_interval";

    public static String patientView = "patient_view";

    private static final int MAX_OFFSET_HOURS_OF_LOCATION_HISTORY = -24;
    private static final int INTERVAL_OF_HISTORY = 1;

    private static final String TABLE_NAME_PATIENT_RELATIVE = "patient_relative";
    private static final String COL_NAME_PATIENT_ID = "patient_id";
    private static final String COL_NAME_RELATIVE_ID = "relative_id";

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
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, carerId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt(colPatientId));
                patient.setFullName(rs.getString(colFullName));
                patient.setRole(rs.getInt(colRoes));

                Location location = new Location();
                location.setLat(rs.getDouble(cl_colLat));
                location.setLon(rs.getDouble(cl_colLon));
                patient.setCurrentLocation(location);

                Timestamp ts = rs.getTimestamp(cl_colUpdateTime);
                if(ts == null) {
                    patient.setLocation_last_update(-1l);
                } else {
                    patient.setLocation_last_update(ts.getTime());
                }

                patient.setCarer_id(carerId);

                if(!patientList.contains(patient)) {
                    //avoid the case in which 2 records have the same patient id
                    patientList.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patientList;
    }

    public Location getPatientLocation(int patientId) {
        Connection con = null;
        Statement stmt = null;
        Location location = new Location();
        try
        {
            con = getConnection();
            stmt = con.createStatement();
            Timestamp time = null;
            String sql = "SELECT * FROM current_location WHERE patient_id=" + String.valueOf(patientId);
            ResultSet rs = stmt.executeQuery(sql);

            /* for testing row count
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            logger.info("Row Count: " + String.valueOf(rowcount));*/

            // set fence data
            while(rs.next()) {
                if (time == null) {
                    location.setLat(rs.getDouble("lat"));
                    location.setLon(rs.getDouble("lon"));
                    time = rs.getTimestamp("time");
                } else {
                    if (rs.getTimestamp("time").after(time)) {
                        location.setLat(rs.getDouble("lat"));
                        location.setLon(rs.getDouble("lon"));
                        time = rs.getTimestamp("time");
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return location;
    }

    /**
     * This methods will update the fence list of each patient
     */
    public void getFencesForPatients(List<Patient> patientList) {
        FenceDao fenceDao = new FenceDao();
        for(Patient p : patientList) {
            int patientId = p.getId();
            List<Fence> fences = fenceDao.getFences(patientId);
            FenceList fenceList = new FenceList();
            fenceList.setItems(fences);
            p.setFenceList(fenceList);
        }
    }

    /**
     * Created by Zachary Tang
     * @param patientId The user ID of the patient
     * @return carer The carer object for the specific patient
     */
    public Carer getCarerByPatientId(int patientId) {
        Connection con = null;
        Statement stmt = null;
        Carer carer = new Carer();
        int carerId = -1;
        try
        {
            con = getConnection();

            // get carer ID
            stmt = con.createStatement();
            String sql = "SELECT * FROM user WHERE id=" + String.valueOf(patientId);
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                carerId = rst.getInt("carer_id");
            }

            // get carer data
            if (carerId != -1) {
                stmt = con.createStatement();
                sql = "SELECT * FROM user WHERE id=" + String.valueOf(carerId);
                ResultSet rs = stmt.executeQuery(sql);

                // set carer data
                while(rs.next()) {
                    carer.setId(rs.getInt("id"));
                    carer.setFullName(rs.getString("fullname"));
                    carer.setRole(rs.getInt("roles"));
                    carer.setGCMId(rs.getString("reg_id"));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return carer;
    }

    public Patient getPatientById(int id) {
        Connection con = null;
        Statement stmt = null;
        Patient patient = new Patient();
        patient.setId(id);
        try
        {
            con = getConnection();

            // get carer ID
            stmt = con.createStatement();
            String sql = "SELECT * FROM user WHERE id=" + String.valueOf(id);
            ResultSet rst = stmt.executeQuery(sql);

            while(rst.next()) {
                patient.setCarer_id(rst.getInt("carer_id"));
                patient.setRole(rst.getInt("roles"));
                patient.setFullName(rst.getString("fullname"));
                patient.setGcmId(rst.getString("reg_id"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return  patient;
    }
	
    /**
     * Created by Koji
     * Get location history
     *   Old locations are retrieved depending on OLDEST_HOURS_OF_LOCATION_HISTORY and INTERVAL_OF_HISTORY.
     *   The oldest location is (Current hour - MAX_OFFSET_HOURS_OF_LOCATION_HISTORY) + 1.
     *   E.g. Current time: 27/09/2015 21:22:35 -> The oldest location: 26/09/2015 22:00:00
     *
     * @param patientId Patient ID
     * @return TreeMap object that contains locations of the patient. The return value is sorted by time asc (Older location first).
     */
    public TreeMap<String, Location> getLocationHistory(int patientId)
    {
        TreeMap<String, Location> history = new TreeMap<>();
        Connection con = null;
        PreparedStatement ps = null;

        try
        {
            // The oldest date time of location history to retrieve
            Calendar oldest = Calendar.getInstance();
            oldest.add(Calendar.HOUR_OF_DAY, MAX_OFFSET_HOURS_OF_LOCATION_HISTORY);
            // Interval of time (every hour)
            Calendar interval = Calendar.getInstance();
            interval.add(Calendar.HOUR_OF_DAY, MAX_OFFSET_HOURS_OF_LOCATION_HISTORY);
            int hour = interval.get(Calendar.HOUR_OF_DAY);
            interval.set(Calendar.HOUR_OF_DAY, (hour + INTERVAL_OF_HISTORY));
            interval.set(Calendar.MINUTE, 0);
            interval.set(Calendar.SECOND, 0);
            interval.set(Calendar.MILLISECOND, 0);
            //Format for interval
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            con = getConnection();
            StringBuffer selectSql = new StringBuffer();
            selectSql.append("select " + cl_colId + ", " + cl_colPatientId + ", max(" + cl_colUpdateTime + ") as " + cl_arias_closest_to_interval + ", ");
            selectSql.append(cl_colLat + ", " + cl_colLon);
            selectSql.append(" from " + currentLocationTableName);
            StringBuffer countSql = new StringBuffer("select count(*) from " + currentLocationTableName);
            StringBuffer whereSql = new StringBuffer(" where patient_id = ? and ( " + cl_colUpdateTime + " >= ? and " + cl_colUpdateTime + " <= ? )");

            countSql.append(whereSql);
            selectSql.append(whereSql);
            //debug code
            //long start = System.currentTimeMillis();
            for(int i = 0; i < Math.abs(MAX_OFFSET_HOURS_OF_LOCATION_HISTORY); i++)
            {
                ps = con.prepareStatement(countSql.toString());
                ps.setInt(1, patientId);
                ps.setString(2, sdf.format(oldest.getTime()));
                ps.setString(3, sdf.format(interval.getTime()));
                ResultSet rs = ps.executeQuery();
                boolean exist = false;
                if (rs.next())
                {
                    exist = rs.getInt(1) == 0 ? false : true;
                    //System.out.println(ps);
                    //System.out.println("The number of result: " + rs.getInt(1));
                }
                if(!exist)
                {
                    //Increment interval
                    hour = interval.get(Calendar.HOUR_OF_DAY);
                    interval.set(Calendar.HOUR_OF_DAY, ++hour);
                    continue;
                }

                ps = con.prepareStatement(selectSql.toString());
                ps.setInt(1, patientId);
                ps.setString(2, sdf.format(oldest.getTime()));
                ps.setString(3, sdf.format(interval.getTime()));
                rs = ps.executeQuery();
                Location loc = null;
                while(rs.next())
                {
                    //System.out.println(ps);
                    loc = new Location();
                    loc.setLon(rs.getDouble(cl_colLon));
                    loc.setLat(rs.getDouble(cl_colLat));
                    loc.setTime(sdf.format(rs.getTimestamp(cl_arias_closest_to_interval)));
                    Patient patient = new Patient();
                    patient.setId(rs.getInt(cl_colPatientId));
                    patient.setCurrentLocation(loc);
                    //Add location to history
                    history.put(sdf.format(interval.getTime()), loc);
                }
                //Add location to history
                //history.put(sdf.format(interval.getTime()), loc);
                //Increment interval
                hour = interval.get(Calendar.HOUR_OF_DAY);
                interval.set(Calendar.HOUR_OF_DAY, ++hour);
            }
//            //debug code
//            long end = System.currentTimeMillis();
//            long time = end - start;
//            System.out.println("running time: " + time);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return history;
    }

    public Patient getPatientByUsername(String username) {
        UserDao userDao = new UserDao();
        ResultSet rs = userDao.getUserEntry(username);
        if(rs == null) {
            return null;
        }

        Patient patient = null;
        try {
            while(rs.next()) {
                patient = new Patient();
                patient.setCarer_id(rs.getInt("carer_id"));
                patient.setRole(rs.getInt("roles"));
                patient.setFullName(rs.getString("fullname"));
                patient.setGcmId(rs.getString("reg_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return patient;
    }
}