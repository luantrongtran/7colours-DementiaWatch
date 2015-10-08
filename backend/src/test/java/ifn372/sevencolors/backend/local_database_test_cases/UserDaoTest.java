package ifn372.sevencolors.backend.local_database_test_cases;

import org.junit.Before;
import org.junit.Test;

import ifn372.sevencolors.backend.dao.UserDao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * Created by koji on 2015/10/08.
 */
public class UserDaoTest extends LocalDatabaseTest
{
    UserDao userDao;
    UserDao spyUserDao;
    @Before
    public void setUp(){
        userDao = new UserDao();
        spyUserDao = spy(userDao);
        doReturn(getSpiedConnectionProvider()).when(spyUserDao).getConnection();
    }


    @Test
    public void testUpdatePatient()
    {
        //create patients and carers
//        Carer mockCarer = mock(Carer.class);
//        List<String> patientsList = mock(List.class);
//        //Iterator<String> mockIter = mock(Iterator.class);
//        patientsList.add("1");
//        when(mockCarer.getFullName()).thenReturn("Koji ");
//        when(mockCarer.getRole()).thenReturn(User.CARER_ROLE);
//        when(mockCarer.getUserName()).thenReturn("Koji " + Calendar.getInstance().getTime().toString());
//        when(mockCarer.getPassword()).thenReturn("password123");
//        when(mockCarer.getPatientIds()).thenReturn(patientsList);
//        //when(mockCarer.getPatientIds().iterator()).thenReturn(mocki);
//
//        Carer newCarer = spyUserDao.createCarer(mockCarer);
//
//        when(mockCarer.getFullName()).thenReturn("Koji Nishimoto2");
//        when(mockCarer.getRole()).thenReturn(User.CARER_ROLE);
//        when(mockCarer.getUserName()).thenReturn("Koji2 "+ Calendar.getInstance().getTime().toString());
//        when(mockCarer.getPassword()).thenReturn("password456");
//        when(mockCarer.getPatientIds()).thenReturn(patientsList);
//        Carer newCarer2 = spyUserDao.createCarer(mockCarer);
//
//        Patient mockPatient = mock(Patient.class);
//        when(mockPatient.getFullName()).thenReturn("Test Patient");
//        when(mockPatient.getRole()).thenReturn(User.PATIENT_ROLE);
//        when(mockPatient.getUserName()).thenReturn("test");
//        when(mockPatient.getPassword()).thenReturn("password12345");
//        when(mockPatient.getCarer_id()).thenReturn(newCarer.getId());
//        Patient newPatient = spyUserDao.createPatient(mockPatient);

//        //update patient
//        //int id = newPatient.getId();
//        Patient targetPatient = mock(Patient.class);
//        when(targetPatient.getId()).thenReturn(10);
//        when(targetPatient.getFullName()).thenReturn("Update Patient");
//        when(targetPatient.getRole()).thenReturn(User.PATIENT_ROLE);
//        when(targetPatient.getUserName()).thenReturn("update test");
//        when(targetPatient.getPassword()).thenReturn("updatepassword12345");
//        when(targetPatient.getCarer_id()).thenReturn(2);
//        Patient updatePatient = spyUserDao.updatePatient(targetPatient);
//
//        assertNotEquals("User ID", updatePatient.getId(), UserEndpoint.CODE_ERR_UPDATE_USER_FAILED);
//        assertEquals("Full name", updatePatient.getFullName(), "Update Patient");
//        assertEquals("Role", updatePatient.getRole(), User.PATIENT_ROLE);
//        assertEquals("User name", updatePatient.getUserName(), "update test");
//        assertEquals("password", updatePatient.getPassword(), "updatepassword12345");
//        assertEquals("Carer ID", updatePatient.getCarer_id(), 2);

    }

    @Test
    public void testUpdateCarer()
    {

    }

    @Test
    public void testUpdateUser()
    {
        int ret = spyUserDao.updateUser(10, "full name", 1, "update username", "update password", 3);
        System.out.print("ret = " + ret);
        assertEquals("ret", 10, ret);
    }

    @Test
    public void testDeleteUser()
    {
        int ret = spyUserDao.deleteUser(10);
        System.out.print("ret = " + ret);
        assertEquals("ret", 10, ret);
    }
}
