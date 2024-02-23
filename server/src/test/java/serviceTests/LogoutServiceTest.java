package serviceTests;

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
        String authToken = myAuthDAO.createAuth("username").authToken();
        try {
            myLogoutService.logoutUser(authToken);
        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(),"");
        }

        Assertions.assertNull(myAuthDAO.getAuth(authToken));
    }

    @Test
    void logoutUserFail() {
        String authToken = myAuthDAO.createAuth("username").authToken();
        try {
            myLogoutService.logoutUser(authToken.toUpperCase());
        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.StatusCode(),401, "Status code is not 401 unauthorized");
        }
    }
}