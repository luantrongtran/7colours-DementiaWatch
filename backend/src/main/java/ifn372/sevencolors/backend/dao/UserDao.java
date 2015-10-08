package ifn372.sevencolors.backend.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.logging.Logger;

import ifn372.sevencolors.backend.HashProvider;
import ifn372.sevencolors.backend.entities.Carer;
import ifn372.sevencolors.backend.entities.Patient;
import ifn372.sevencolors.backend.webservices.UserEndpoint;

/**
 * Created by zach on 26/09/15.
 */
public class UserDao extends DAOBase {
    private static final Logger logger = Logger.getLogger(UserDao.class.getName());

    public static String tableName = "user";
    public static String colId = "id";
    public static String colFullName = "fullname";
    public static String colRoles = "roles";
    public static String colCarer = "carer_id";
    public static String colRegId = "reg_id";
    public static String colUserName = "username";
    public static String colPassword = "password";

    public Patient createPatient(Patient patient) {
        logger.info("UserDao class createPatient() method started.");
        try {
            if (insertUser(patient.getFullName(), patient.getRole(), patient.getUserName(), patient.getPassword(), patient.getCarer_id()) != -1) {
                logger.info("Patient created successfully.");
                logger.info("UserDao class createPatient() method ended.");
                return patient;
            } else {
                logger.info("Failed to create patient.");
                logger.info("UserDao class createPatient() method ended.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Carer createCarer(Carer carer) {
        logger.info("UserDao class createCarer() method started.");
        try {
            carer.setId(insertUser(carer.getFullName(), carer.getRole(), carer.getUserName(), carer.getPassword(), -1));
            if ((carer.getId() != -1) && updateUserAssignment(carer.getId(), carer.getPatientIds())) {
                logger.info("Carer created successfully.");
                logger.info("UserDao class createCarer() method ended.");
                return carer;
            } else {
                logger.info("Failed to create carer.");
                logger.info("UserDao class createCarer() method ended.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUserAssignment(int carer_id, List<String> patientIds) {
        logger.info("UserDao class updateUserAssignment() method started");
        String idList = "(";
        for (String id : patientIds) {
            idList += id;
            if (id != patientIds.get(patientIds.size() - 1)) {
                idList += ", ";
            }
        }
        idList += ")";
        Connection conn = null;
        PreparedStatement ps = null;
        try
        {
            conn = getConnection();
            String query = "UPDATE user SET carer_id = ? WHERE id IN " + idList;
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, carer_id);
            preparedStmt.executeUpdate();
            logger.info("UserDao class updateUserAssignment() method updated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to update user assignment.");
            return false;
        }
        finally
        {
            logger.info("UserDao class updateUserAssignment() method end.");
        }
        return true;
    }

    public Carer carerAuthentication(String userName, String password) {
        Carer carer = new Carer();
        HashProvider hashProvider = new HashProvider();
        password = hashProvider.encrypt(password);
        ResultSet rs = getUserEntry(userName);
        if (rs == null) {
            return null;
        } else {
            try {
                rs.next();
                if (password.equals(rs.getString(colPassword))) {
                    carer.setId(rs.getInt(colId));
                    carer.setFullName(rs.getString(colFullName));
                    carer.setRole(rs.getInt(colRoles));
                    carer.setGCMId(rs.getString(colRegId));
                    carer.setUserName(rs.getString(colUserName));
                } else {
                    logger.info("Failed to retrieve user: invalid password.");
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                logger.info(carer.getId() + ", " + carer.getFullName() + ", " + carer.getRole());
            }
            return carer;
        }
    }

    public Patient patientAuthentication(String userName, String password) {
        Patient patient = new Patient();
        HashProvider hashProvider = new HashProvider();
        password = hashProvider.encrypt(password);
        ResultSet rs = getUserEntry(userName);
        if (rs == null) {
            return null;
        } else {
            try {
                rs.next();
                if (password.equals(rs.getString(colPassword))) {
                    patient.setId(rs.getInt(colId));
                    patient.setFullName(rs.getString(colFullName));
                    patient.setRole(rs.getInt(colRoles));
                    patient.setCarer_id(rs.getInt(colCarer));
                    patient.setUserName(rs.getString(colUserName));
                } else {
                    logger.info("Failed to retrieve user: invalid password.");
                    return null;
                }
            } catch (Exception e) {
                return null;
            } finally {
                logger.info(patient.getId() + ", " + patient.getFullName() + ", " + patient.getRole());
            }
            return patient;
        }
    }

    public ResultSet getUserEntry(String userName) {
        Connection con = null;
        try
        {
            con = getConnection();
            String query = "SELECT * FROM user WHERE username = ?";
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, userName);
            ResultSet rs = preparedStmt.executeQuery();
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first element
            }
            if (rowcount > 0 && rowcount < 2) {
                return rs;
            } else {
                logger.info("Failed to retrieve user: invalid username.");
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Failed to retrieve user.");
            return null;
        }
    }

    public int insertUser(String fullName, int role, String userName, String password, int assignCarer)
    {
        logger.info("UserDao class createUser() method started");
        int createdUserId = UserEndpoint.CODE_ERR_CREATE_USER_FAILED;
        HashProvider hashProvider = new HashProvider();
        password = hashProvider.encrypt(password);

        if (getUserEntry(userName) == null) {
            Connection con = null;
            PreparedStatement ps = null;
            StringBuffer sql = new StringBuffer();
            sql.append("insert into " + tableName);
            sql.append(" (" + colFullName + ", " + colRoles + ", " + colCarer + ", " + colRegId + ", " + colUserName + ", " + colPassword + ")");
            sql.append(" values (?, ?, ?, ?, ?, ?) ");
            try {
                con = getConnection();
                ps = con.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, fullName);
                ps.setInt(2, role);
                if (assignCarer == -1) {
                    ps.setNull(3, Types.INTEGER);
                } else {
                    ps.setInt(3, assignCarer);
                }
                ps.setNull(4, Types.VARCHAR);
                ps.setString(5, userName);
                ps.setString(6, password);
                int num = ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    createdUserId = rs.getInt(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.print("Exception has occurred in createUser() method. Created User ID = " + createdUserId);
                return createdUserId;
            }
            logger.info("UserDao class createUser() method ended");
            return createdUserId;
        } else {
            logger.info("UserDao class createUser() method failed for repeating username.");
            return createdUserId;
        }
    }
}