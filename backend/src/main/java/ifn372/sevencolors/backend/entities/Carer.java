package ifn372.sevencolors.backend.entities;

import java.util.ArrayList;
import java.util.List;

import ifn372.sevencolors.backend.dao.CarerDao;

/**
 * Created by zach on 28/08/15.
 */
public class Carer extends User {

    boolean patientSafety;

    private List<String> patientIds;

    public Carer() {
    }

    public Carer(boolean patientSafety)
    {
        this.patientSafety = patientSafety;
    }

    public boolean getPatientSafety() {
        return patientSafety;
    }

    public void setPatientSafety(boolean patientSafe) {
        this.patientSafety = patientSafe;
    }

    public void setPatientIds()
    {
        CarerDao carerDao = new CarerDao();
        patientIds = carerDao.getPatientIds(this.getId());
    }

    public List<String> getPatientIds()
    {
        return patientIds;
    }
}
