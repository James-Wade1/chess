package serviceTests;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;
import service.LogoutService;

class LogoutServiceTest extends TestVariables {

    LogoutService myLogoutService = null;
    @BeforeEach
    void createSystemService() {
        myLogoutService = new LogoutService(myAuthDAO, myGameDAO, myUserDAO);
    }

    @Test
    void logoutUserSuccess() {
        try {
        myUserDAO.createUser(new UserData("username","password","email@email.com"));
        String authToken = myAuthDAO.createAuth("username").authToken();
            myLogoutService.logoutUser(authToken);

            Assertions.assertNull(myAuthDAO.getAuth(authToken));
        } catch (Exception ex) {
            Assertions.fail("Expected no error but got: " + ex);
        }
    }

    @Test
    void logoutUserFail() {
        int statusCode = 0;
        try {
            myUserDAO.createUser(new UserData("username","password","email@email.com"));
            String authToken = myAuthDAO.createAuth("username").authToken();
            myLogoutService.logoutUser(authToken.toUpperCase());
        } catch (ResponseException ex) {
            statusCode = ex.statusCode();
        }
        Assertions.assertEquals(statusCode,401, "Status code is not 401 unauthorized");
    }
}