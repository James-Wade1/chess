package serviceTests;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;
import service.RegisterService;
import service.SystemService;

class RegisterServiceTest extends TestVariables {

    RegisterService myRegisterService = null;
    @BeforeEach
    void createSystemService() {
        myRegisterService = new RegisterService(myAuthDAO, myGameDAO, myUserDAO);
    }
    @Test
    void registerUserSuccess() {
        UserData newUser = new UserData("username", "password","email");
        try {
            myRegisterService.registerUser(newUser);
            Assertions.assertEquals(newUser, myUserDAO.getUser(newUser.username()));
        } catch (ResponseException ex) {
            Assertions.assertEquals(1, 0);
        }

    }

    @Test
    void registerUserFail() {
        UserData newUser = new UserData("username", "password",null);
        try {
            myRegisterService.registerUser(newUser);
            Assertions.assertEquals(newUser, myUserDAO.getUser(newUser.username()));
        } catch (ResponseException ex) {
            Assertions.assertEquals(400, ex.StatusCode(), "Status code was not 400 bad request");
        }

    }
}