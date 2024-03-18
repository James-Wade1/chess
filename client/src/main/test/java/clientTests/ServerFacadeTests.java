package clientTests;

import ServerFacade.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;


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

    }

    @Test
    void registerUserFail() {

    }

    @Test
    void loginUser() {
    }

    @Test
    void logoutUser() {
    }

    @Test
    void createGame() {
    }

    @Test
    void listGames() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void delete() {
    }

}