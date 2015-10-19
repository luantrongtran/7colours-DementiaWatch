package ifn372.sevencolors.backend.local_database_test_cases;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ifn372.sevencolors.backend.HashProvider;
import ifn372.sevencolors.backend.dao.UserDao;
import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.entities.Patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by zach on 10/10/2015.
 */
public class UserDaoTest extends LocalDatabaseTest {
    UserDao userDao;
    UserDao spyUserDao;

    public static String tableName = "user";
    public static String colId = "id";
    public static String colFullName = "fullname";
    public static String colRoles = "roles";
    public static String colCarer = "carer_id";
    public static String colRegId = "reg_id";
    public static String colUserName = "username";
    public static String colPassword = "password";

    @Before
    public void setUp() {
        userDao = new UserDao();

        spyUserDao = spy(userDao);
        doReturn(getSpiedConnectionProvider()).when(spyUserDao).getConnection();
    }

    @Test
    public void testInsertUser() {
        int userId = spyUserDao.insertUser("testUser", 1, "testUsername", "testPassword", 3);

        assertNotNull(userId);
        assertNotEquals("userId", userId, -1);

        deleteEntry("testUsername");
    }

    @Test
    public void testGetUserEntry() {
        spyUserDao.insertUser("testUser2", 1, "testUsername2", "testPassword2", 3);
        ResultSet rs = spyUserDao.getUserEntry("testUsername2");

        assertNotNull(rs);
        try {
            assertEquals("fullname", rs.getString(colFullName), "testUser2");
            assertEquals("roles", rs.getInt(colRoles), 1);
            assertEquals("username", rs.getString(colUserName), "testUsername2");
            assertEquals("password", rs.getString(colPassword), new HashProvider().encrypt("testPassword2"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        deleteEntry("testUsername2");
    }

    @Test
    public void testCreatePatient() {
        Patient mockPatient = mock(Patient.class);
        when(mockPatient.getFullName()).thenReturn("testPatientUser");
        when(mockPatient.getRole()).thenReturn(1);
        when(mockPatient.getUserName()).thenReturn("testPatient");
        when(mockPatient.getPassword()).thenReturn("patientPassword");
        when(mockPatient.getCarer_id()).thenReturn(3);
        Patient result = spyUserDao.createPatient(mockPatient);

        assertNotNull(result);
        assertEquals("fullname", result.getFullName(), "testPatientUser");
        assertEquals("roles", result.getRole(), 1);
        assertEquals("username", result.getUserName(), "testPatient");
        assertEquals("password", result.getPassword(), "patientPassword");
        assertEquals("carer_id", result.getCarer_id(), 3);

        deleteEntry("testPatient");
    }

    @Test
    public void testPatientAuthentication() {
        Patient mockPatient = mock(Patient.class);
        when(mockPatient.getFullName()).thenReturn("testPatientUser2");
        when(mockPatient.getRole()).thenReturn(1);
        when(mockPatient.getUserName()).thenReturn("testPatient2");
        when(mockPatient.getPassword()).thenReturn("patientPassword2");
        when(mockPatient.getCarer_id()).thenReturn(3);
        spyUserDao.createPatient(mockPatient);

        Patient result = spyUserDao.patientAuthentication("testPatient2", "patientPassword2");

        assertNotNull(result);
        assertEquals("fullname", result.getFullName(), "testPatientUser2");
        assertEquals("roles", result.getRole(), 1);
        assertEquals("username", result.getUserName(), "testPatient2");
        assertEquals("carer_id", result.getCarer_id(), 3);

        deleteEntry("testPatient2");
    }

    @Test
    public void testCreateCarer() {
        Carer mockCarer = mock(Carer.class);
        when(mockCarer.getFullName()).thenReturn("testCarerUser");
        when(mockCarer.getRole()).thenReturn(2);
        when(mockCarer.getUserName()).thenReturn("testCarer");
        when(mockCarer.getPassword()).thenReturn("carerPassword");
        Carer result = spyUserDao.createCarer(mockCarer);

        assertNotNull(result);
        assertEquals("fullname", result.getFullName(), "testCarerUser");
        assertEquals("roles", result.getRole(), 2);
        assertEquals("username", result.getUserName(), "testCarer");
        assertEquals("password", result.getPassword(), "carerPassword");

        deleteEntry("testCarer");
    }

    @Test
    public void testCarerAuthentication() {
        Carer mockCarer = mock(Carer.class);
        when(mockCarer.getFullName()).thenReturn("testCarerUser2");
        when(mockCarer.getRole()).thenReturn(2);
        when(mockCarer.getUserName()).thenReturn("testCarer2");
        when(mockCarer.getPassword()).thenReturn("carerPassword2");
        spyUserDao.createCarer(mockCarer);

        Carer result = spyUserDao.carerAuthentication("testCarer2", "carerPassword2");

        assertNotNull(result);
        assertEquals("fullname", result.getFullName(), "testCarerUser2");
        assertEquals("roles", result.getRole(), 2);
        assertEquals("username", result.getUserName(), "testCarer2");

        deleteEntry("testCarer2");
    }

    @Test
    public void testUpdateUserAssignment() {
        Carer mockCarer = mock(Carer.class);
        when(mockCarer.getFullName()).thenReturn("testCarerUser3");
        when(mockCarer.getRole()).thenReturn(2);
        when(mockCarer.getUserName()).thenReturn("testCarer3");
        when(mockCarer.getPassword()).thenReturn("carerPassword3");
        Carer resultCarer = spyUserDao.createCarer(mockCarer);
        int carer_id = resultCarer.getId();

        Patient mockPatient = mock(Patient.class);
        when(mockPatient.getFullName()).thenReturn("testPatientUser3");
        when(mockPatient.getRole()).thenReturn(1);
        when(mockPatient.getUserName()).thenReturn("testPatient3");
        when(mockPatient.getPassword()).thenReturn("patientPassword3");
        when(mockPatient.getCarer_id()).thenReturn(3);
        Patient patient = spyUserDao.createPatient(mockPatient);
        List<String> patientIds = new ArrayList<String>();
        patientIds.add(String.valueOf(patient.getId()));

        boolean updateSuccess = spyUserDao.updateUserAssignment(carer_id, patientIds);

        assertEquals("updateSuccess", updateSuccess, true);
    }

    public void deleteEntry(String userName) {
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = spyUserDao.getConnection();
            String query = "DELETE FROM user WHERE username = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, userName);
            preparedStmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}