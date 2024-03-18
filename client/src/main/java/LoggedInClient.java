import responseException.ResponseException;
import ui.UIException;

import java.util.Arrays;

public class LoggedInClient {

    ServerFacade server;

    public LoggedInClient(ServerFacade server) {
        this.server = server;
    }

    public String eval(String userInput) throws ResponseException, UIException {
        var tokens = userInput.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "Help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "Help" -> help();
            case "Logout" -> logout(params);
            case "CreateGame" -> createGame(params);
            case "ListGames" -> listGames(params);
            case "JoinGame" -> joinGame(params);
            case "JoinObserver" -> joinObserver(params);
            case "Delete" -> delete(params);
            default -> "Unknown command. Please try again";
        };
    }

    public String help() {
        return """
                - Help
                - Logout
                - CreateGame
                - ListGames
                - JoinGame
                - JoinObserver
                - Delete
                """;
    }
    private String logout(String... params) throws UIException, ResponseException {
        if (params.length == 0) {
            server.logout();
            return "User logged out";
        }
        throw new UIException("Expected: Logout");
    }

    private String createGame(String... params) {
        return "";
    }

    private String listGames(String... params) {
        return "";
    }

    private String joinGame(String... params) {
        return "";
    }

    private String joinObserver(String... params) {
        return "";
    }

    private String delete(String... params) throws UIException, ResponseException {
        if (params.length == 0) {
            server.delete();
            return "Deleted all data";
        }
        throw new UIException("Expected: Delete");
    }
}
