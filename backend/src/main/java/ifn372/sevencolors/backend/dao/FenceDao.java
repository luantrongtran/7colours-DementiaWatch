package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ifn372.sevencolors.backend.entities.Fence;

/**
 * Created by Kirti on 31/8/2015.
 */
public class FenceDao extends DAOBase {

    public FenceDao(){}
    private static final Logger logger = Logger.getLogger(FenceDao.class.getName());
    public static String TABLE_NAME = "fence";
    public static String COL_NAME_ID = "id";
    public static String COL_NAME_USER_ID= "user_id";
    public static String COL_NAME_RADIUS = "fence_radius";
    public static String COL_NAME_ADDRESS = "address";
    public static String COL_NAME_LAT = "lat";
    public static String COL_NAME_LON = "lon";
    public static String COL_NAME_FENCE_NAME = "fence_name";

    public Fence findById(int id) {
        Connection con = getConnection();
        Fence fence = null;
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " +COL_NAME_ID + " = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fence = new Fence();
                fence.setUserId(rs.getInt(COL_NAME_USER_ID));
                fence.setAddress(rs.getString(COL_NAME_ADDRESS));
                fence.setFenceName(rs.getString(COL_NAME_FENCE_NAME));
                fence.setRadius(rs.getFloat(COL_NAME_RADIUS));
                fence.setLat(rs.getFloat(COL_NAME_LAT));
                fence.setLon(rs.getFloat(COL_NAME_LON));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fence;
    }
    public int createFence(Fence info)
    {
        logger.info("FenceDao class createFence() method starts.....");
        Connection con = null;
        //boolean ret = false;
        int insertedRecordId = -1;
        try
        {
            con = getConnection();
            //StringBuffer sql = new StringBuffer("insert into fence (user_id, fence_name, lat, lon, fence_radius, address) ");
            //sql.append(" values (?, ?, ?, ?, ?, ?) ");
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO " + TABLE_NAME);
            sql.append(" (" + COL_NAME_USER_ID + ", " + COL_NAME_FENCE_NAME+ ", " + COL_NAME_LAT);
            sql.append(", " + COL_NAME_LON + ", " + COL_NAME_RADIUS + ", " + COL_NAME_ADDRESS + ") ");
            sql.append(" VALUES(?,?,?,?,?,?)");

            PreparedStatement ps = con.prepareStatement(sql.toString());
            ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, info.getUserId());
            ps.setString(2, info.getFenceName());
            ps.setDouble(3, info.getLat());
            ps.setDouble(4, info.getLon());
            ps.setFloat(5, info.getRadius());
            ps.setString(6, info.getAddress());

            int num = ps.executeUpdate();

            //ret = num == 1 ? true : false;
            ResultSet rs = ps.getGeneratedKeys();
            if(rs != null && rs.next())
            {
                insertedRecordId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally
        {
            logger.info("FenceDao class createFence() method end.");
        }
        //return ret;
        return insertedRecordId;
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
