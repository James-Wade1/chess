package serviceTests;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responseException.ResponseException;
import service.ListGamesService;

import java.util.HashSet;

class ListGamesServiceTest extends TestVariables {
    ListGamesService myListGamesService = null;
    @BeforeEach
    void createSystemService() {
        myListGamesService = new ListGamesService(myAuthDAO, myGameDAO, myUserDAO);
    }
    @Test
    void listGamesSuccess() {
        try {
            int gameID1 = myGameDAO.createGame("Game 1");
            int gameID2 = myGameDAO.createGame("Game 2");
            myUserDAO.createUser(new UserData("username","password","email@email.com"));
            AuthData authData = myAuthDAO.createAuth("username");
            HashSet<GameData> expected = new HashSet<GameData>();
            expected.add(myGameDAO.getGame(gameID1));
            expected.add(myGameDAO.getGame(gameID2));
            Assertions.assertEquals(expected,myListGamesService.listGames(authData.authToken()));

        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(), "");
        }
    }

    @Test
    void listGamesFail() {
        try {
            int gameID1 = myGameDAO.createGame("Game 1");
            int gameID2 = myGameDAO.createGame("Game 2");
            myUserDAO.createUser(new UserData("username","password","email@email.com"));
            AuthData authData = myAuthDAO.createAuth("username");
            HashSet<GameData> expected = new HashSet<GameData>();
            expected.add(myGameDAO.getGame(gameID1));

            Assertions.assertNotEquals(expected,myListGamesService.listGames(authData.authToken()));

        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(), "");
        }
    }
}