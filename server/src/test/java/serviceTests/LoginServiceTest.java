package serviceTests;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;
import service.LoginService;

class LoginServiceTest extends TestVariables{

    LoginService myLoginService = null;
    @BeforeEach
    void createSystemService() {
        myLoginService = new LoginService(myAuthDAO, myGameDAO, myUserDAO);
    }
    @Test
    void loginUserSuccess() {
        UserData user = new UserData("username", "password", "email@gmail.com");
        myUserDAO.createUser(user);
        try {
            AuthData newAuthData = myLoginService.loginUser(user);
            Assertions.assertNotNull(newAuthData);
        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(), "");
        }
    }
    @Test
    void loginUserFail() {
        int statusCode = 0;
        UserData user = new UserData("username", "password", "email@gmail.com");
        myUserDAO.createUser(user);
        try {
            AuthData newAuthData = myLoginService.loginUser(new UserData("username", "pass", "email@gmail.com"));
            Assertions.assertNotNull(newAuthData);
        } catch (ResponseException ex) {
            statusCode = ex.statusCode();
        }
        Assertions.assertEquals(statusCode, 401, "Status is not 401 unauthorized");
    }
}