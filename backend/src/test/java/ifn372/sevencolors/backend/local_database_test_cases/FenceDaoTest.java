package ifn372.sevencolors.backend.local_database_test_cases;

/**
 * Created by Kirti on 31/8/2015.
 */

import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ifn372.sevencolors.backend.dao.FenceDao;
import ifn372.sevencolors.backend.entities.Fence;
import ifn372.sevencolors.backend.webservices.FenceEndpoint;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class FenceDaoTest extends LocalDatabaseTest{

    FenceDao fenceDao;
    FenceDao spyFenceDao;

    @Before
    public void setUp(){
        fenceDao = new FenceDao();
        spyFenceDao = spy(fenceDao);
        doReturn(getSpiedConnectionProvider()).when(spyFenceDao).getConnection();
    }


    public Fence findById(int id) {
        Connection con = spyFenceDao.getConnection();
        Fence fence = null;
        String sql = "SELECT * FROM " + FenceDao.TABLE_NAME + " WHERE " + FenceDao.COL_NAME_ID + " = ?";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fence = new Fence();
                fence.setUserId(rs.getInt(FenceDao.COL_NAME_USER_ID));
                fence.setAddress(rs.getString(FenceDao.COL_NAME_ADDRESS));
                fence.setFenceName(rs.getString(FenceDao.COL_NAME_FENCE_NAME));
                fence.setRadius(rs.getFloat(FenceDao.COL_NAME_RADIUS));
                fence.setLat(rs.getFloat(FenceDao.COL_NAME_LAT));
                fence.setLon(rs.getFloat(FenceDao.COL_NAME_LON));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fence;
    }


    @Test
    public void testInsertANewFence() {
        Fence mockFence = mock(Fence.class);
        when(mockFence.getFenceName()).thenReturn("KIRTI");
        when(mockFence.getUserId()).thenReturn(2);
        when(mockFence.getLon()).thenReturn(128.76d);
        when(mockFence.getLat()).thenReturn(110.50d);
        when(mockFence.getRadius()).thenReturn(5f);
        when(mockFence.getAddress()).thenReturn("QUT Gardens point");

        int fenceId = spyFenceDao.createFence(mockFence);

        Fence f = this.findById(fenceId);//get the insterted from the database
        assertNotNull("The fence should not be null", f);

        assertEquals("Address", f.getAddress(), "QUT Gardens point");
        assertEquals("Fence Name", f.getFenceName(), "KIRTI");
        assertEquals("User Id", f.getUserId(), 2);
        assertEquals("Longitude", f.getLon(), 128.76, 0.001);
        assertEquals("Latitude", f.getLat(), 110.50, 0.0);
        assertEquals("Fence Radius", f.getRadius(), 5.01, 0.01);

    }

    /**
     * Test updateFenceById method()
     */
    @Test
    public void testUpdateFenceById() {
        int fenceId = 6;
        double lat = 200;
        double lon = 300;
        int id = spyFenceDao.updateFenceById(fenceId, lat, lon);
        Fence fence = this.findById(fenceId);
        assertThat("Fence ID", id, is(fenceId));
        assertThat("Latitude", lat, is(lat));
        assertThat("Longitude", lon, is(lon));
    }


    /**
     * Test deleteFenceById method()
     */
    @Test
    public void testDeleteFenceById() {
        int fenceId = 6;
        int code = spyFenceDao.deleteFenceById(fenceId);
        assertThat("Success", code, is(FenceEndpoint.CODE_SUCCESS));
    }
}

