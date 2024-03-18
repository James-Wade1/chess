package clientTests;

import ServerFacade.ServerFacade;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import responseException.ResponseException;
import model.GameResponseClass;
import model.PlayerJoinRequest;
import server.Server;

import java.util.HashSet;


public class ServerFacadeTests {

    private static Server server;

    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://localhost:%d", port));
    }

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> facade.delete());
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void registerUserSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
    }

    @Test
    void registerUserFail() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertThrows(ResponseException.class, () -> facade.registerUser(new UserData("user1", "pass1", "email")));
    }

    @Test
    void loginUserSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
    }

    @Test
    void loginUserFail() {
        Assertions.assertThrows(ResponseException.class, () -> facade.loginUser(new UserData("user1", "pass1", null)));
    }

    @Test
    void logoutUserSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.logoutUser());
    }

    @Test
    void logoutUserFail() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertThrows(ResponseException.class, () -> facade.logoutUser());

        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.logoutUser());
        Assertions.assertThrows(ResponseException.class, () -> facade.logoutUser());
    }

    @Test
    void createGameSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
    }

    @Test
    void createGameFail() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
        Assertions.assertThrows(ResponseException.class, () -> facade.createGame(new GameData(-1, null, null, "game1", null)));
    }

    @Test
    void listGamesSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
        try {
            HashSet<GameResponseClass.GameList> games = facade.listGames().getGames();
            for (var game : games) {
                Assertions.assertNull(game.blackUsername());
                Assertions.assertNull(game.whiteUsername());
                Assertions.assertTrue(game.gameID() > 0, "gameID is not default value");
                Assertions.assertEquals("game1", game.gameName());
            }
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex.getMessage());
        }
    }

    @Test
    void listGamesFail() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
        try {
            HashSet<GameResponseClass.GameList> games = facade.listGames().getGames();
            for (var game : games) {
                Assertions.assertNull(game.blackUsername());
                Assertions.assertNull(game.whiteUsername());
                Assertions.assertTrue(game.gameID() > 0, "gameID is not default value");
                Assertions.assertNotEquals("game2", game.gameName());
            }
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex.getMessage());
        }
    }

    @Test
    void joinGameSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
        try {
            HashSet<GameResponseClass.GameList> games = facade.listGames().getGames();
            int gameID = 0;
            for (var game : games) {
                gameID = game.gameID();
            }

            int finalGameID = gameID;
            Assertions.assertDoesNotThrow(() -> facade.joinGame(new PlayerJoinRequest("WHITE", finalGameID)));
            games = facade.listGames().getGames();
            for (var game : games) {
                Assertions.assertEquals("user1", game.whiteUsername());
            }

            Assertions.assertDoesNotThrow(() -> facade.joinGame(new PlayerJoinRequest(null, finalGameID)));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex.getMessage());
        }
    }

    @Test
    void joinGameFail() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
        try {
            HashSet<GameResponseClass.GameList> games = facade.listGames().getGames();
            int gameID = 0;
            for (var game : games) {
                gameID = game.gameID();
            }

            int finalGameID = gameID;
            Assertions.assertDoesNotThrow(() -> facade.joinGame(new PlayerJoinRequest("WHITE", finalGameID)));
            Assertions.assertThrows(ResponseException.class, () -> facade.joinGame(new PlayerJoinRequest("WHITE", finalGameID)));
        } catch (ResponseException ex) {
            Assertions.fail("Expected no error but got " + ex.getMessage());
        }
    }

    @Test
    void deleteSuccess() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
        Assertions.assertDoesNotThrow(() -> facade.delete());
    }

    @Test
    void deleteFail() {
        Assertions.assertDoesNotThrow(() -> facade.registerUser(new UserData("user1", "pass1", "email")));
        Assertions.assertDoesNotThrow(() -> facade.loginUser(new UserData("user1", "pass1", null)));
        Assertions.assertDoesNotThrow(() -> facade.createGame(new GameData(-1, null, null, "game1", null)));
        Assertions.assertDoesNotThrow(() -> facade.delete());

        Assertions.assertThrows(ResponseException.class, () -> facade.loginUser(new UserData("user1", "pass1", null)));
    }

}