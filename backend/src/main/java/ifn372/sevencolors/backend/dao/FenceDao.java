package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.sql.Statement;

import ifn372.sevencolors.backend.entities.Fence;

/**
 * Created by koji on 2015/08/27.
 */
public class FenceDao extends DAOBase {

    public FenceDao(){}
    private static final Logger logger = Logger.getLogger(FenceDao.class.getName());

    public boolean createFence(Fence info)
    {
        logger.info("FenceDao class createFence() method starts.....");
        Connection con = null;
        boolean ret = false;
        try
        {
            con = getConnection();
            StringBuffer sql = new StringBuffer("insert into fence (user_id, fence_name, lat, lon, fence_radius, address) ");
            sql.append(" values (?, ?, ?, ?, ?, ?) ");
            PreparedStatement ps = con.prepareStatement(sql.toString());

            ps.setString(1, info.getUserId());
            ps.setString(2, info.getFenceName());
            ps.setDouble(3, info.getLat());
            ps.setDouble(4, info.getLon());
            ps.setFloat(5, info.getRadius());
            ps.setString(6, info.getAddress());

            int num = ps.executeUpdate();

            ret = num == 1 ? true : false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            logger.info("FenceDao class createFence() method end.");
        }
        return ret;
    }

    /**
     * Created by Zachary Tang
     * @param patientId The ID of the patient
     * @return fences An ArrayList of fences related to the patient
     */
    public List<Fence> getFences(int patientId)
    {
        logger.info("FenceDao class getFence() method starts.....");
        List<Fence> fences = new ArrayList<Fence>();
        Connection con = null;
        Statement stmt = null;
        try
        {
            con = getConnection();
            stmt = con.createStatement();
            String sql = "SELECT * FROM fence WHERE user_id=" + String.valueOf(patientId);
            ResultSet rs = stmt.executeQuery(sql);

            // set fence data
            while(rs.next())
            {
                Fence fence = new Fence(); // temporal fence to be stored
                fence.setFenceName(rs.getString("fence_name"));
                fence.setLat(rs.getDouble("lat"));
                fence.setLon(rs.getDouble("lon"));
                fence.setRadius(rs.getFloat("fence_radius"));
                fence.setAddress(rs.getString("address"));

                fences.add(fence); // add fence to the list
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            logger.info("FenceDao class getFence() method end.");
        }
        return fences;
    }
}
