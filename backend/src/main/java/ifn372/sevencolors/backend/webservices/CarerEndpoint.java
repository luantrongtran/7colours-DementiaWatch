package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.net.URLDecoder;

import javax.inject.Named;

import ifn372.sevencolors.backend.dao.CarerDao;
import ifn372.sevencolors.backend.entities.Carer;

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
public class CarerEndpoint {
    @ApiMethod(name = "updateGcmId")
    public Carer updateGcmId(@Named("carerId") int carerId, @Named("regId") String regId) {
        Carer carer = new Carer();
        carer.setId(carerId);
        try {
            CarerDao carerDao = new CarerDao();
            regId = URLDecoder.decode(regId, "UTF-8");
            carerDao.updateGCMId(carer, regId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return carer;
    }
}
