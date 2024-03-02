package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;

class SQLAuthDAOTest {

    SQLAuthDAO myAuthDAO;
    SQLUserDAO myUserDAO;
    SQLGameDAO myGameDAO;
    @BeforeEach
    void setUp() {
        try {
            myAuthDAO = new SQLAuthDAO();
            myUserDAO = new SQLUserDAO();
            myGameDAO = new SQLGameDAO();
            myAuthDAO.clearAuthData();
            myGameDAO.clearGames();
            myUserDAO.clearUsers();

            myUserDAO.createUser(new UserData("username1", "password1", "email1@email.com"));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void createAuthSuccess() {
        try {
            AuthData expected = myAuthDAO.createAuth("username1");
            AuthData actual = myAuthDAO.getAuth(expected.authToken());

            Assertions.assertEquals(expected, actual);
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void createAuthFail() {
        try {
            AuthData expected = myAuthDAO.createAuth("username1");
            AuthData actual = myAuthDAO.getAuth(expected.authToken().toUpperCase());

            Assertions.assertNotEquals(expected, actual);
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void getAuthSuccess() {
        try {
            AuthData expected = myAuthDAO.createAuth("username1");
            AuthData actual = myAuthDAO.getAuth(expected.authToken());

            Assertions.assertEquals(expected, actual);
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void getAuthFail() {
        try {
            AuthData expected = myAuthDAO.createAuth("username1");
            Assertions.assertNull(myAuthDAO.getAuth(expected.authToken().toUpperCase()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void deleteAuthSuccess() {
        try {
            AuthData auth1 = myAuthDAO.createAuth("username1");

            myUserDAO.createUser(new UserData("username2", "password2", "email2@email.com"));
            AuthData auth2 = myAuthDAO.createAuth("username2");

            myAuthDAO.deleteAuth(auth1.authToken());
            Assertions.assertNull(myAuthDAO.getAuth(auth1.authToken()));
            Assertions.assertNotNull(myAuthDAO.getAuth(auth2.authToken()));
        } catch (DataAccessException | ResponseException e) {
            Assertions.fail("No error expected but got: " + e);
        }
    }

    @Test
    void deleteAuthFail() {
        try {
            AuthData auth1 = myAuthDAO.createAuth("username1");
            Assertions.assertThrows(DataAccessException.class, () -> {
                myAuthDAO.deleteAuth(auth1.authToken().toUpperCase());
            });
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void clearAuthDataSuccess() {
        try {
            AuthData auth1 = myAuthDAO.createAuth("username1");

            myUserDAO.createUser(new UserData("username2", "password2", "email2@email.com"));
            AuthData auth2 = myAuthDAO.createAuth("username2");

            myUserDAO.createUser(new UserData("username3", "password3", "email3@email.com"));
            AuthData auth3 = myAuthDAO.createAuth("username3");
            myAuthDAO.clearAuthData();

            Assertions.assertNull(myAuthDAO.getAuth(auth1.authToken()));
            Assertions.assertNull(myAuthDAO.getAuth(auth2.authToken()));
            Assertions.assertNull(myAuthDAO.getAuth(auth3.authToken()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }
}