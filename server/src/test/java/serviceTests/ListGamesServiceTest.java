package serviceTests;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
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
        int gameID1 = myGameDAO.createGame("Game 1");
        int gameID2 = myGameDAO.createGame("Game 2");
        AuthData authData = myAuthDAO.createAuth("username");
        HashSet<GameData> expected = new HashSet<GameData>();
        expected.add(myGameDAO.getGame(gameID1));
        expected.add(myGameDAO.getGame(gameID2));

        try {
            Assertions.assertEquals(expected,myListGamesService.listGames(authData.authToken()));

        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(), "");
        }
    }

    @Test
    void listGamesFail() {
        int gameID1 = myGameDAO.createGame("Game 1");
        int gameID2 = myGameDAO.createGame("Game 2");
        AuthData authData = myAuthDAO.createAuth("username");
        HashSet<GameData> expected = new HashSet<GameData>();
        expected.add(myGameDAO.getGame(gameID1));

        try {
            Assertions.assertNotEquals(expected,myListGamesService.listGames(authData.authToken()));

        } catch (ResponseException ex) {
            Assertions.assertEquals(ex.getMessage(), "");
        }
    }
}