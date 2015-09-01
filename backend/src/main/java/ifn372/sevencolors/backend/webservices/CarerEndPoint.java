package ifn372.sevencolors.backend.webservices;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.util.ArrayList;
import java.util.List;

import ifn372.sevencolors.backend.dao.FenceDao;
import ifn372.sevencolors.backend.dao.PatientDao;
import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.connection.ConnectionProvider;
import ifn372.sevencolors.backend.entities.Fence;
import ifn372.sevencolors.backend.entities.Location;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "carerApi",
        version = "v1",
        resource = "carer",
        namespace = @ApiNamespace(
                ownerDomain = "backend.sevencolors.ifn372",
                ownerName = "backend.sevencolors.ifn372",
                packagePath = ""
        )
)

public class CarerEndPoint {

    /**
     * Created by Zachary
     * Receives outOfBound-checking request from carer's application
     * @param carer The carer object to be updated
     * @return carer Same carer object with updated patient safety property
     */
    @ApiMethod (name = "outOfBoundCheck")
    public Carer outOfBoundCheck(Carer carer) {
        carer.setPatientIds();
        List<String> patientIds = carer.getPatientIds();
        FenceDao fenceDao = new FenceDao();
        PatientDao patientDao = new PatientDao();
        boolean patientSafety = true;
        for (String patientId : patientIds) {
            int pId = Integer.parseInt(patientId);
            Location location = patientDao.getPatientLocation(pId);
            double patientLat = location.getLat();
            double patientLon = location.getLon();
            List<Fence> fences = fenceDao.getFences(pId);
            for (Fence fence : fences) {
                double fenceLat = fence.getLat();
                double fenceLon = fence.getLon();
                float fenceRadius = fence.getRadius();
                Boolean inFence = ((patientLat - fenceLat) * (patientLat - fenceLat) + (patientLon - fenceLon) * (patientLon - fenceLon)) < (fenceRadius * fenceRadius);
                if (!inFence) {
                    patientSafety = false;
                }
            }
        }
        carer.setPatientSafety(patientSafety);
        return carer;
    }
}