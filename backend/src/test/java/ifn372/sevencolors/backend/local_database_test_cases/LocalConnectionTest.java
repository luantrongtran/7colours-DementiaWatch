package ifn372.sevencolors.backend.local_database_test_cases;

import com.google.appengine.api.utils.SystemProperty;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import ifn372.sevencolors.backend.connection.ConnectionProperty;
import ifn372.sevencolors.backend.connection.ConnectionProvider;

@RunWith(MockitoJUnitRunner.class)
public class LocalConnectionTest extends LocalDatabaseTest{
    ConnectionProvider conProvider = null;
//    ConnectionProvider spyProvider;

    @Before
    public void setUp() {
        conProvider = new ConnectionProvider();
        Properties mockProp = mock(Properties.class);
        when(mockProp.getProperty("properties.driverClassName")).thenReturn("com.mysql.jdbc.Driver");
        when(mockProp.getProperty("properties.url")).thenReturn("jdbc:mysql://localhost:3306/dementia");
//        when(mockProp.getProperty("properties.url")).thenReturn("jdbc:mysql://173.194.227.87:3306/dementia_test");
        when(mockProp.getProperty("properties.username")).thenReturn("root");
        when(mockProp.getProperty("properties.password")).thenReturn("1231988");
//        when(mockProp.getProperty("properties.password")).thenReturn("");

        ConnectionProperty conProp = new ConnectionProperty(mockProp);
        conProvider.setConProperties(conProp);

//        spyProvider = spy(conProvider);
//        doReturn(conProp).when(spyProvider).getConnectionProperty();
    }

    @After
    public void breakDown() {

    }

    @Test
    public void testLocalConnection(){
        assertNotNull(conProvider.getConnection());
    }

    @Test
    public void testUserCRUD(){

    }
}
