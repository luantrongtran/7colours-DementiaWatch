package ifn372.sevencolors.backend.local_database_test_cases;

import java.sql.Connection;
import java.util.Properties;

import ifn372.sevencolors.backend.connection.ConnectionProperty;
import ifn372.sevencolors.backend.connection.ConnectionProvider;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class LocalDatabaseTest {
    private static Connection connection = null;
    public Connection getSpiedConnectionProvider() {
        if (connection == null) {
            ConnectionProvider conProvider = new ConnectionProvider();
            Properties mockProp = mock(Properties.class);
            when(mockProp.getProperty("properties.driverClassName")).thenReturn("com.mysql.jdbc.Driver");
            when(mockProp.getProperty("properties.url")).thenReturn("jdbc:mysql://localhost:3306/dementia");
            when(mockProp.getProperty("properties.username")).thenReturn("root");
            when(mockProp.getProperty("properties.password")).thenReturn("1231988");

            ConnectionProperty conProp = new ConnectionProperty(mockProp);

            ConnectionProvider spyConnectionProvider = spy(conProvider);
            doReturn(conProp).when(spyConnectionProvider).getConnectionProperty();

            connection = spyConnectionProvider.getConnection();
        }
        return connection;
    }
}
