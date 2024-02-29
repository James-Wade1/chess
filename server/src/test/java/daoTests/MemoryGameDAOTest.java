package daoTests;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemoryGameDAOTest {

    MemoryGameDAO myGameDAO;

    @BeforeEach
    void setUp() {
        myGameDAO = new MemoryGameDAO();
        myGameDAO.clearGames();
    }

    @Test
    void createGameSuccess() {
        int gameID = myGameDAO.createGame("game1");
        Assertions.assertNotNull(myGameDAO.getGame(gameID));
    }

    @Test
    void createGameFail() {
        int gameID1 = myGameDAO.createGame("game1");
        int gameID2 = myGameDAO.createGame("game1");
        Assertions.assertNotEquals(gameID1, gameID2);
    }

    @Test
    void getGameByNameSuccess() {
        String gameName = "game1";
        int gameID1 = myGameDAO.createGame(gameName);
        GameData expected = new GameData(gameID1, null, null, gameName, new ChessGame());
        Assertions.assertEquals(expected, myGameDAO.getGame(gameName));
    }

    @Test
    void getGameByNameFail() {
        String gameName = "game1";
        int gameID1 = myGameDAO.createGame(gameName);
        GameData expected = new GameData(gameID1, null, null, gameName, new ChessGame());
        Assertions.assertNull(myGameDAO.getGame(gameName.toUpperCase()));
    }

    @Test
    void getGameByIDSuccess() {
        String gameName = "game1";
        int gameID1 = myGameDAO.createGame(gameName);
        GameData expected = new GameData(gameID1, null, null, gameName, new ChessGame());
        Assertions.assertEquals(expected, myGameDAO.getGame(gameName));
        Assertions.assertEquals(expected, myGameDAO.getGame(gameID1));
    }

    @Test
    void getGameByIDFail() {
        String gameName = "game1";
        int gameID1 = myGameDAO.createGame(gameName);
        GameData expected = new GameData(gameID1, null, null, gameName, new ChessGame());
        Assertions.assertNull(myGameDAO.getGame(gameName.toUpperCase()));
        Assertions.assertNull(myGameDAO.getGame(gameID1+1));
    }

    @Test
    void listGamesSuccess() {
        String gameName1 = "game1";
        String gameName2 = "game2";
        String gameName3 = "game3";
        int gameID1 = myGameDAO.createGame(gameName1);
        int gameID2 = myGameDAO.createGame(gameName2);
        int gameID3 = myGameDAO.createGame(gameName3);

        GameData gameData1 = new GameData(gameID1, null, null, gameName1, new ChessGame());
        GameData gameData2 = new GameData(gameID2, null, null, gameName2, new ChessGame());
        GameData gameData3 = new GameData(gameID3, null, null, gameName3, new ChessGame());

        HashSet<GameData> expectedGamesList = new HashSet<>(List.of(gameData1, gameData2, gameData3));
        Assertions.assertEquals(expectedGamesList, myGameDAO.listGames());
    }

    @Test
    void listGamesFail() {
        String gameName1 = "game1";
        String gameName2 = "game2";
        String gameName3 = "game3";
        int gameID1 = myGameDAO.createGame(gameName1);
        int gameID2 = myGameDAO.createGame(gameName2);
        int gameID3 = myGameDAO.createGame(gameName3);

        GameData gameData1 = new GameData(gameID1+1, null, null, gameName1, new ChessGame());
        GameData gameData2 = new GameData(gameID2, null, null, gameName2, new ChessGame());
        GameData gameData3 = new GameData(gameID3, null, null, gameName3, new ChessGame());

        HashSet<GameData> expectedGamesList = new HashSet<>(List.of(gameData1, gameData2, gameData3));
        Assertions.assertNotEquals(expectedGamesList, myGameDAO.listGames());
    }

    @Test
    void updateGameSuccess() {
        try {
            String gameName = "game1";
            int gameID1 = myGameDAO.createGame(gameName);
            ChessGame game = new ChessGame();
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            game.setBoard(board);

            GameData expected = new GameData(gameID1, null, null, gameName, game);
            myGameDAO.updateGame(expected);
            Assertions.assertEquals(expected, myGameDAO.getGame(gameID1));
        } catch(DataAccessException ex) {
            Assertions.fail("Expected no error but received " + ex);
        }
    }

    @Test
    void updateGameFail() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            String gameName = "game1";
            int gameID = myGameDAO.createGame(gameName);
            myGameDAO.updateGame(new GameData(gameID+1, null, null, gameName, new ChessGame()));
        });
    }

    @Test
    void clearGames() {
        int gameID = myGameDAO.createGame("game1");
        Assertions.assertNotNull(myGameDAO.getGame(gameID));
        myGameDAO.clearGames();
        Assertions.assertNull(myGameDAO.getGame(gameID));
    }
}