package ifn372.sevencolors.backend.local_database_test_cases;

/**
 * Created by Kirti on 3/9/2015.
 */

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import ifn372.sevencolors.backend.dao.PatientDao;
import ifn372.sevencolors.backend.entities.Location;
import ifn372.sevencolors.backend.entities.Patient;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class PatientTest extends LocalDatabaseTest{

    PatientDao patientDao;
    PatientDao spyPatientDao;
    @Before
    public void setUp(){
        patientDao = new PatientDao();

        spyPatientDao = spy(patientDao);
        doReturn(getSpiedConnectionProvider()).when(spyPatientDao).getConnection();
    }


    @Test
    public void testUpdateCurrentLocation() {

        Patient mockPatient = mock(Patient.class);
        when(mockPatient.getId()).thenReturn(2);

        Location location = new Location(128.76d, 110.50d);

        mockPatient.setCurrentLocation(location);

        spyPatientDao.updateCurrentLocation(mockPatient);

        assertEquals("PatientId", mockPatient.getId(), 2);
        assertEquals("Longitude", mockPatient.getCurrentLocation().getLat(), 128.76, 0.001);
        assertEquals("Latitude", mockPatient.getCurrentLocation().getLon(), 110.50, 0.0);


    }

    @Test
    public void testGetPatientsListByCarer(){
        Patient mockPatient = mock(Patient.class);
        when(mockPatient.getId()).thenReturn(2);
        when(mockPatient.getFullName()).thenReturn("KIRTi");
        when(mockPatient.getRole()).thenReturn(1);

        Location location = new Location(128.76d, 110.50d);
        mockPatient.setCurrentLocation(location);

        Vector<Patient> patientVector = spyPatientDao.getPatientsListByCarer(2);

     //   Patient f = spyPatientDao.findById(patientVector);//get the insterted from the database
     //   assertNotNull("The fence should not be null", f);

        assertEquals("Patiet Id",patientVector.get(0), 2);

    }

    @Test
    public void testGetPatientLocation(){
        Patient mockPatient = mock(Patient.class);
        Location mockLocation = mock(Location.class);
        when(mockLocation.getLat()).thenReturn(128.76d);
        when(mockLocation.getLon()).thenReturn(110.50d);



    //    assertEquals("Latitude", f.getCurrentLocation().getLat(),128.76, 0.001 );


    }


    @Test
    public void testGetLocationHistory()
    {
        //create dummy user to complete the test.
        int dummyUserId = this.createNewUser("Dummy user ", 1);
        int interval = -1;
        int sec = 5;
        double lat = 100000;
        double lon = 200000;
        Timestamp timestamp = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, sec);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<String> dateList = new ArrayList<String>();
        for(int i = 0; i < 24; i++)
        {
            cal.add(Calendar.HOUR_OF_DAY, interval);
            this.updateCurrentLocationWithTimestamp(
                    this.createDummyPatientLocation(dummyUserId, lat++, lon++),
                    new Timestamp(cal.getTimeInMillis()));
            dateList.add(sdf.format(cal.getTime()).toString());
        }

        //System.out.println("===== Original date =====");
        int index = 0;
        Collections.reverse(dateList);
//        for(String date : dateList)
//        {
//            System.out.println(date);
//        }

        TreeMap<String, Location> history = spyPatientDao.getLocationHistory(dummyUserId);
        for(Map.Entry<String, Location> item : history.entrySet())
        {
            System.out.print("Key = " + item.getKey());
            if(item != null && item.getValue() != null)
            {
                System.out.println(
                        ", Lat = " + item.getValue().getLat()
                                + ", Lon = " + item.getValue().getLon()
                                + ", time = " + item.getValue().getTime());
                System.out.println(dateList.get(index));
                assertEquals("time: ", item.getValue().getTime(), dateList.get(index++));
            }
            else {
                System.out.println("Time has not been found.");
            }
        }

    }


    private void updateCurrentLocationWithTimestamp(Patient patient, Timestamp stamp) {
        Connection con = spyPatientDao.getConnection();
        String sql = "INSERT INTO current_location (patient_id, lat, lon, time) VALUES (?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, patient.getId());
            ps.setDouble(2, patient.getCurrentLocation().getLat());
            ps.setDouble(3, patient.getCurrentLocation().getLon());
            ps.setTimestamp(4, stamp);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private int createNewUser(String fullName, int role)
    {
        int userId = -1;
        Connection con = null;
        PreparedStatement ps = null;
        StringBuffer sql = new StringBuffer("insert into " + PatientDao.tableName);
        sql.append(" (" + PatientDao.colFullName + ", " + PatientDao.colRoes + ") values (?, ?) ");
        try
        {
            con = spyPatientDao.getConnection();
            ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fullName);
            ps.setInt(2, role);
            int num = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs != null && rs.next())
            {
                userId = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    private Patient createDummyPatientLocation(int patientId, double lat, double lon)
    {
        Patient patient = new Patient();
        patient.setId(patientId);
        Location loc = new Location(lat, lon);
        patient.setCurrentLocation(loc);
        return patient;
    }


}