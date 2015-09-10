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

    /**
     *
     * @param fenceName
     * @param address
     * @return
     */
    @ApiMethod(name = "createFence")
    public Fence createFence(@Named("userId") String userId, @Named("fenceName") String fenceName,
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
            info.setSuccess(dao.createFence(req));
        }
        catch(Exception exp)
        {
            exp.printStackTrace();
        }

        return info;
    }
}