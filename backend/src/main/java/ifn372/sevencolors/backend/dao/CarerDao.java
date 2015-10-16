package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
    public CarerDao() {
    }

    private static final Logger logger = Logger.getLogger(CarerDao.class.getName());

    /**
     * Created by Zachary Tang
     *
     * @param carerId The ID of the carer
     * @return patientIds The list of patient IDs related to the carer
     */
    public List<String> getPatientIds(int carerId) {
        //logger.info("CarerDao class getPatients() method starts.....");
        List<String> patientIds = new ArrayList<String>();
        Connection con = null;
        Statement stmt = null;
        try {
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
            while (rs.next()) {
                String pid = rs.getString("id");
                patientIds.add(pid); // add patient to the list
                logger.info("Patient ID: " + pid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            logger.info("CarerDao class getPatients() method end.");
        }
        return patientIds;
    }

    /**
     * Created by Zachary Tang
     *
     * @param carer The carer to update GCM ID
     * @param ID    The GCM ID
     * @return ID The GCM ID inserted into the database
     */
    public String updateGCMId(Carer carer, String ID) {
        logger.info("CarerDao class updateGCMId() method starts.....");
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getConnection();
            logger.info("CarerDao class updateGCMId() method RegID: " + ID);
            String query = "UPDATE user SET reg_id = ? WHERE id = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, ID);
            preparedStmt.setInt(2, carer.getId());

            preparedStmt.executeUpdate();
            logger.info("CarerDao class updateGCMId() method updated successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            logger.info("CarerDao class updateGCMId() method end.");
        }
        return ID;
    }

    public Carer getCarerById(int carerId) {
        Connection con = null;
        Statement stmt = null;
        Carer carer = new Carer();
        carer.setId(carerId);
        try {
            con = getConnection();

            // get carer ID
            stmt = con.createStatement();
            String sql = "SELECT * FROM user WHERE id=" + carerId;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                carer.setId(rs.getInt("id"));
                carer.setFullName(rs.getString("fullname"));
                carer.setRole(rs.getInt("roles"));
                carer.setGCMId(rs.getString("reg_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carer;
    }

    public void removeGcmId(int userId) {
        String sql = "UPDATE user SET " + " reg_id = '' WHERE id = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setInt(1, userId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}