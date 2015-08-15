package ifn372.sevencolors.backend.dao;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import ifn372.sevencolors.backend.entities.Patient;

public class UserDao extends DAOBase {
    public void UpdateCurrentPosition(Patient patient) {
        Connection con = getConnection();
        String sql = "UPDATE user SET lat = ?, long = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setFloat(1, patient.getCurrentLocation().getLat());
            ps.setFloat(2, patient.getCurrentLocation().getLat());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}