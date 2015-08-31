package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.entities.Patient;

/**
 * Created by zach on 28/08/15.
 */
public class CarerDao extends DAOBase {
    public CarerDao(){}
    private static final Logger logger = Logger.getLogger(CarerDao.class.getName());

    /**
     * Created by Zachary Tang
     * @param carerId The ID of the carer
     * @return patientIds The list of patient IDs related to the carer
     */
    public List<String> getPatientIds(int carerId)
    {
        //logger.info("CarerDao class getPatients() method starts.....");
        List<String> patientIds = new ArrayList<String>();
        Connection con = null;
        Statement stmt = null;
        try
        {
            con = getConnection();
            stmt = con.createStatement();
            //logger.info("CarerDao class getPatients() method carer: " + String.valueOf(carerId));
            String sql = "SELECT * FROM user WHERE carer_id=" + String.valueOf(carerId);
            ResultSet rs = stmt.executeQuery(sql);

            /* for testing row count
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            logger.info("CarerDao class getPatients() method count:" + String.valueOf(rowcount));*/

            // add patient id to list
            while(rs.next())
            {
                String pid = rs.getString("id");
                patientIds.add(pid); // add patient to the list
                logger.info("Patient ID: " + pid);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            logger.info("CarerDao class getPatients() method end.");
        }
        return patientIds;
    }
}
