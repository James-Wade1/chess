package serviceTests;

import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;
import service.CreateGameService;

class CreateGameServiceTest extends TestVariables{

    CreateGameService myCreateGameService = null;
    @BeforeEach
    void createSystemService() {
        myCreateGameService = new CreateGameService(myAuthDAO, myGameDAO, myUserDAO);
    }

    @Test
    void createGameSuccess() {
        try {
            myUserDAO.createUser(new UserData("username","password","email@email.com"));
            String authToken = myAuthDAO.createAuth("username").authToken();
            int gameID = myCreateGameService.createGame(authToken, "Game1");
            GameData actual = myGameDAO.getGame(gameID);
            Assertions.assertNotNull(actual);
        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(), "");
        }
    }

    @Test
    void createGameFail() {
        int statusCode = 0;
        try {
            myUserDAO.createUser(new UserData("username","password","email@email.com"));
            String authToken = myAuthDAO.createAuth("username").authToken();
            int gameID1 = myCreateGameService.createGame(authToken, "Game1");
            int gameID2 = myCreateGameService.createGame(authToken, "Game1");
        } catch (ResponseException ex) {
            statusCode = ex.statusCode();
        }
        Assertions.assertEquals(statusCode, 400, "Status code not 400 bad request");
    }
}