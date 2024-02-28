package daoTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    SQLAuthDAO myAuthDao;
    SQLUserDAO myUserDao;
    @BeforeEach
    void setUp() {
        try {
            myAuthDao = new SQLAuthDAO();
            myUserDao = new SQLUserDAO();
            myAuthDao.clearAuthData();
            myUserDao.clearUsers();

            myUserDao.createUser(new UserData("username", "password", "email@email.com"));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void createAuthSuccess() {
        try {
            AuthData expected = myAuthDao.createAuth("username");
            AuthData actual = myAuthDao.getAuth(expected.authToken());

            Assertions.assertEquals(expected, actual);
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void createAuthFail() {
        try {
            AuthData expected = myAuthDao.createAuth("username");
            AuthData actual = myAuthDao.getAuth(expected.authToken().toUpperCase());

            Assertions.assertNotEquals(expected, actual);
        } catch (ResponseException ex) {
        Assertions.fail("Expected no error but got " + ex);
    }
    }

    @Test
    void getAuthSuccess() {
        try {
            AuthData expected = myAuthDao.createAuth("username");
            AuthData actual = myAuthDao.getAuth(expected.authToken());

            Assertions.assertEquals(expected, actual);
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void getAuthFail() {
        try {
        AuthData expected = myAuthDao.createAuth("username");
        Assertions.assertNull(myAuthDao.getAuth(expected.authToken().toUpperCase()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void deleteAuthSuccess() {
        try {
            AuthData auth1 = myAuthDao.createAuth("username1");
            AuthData auth2 = myAuthDao.createAuth("username2");
            myAuthDao.deleteAuth(auth1.authToken());
            Assertions.assertNull(myAuthDao.getAuth(auth1.authToken()));
            Assertions.assertNotNull(myAuthDao.getAuth(auth2.authToken()));
        } catch (DataAccessException | ResponseException e) {
            Assertions.fail("No error expected but got: " + e);
        }
    }

    @Test
    void deleteAuthFail() {
        try {
            AuthData auth1 = myAuthDao.createAuth("username1");
            Assertions.assertThrows(DataAccessException.class, () -> {
                myAuthDao.deleteAuth(auth1.authToken().toUpperCase());
            });
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void clearAuthDataSuccess() {
        try {
            AuthData auth1 = myAuthDao.createAuth("username1");
            AuthData auth2 = myAuthDao.createAuth("username2");
            AuthData auth3 = myAuthDao.createAuth("username3");
            myAuthDao.clearAuthData();

            Assertions.assertNull(myAuthDao.getAuth(auth1.authToken()));
            Assertions.assertNull(myAuthDao.getAuth(auth2.authToken()));
            Assertions.assertNull(myAuthDao.getAuth(auth3.authToken()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }
}