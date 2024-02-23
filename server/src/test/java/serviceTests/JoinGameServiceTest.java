package serviceTests;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;
import service.JoinGameService;

class JoinGameServiceTest extends TestVariables {
    JoinGameService myJoinGameService = null;
    @BeforeEach
    void createSystemService() {
        myJoinGameService = new JoinGameService(myAuthDAO, myGameDAO, myUserDAO);
    }
    @Test
    void joinGameSuccess() {
        int gameID = myGameDAO.createGame("Game1");
        AuthData authData = myAuthDAO.createAuth("username");

        try {
            myJoinGameService.joinGame(authData.authToken(), "WHITE", gameID);
            GameData updatedGame = myGameDAO.getGame(gameID);
            Assertions.assertEquals("username", updatedGame.whiteUsername());
        } catch(ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(), "");
        }
    }

    @Test
    void joinGameFail() {
        int gameID = myGameDAO.createGame("Game1");
        AuthData authData = myAuthDAO.createAuth("username");
        int statusCode = 0;

        try {
            myJoinGameService.joinGame(authData.authToken(), "WHITE", gameID);
            myJoinGameService.joinGame(authData.authToken(), "WHITE", gameID);
        } catch(ResponseException ex) {
            statusCode = ex.StatusCode();
        }
        Assertions.assertEquals(statusCode, 403, "Status code not 403 already taken");
    }
}