package ifn372.sevencolors.backend.dao;

import java.sql.Connection;

import ifn372.sevencolors.backend.connection.ConnectionProvider;

/**
 * Created by lua on 15/08/2015.
 */
public class DAOBase {
    private static Connection connection;

    public Connection getConnection() {
        if(connection == null) {
            ConnectionProvider conProvider = new ConnectionProvider();
            connection = conProvider.getConnection();
        }
        return connection;
    }
}
