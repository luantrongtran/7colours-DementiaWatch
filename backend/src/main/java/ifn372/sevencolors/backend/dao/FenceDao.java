package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

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
            StringBuffer sql = new StringBuffer("insert into fence (user_id, fence_name, lat, lon, radius, address) ");
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


}
