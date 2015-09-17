package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.logging.Logger;

import javax.inject.Named;

import ifn372.sevencolors.backend.dao.FenceDao;
import ifn372.sevencolors.backend.entities.Fence;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.sevencolors.ifn372",
                ownerName = "backend.sevencolors.ifn372",
                packagePath = ""
        )
)
public class FenceEndpoint {

    private static final Logger logger = Logger.getLogger(FenceEndpoint.class.getName());

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_ERR_CREATE_FENCE_FAILED = -1;
    public static final int CODE_ERR_UPDATE_FENCE_FAILED = -2;
    public static final int CODE_ERR_DELETE_FENCE_FAILED = -3;

    /**
     * Create a fence.
     * Author: Koji
     * @param fenceName
     * @param address
     * @return
     */
    @ApiMethod(name = "createFence")
    public Fence createFence(@Named("userId") int userId, @Named("fenceName") String fenceName,
                                 @Named("lat") double lat, @Named("lon") double lon,
                                 @Named("radius") float radius, @Named("add") String address)
    {
        logger.info("createFence() method called");
        //System.out.println(fenceName);
        //System.out.println(address);
        Fence req = new Fence();
        req.setUserId(userId);
        req.setFenceName(fenceName);
        req.setLat(lat);
        req.setLon(lon);
        req.setRadius(radius);
        req.setAddress(address);

        Fence info = new Fence(false);
        try
        {
            FenceDao dao = new FenceDao();
            int id = dao.createFence(req);
            info.setSuccess(id != CODE_ERR_CREATE_FENCE_FAILED ? true : false);
            info.setFenceId(id);
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
        }

        return info;
    }

    /**
     * Update latitude and longitude of the fence by fence ID
     * Author: Koji
     * @param fenceId
     * @param lat
     * @param lon
     * @return
     */
    @ApiMethod(name = "updateFenceById")
    public Fence updateFenceById(@Named("fenceId") int fenceId, @Named("lat") double lat, @Named("lon") double lon)
    {
        logger.info("FenceEndpoint updateFenceById() method called");
        Fence info = new Fence();
//        info.setFenceId(new FenceDao().updateFenceById(fenceId, lat, lon));
//        return info;

        try
        {
            FenceDao fdao = new FenceDao();
            int ufs = fdao.updateFenceById(fenceId, lat, lon);
            info.setSuccess(ufs != CODE_ERR_UPDATE_FENCE_FAILED ? true : false);
            info.setFenceId(fenceId);
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
        }
        return info;
    }

    /**
     * Update latitude and longitude of the fence by fence ID
     * Author: Koji
     * @param fenceId
     * @return
     */
    @ApiMethod(name = "deleteFenceById")
    public Fence deleteFenceById(@Named("fenceId") int fenceId)
    {
        logger.info("FenceEndpoint deleteFenceById() method called");
        Fence info = new Fence();
        int code = new FenceDao().deleteFenceById(fenceId);
        info.setSuccess(code == CODE_SUCCESS ? true : false);
        return info;
    }
}