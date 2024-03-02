package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MemoryAuthDAOTest {

    MemoryAuthDAO myAuthDao;
    @BeforeEach
    void setUp() {
        myAuthDao = new MemoryAuthDAO();
        myAuthDao.clearAuthData();
    }

    @Test
    void createAuthSuccess() {
        AuthData expected = myAuthDao.createAuth("username");
        AuthData actual = myAuthDao.getAuth(expected.authToken());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void createAuthFail() {
        AuthData expected = myAuthDao.createAuth("username");
        AuthData actual = myAuthDao.getAuth(expected.authToken().toUpperCase());

        Assertions.assertNotEquals(expected, actual);
    }

    @Test
    void getAuthSuccess() {
        AuthData expected = myAuthDao.createAuth("username");
        AuthData actual = myAuthDao.getAuth(expected.authToken());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getAuthFail() {
        AuthData expected = myAuthDao.createAuth("username");
        Assertions.assertNull(myAuthDao.getAuth(expected.authToken().toUpperCase()));
    }

    @Test
    void deleteAuthSuccess() {
        try {
            AuthData auth1 = myAuthDao.createAuth("username1");
            AuthData auth2 = myAuthDao.createAuth("username2");
            myAuthDao.deleteAuth(auth1.authToken());
            Assertions.assertNull(myAuthDao.getAuth(auth1.authToken()));
            Assertions.assertNotNull(myAuthDao.getAuth(auth2.authToken()));
        } catch (DataAccessException e) {
            Assertions.fail("No error expected but got: " + e);
        }
    }

    @Test
    void deleteAuthFail() {
        AuthData auth1 = myAuthDao.createAuth("username1");
        Assertions.assertThrows(DataAccessException.class, () -> {myAuthDao.deleteAuth(auth1.authToken().toUpperCase());} );
    }

    @Test
    void clearAuthDataSuccess() {
        AuthData auth1 = myAuthDao.createAuth("username1");
        AuthData auth2 = myAuthDao.createAuth("username2");
        AuthData auth3 = myAuthDao.createAuth("username3");
        myAuthDao.clearAuthData();

        Assertions.assertNull(myAuthDao.getAuth(auth1.authToken()));
        Assertions.assertNull(myAuthDao.getAuth(auth2.authToken()));
        Assertions.assertNull(myAuthDao.getAuth(auth3.authToken()));
    }
}