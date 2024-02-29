package daoTests;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;

import static org.junit.jupiter.api.Assertions.*;

class SQLUserDAOTest {

    SQLAuthDAO myAuthDAO;
    SQLUserDAO myUserDAO;
    SQLGameDAO myGameDAO;
    UserData exampleUser;

    @BeforeEach
    void setUp() {
        try {
            myAuthDAO = new SQLAuthDAO();
            myUserDAO = new SQLUserDAO();
            myGameDAO = new SQLGameDAO();
            myAuthDAO.clearAuthData();
            myGameDAO.clearGames();
            myUserDAO.clearUsers();

            exampleUser = new UserData("username", "password", "email@email.com");
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void createUserSuccess() {
        try {
            myUserDAO.createUser(exampleUser);
            UserData actual = myUserDAO.getUser(exampleUser.username());
            Assertions.assertEquals(exampleUser.username(), actual.username());
            Assertions.assertTrue(myUserDAO.verifyPassword(actual.password(), exampleUser.password()));
            Assertions.assertEquals(exampleUser.email(), actual.email());
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void createUserFail() {
        try {
            myUserDAO.createUser(exampleUser);
            Assertions.assertNull(myUserDAO.getUser(exampleUser.username().toUpperCase()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void getUserSuccess() {
        try {
            myUserDAO.createUser(exampleUser);
            UserData actual = myUserDAO.getUser(exampleUser.username());
            Assertions.assertEquals(exampleUser.username(), actual.username());
            Assertions.assertTrue(myUserDAO.verifyPassword(actual.password(), exampleUser.password()));
            Assertions.assertEquals(exampleUser.email(), actual.email());
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void getUserFail() {
        try {
            myUserDAO.createUser(exampleUser);
            Assertions.assertNull(myUserDAO.getUser(exampleUser.username().toUpperCase()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void clearUsers() {
        try {
            myUserDAO.createUser(exampleUser);
            myUserDAO.clearUsers();
            Assertions.assertNull(myUserDAO.getUser(exampleUser.username()));
        } catch(ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void verifyPasswordSuccess() {
        try {
            myUserDAO.createUser(exampleUser);
            Assertions.assertTrue(myUserDAO.verifyPassword(myUserDAO.getUser(exampleUser.username()).password(), exampleUser.password()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }

    @Test
    void verifyPasswordFail() {
        try {
            myUserDAO.createUser(exampleUser);
            Assertions.assertFalse(myUserDAO.verifyPassword(myUserDAO.getUser(exampleUser.username()).password(), exampleUser.password().toUpperCase()));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex);
        }
    }
}