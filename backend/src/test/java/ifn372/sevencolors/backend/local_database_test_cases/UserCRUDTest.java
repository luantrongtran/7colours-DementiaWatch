package ifn372.sevencolors.backend.local_database_test_cases;

import org.junit.Before;
import org.junit.Test;

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

public class UserCRUDTest extends LocalDatabaseTest{

    PatientDao patientDao;
    PatientDao spyPatientDao;
    @Before
    public void setUp(){
        patientDao = new PatientDao();

        spyPatientDao = spy(patientDao);
        doReturn(getSpiedConnectionProvider()).when(spyPatientDao).getConnection();
    }

    @Test
    public void testInsertANewPatient() {
        Patient mockPatient = mock(Patient.class);
        when(mockPatient.getFullName()).thenReturn("Trong Luan Tran");
        when(mockPatient.getId()).thenReturn(1);

        int id = spyPatientDao.insertANewPatient(mockPatient);

        Patient patient = spyPatientDao.findById(id);
        assertNotNull(patient);//test insert

        Location expectedLocation = new Location(108, 108);
        patient.setCurrentLocation(new Location(108, 108));

        spyPatientDao.updateCurrentLocation(patient);

        patient = spyPatientDao.findById(patient.getId());
        assertEquals("The current patient should be (108,108) as expected",
                patient.getCurrentLocation(), expectedLocation); // test updateCurrentLocation

        spyPatientDao.delete(patient.getId());

        patient = spyPatientDao.findById(patient.getId());
        assertNull("The patient has been removed and should not be in database", patient);
    }
}