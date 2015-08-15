package ifn372.sevencolors.backend.local_database_test_cases;

import org.junit.BeforeClass;
import org.junit.Test;

import ifn372.sevencolors.backend.dao.UserDao;
import static org.mockito.Mockito.spy;

public class UserCRUDTest extends LocalDatabaseTest{

    UserDao userDao;
    @BeforeClass
    public void setUp(){
        userDao = new UserDao();

        UserDao spyUserDao = spy(userDao);
    }

    @Test
    public void testInsertANewPatient() {

    }
}