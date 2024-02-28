package daoTests;

import dataAccess.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;

import static org.junit.jupiter.api.Assertions.*;

class MemoryUserDAOTest {

    MemoryUserDAO myUserDAO;
    UserData exampleUser;

    @BeforeEach
    void setUp() {
        myUserDAO = new MemoryUserDAO();
        myUserDAO.clearUsers();
        exampleUser = new UserData("username", "password", "email@email.com");
    }

    @Test
    void createUserSuccess() {
        try {
            myUserDAO.createUser(exampleUser);
            Assertions.assertEquals(exampleUser, myUserDAO.getUser(exampleUser.username()));
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
            Assertions.assertEquals(exampleUser, myUserDAO.getUser(exampleUser.username()));
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
        Assertions.assertTrue(myUserDAO.verifyPassword(exampleUser.password(), "password"));
    }

    @Test
    void verifyPasswordFail() {
        Assertions.assertFalse(myUserDAO.verifyPassword(exampleUser.password(), "password".toUpperCase()));
    }
}