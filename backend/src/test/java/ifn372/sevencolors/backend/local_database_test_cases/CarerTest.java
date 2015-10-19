package ifn372.sevencolors.backend.local_database_test_cases;

/**
 * Created by Harjot on 9/6/2015.
 */
import org.junit.Before;
import org.junit.Test;

//import java.sql.Timestamp;
//import java.util.Vector;
import java.io.IOException;
import java.util.List;

import ifn372.sevencolors.backend.dao.CarerDao;
import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.webservices.CarerEndpoint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;



public class CarerTest extends LocalDatabaseTest {

    CarerDao carerDao;
    CarerDao spyCarerDao;

    @Before
    public void setUp() {
        carerDao = new CarerDao();

        spyCarerDao = spy(carerDao);
        doReturn(getSpiedConnectionProvider()).when(spyCarerDao).getConnection();
    }


    @Test
    public void testGetPatientIds() {
       // int CarerDao.id;
        Carer mockCarer = mock(Carer.class);
        when(mockCarer.getId()).thenReturn(3);
        //when(mockPatient.getFullName()).thenReturn("KIRTi");
        //when(mockPatient.getRole()).thenReturn(1);

        //Location location = new Location(128.76d, 110.50d);
        //mockPatient.setCurrentLocation(location);

        List<String> carerList = spyCarerDao.getPatientIds(3);

        //   Patient f = spyPatientDao.findById(patientVector);//get the insterted from the database
        //   assertNotNull("The fence should not be null", f);

        assertEquals("Patient Id", carerList.get(0), "1");
    }

    @Test
    public void testUpdateGCMId() {
        Carer mockCarer = mock(Carer.class);
        when(mockCarer.getId()).thenReturn(3);
        String gcmId = spyCarerDao.updateGCMId(mockCarer, "testing");

        assertNotNull(gcmId);
        assertEquals("gcmId", gcmId, "testing");
    }

    /*
    @Test
    public void testCarerEndpoint() throws IOException {
        Carer carer = new CarerEndpoint().updateGcmId(3, "test");

        assertNotNull(carer);
        assertEquals("gcmId", carer.getGCMId(), "test");
    }*/
}
