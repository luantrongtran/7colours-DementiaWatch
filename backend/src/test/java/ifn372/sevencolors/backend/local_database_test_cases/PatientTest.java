package ifn372.sevencolors.backend.local_database_test_cases;

/**
 * Created by Kirti on 3/9/2015.
 */
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Vector;

import ifn372.sevencolors.backend.dao.PatientDao;
import ifn372.sevencolors.backend.entities.Location;
import ifn372.sevencolors.backend.entities.Patient;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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

        Location mockLocation = mock(Location.class);
        when(mockLocation.getLat()).thenReturn(128.76d);
        when(mockLocation.getLon()).thenReturn(110.50d);

        when(mockPatient.getCurrentLocation()).thenReturn(mockLocation);

        spyPatientDao.updateCurrentLocation(mockPatient);

        assertEquals("PatientId", mockPatient.getId(), 2);
        assertEquals("Longitude", mockPatient.getCurrentLocation().getLat(), 128.76, 0.001);
        assertEquals("Latitude", mockPatient.getCurrentLocation().getLon(), 110.50, 0.0);


    }

    @Test
    public void testGetPatientsListByCarer(){
       /* Patient mockPatient = mock(Patient.class);
        when(mockPatient.getId()).thenReturn(2);
        when(mockPatient.getFullName()).thenReturn("KIRTi");
        when(mockPatient.getRole()).thenReturn(1);
*/
        /*Location mockLocation = mock(Location.class);
        when(mockLocation.getLat()).thenReturn(128.76d);
        when(mockLocation.getLon()).thenReturn(110.50d);*/


        Vector<Patient> patientVector = spyPatientDao.getPatientsListByCarer(2);

        assertEquals("Patiet Id", patientVector.get(0).getId(), 2);
        assertEquals("Full Name", patientVector.get(0).getFullName(), "kirti");
        assertEquals("Get Role",patientVector.get(0).getRole(), 1);
        assertEquals("Latitude",patientVector.get(0).getCurrentLocation().getLat(), 128.76, 0.001);
        assertEquals("Longitude",patientVector.get(0).getCurrentLocation().getLon(), 110.50, 0.0);


    }

    @Test
    public void testGetPatientLocation(){
        /*Patient mockPatient = mock(Patient.class);

        Location mockLocation = mock(Location.class);
        when(mockLocation.getLat()).thenReturn(128.76d);
        when(mockLocation.getLon()).thenReturn(110.50d);

        when(mockPatient.getCurrentLocation()).thenReturn(mockLocation);*/

        Location l = spyPatientDao.getPatientLocation(2);

        assertEquals("Longitude", l.getLat(), 128.76, 0.001);
        assertEquals("Latitude", l.getLon(), 110.50, 0.0);

    }
}