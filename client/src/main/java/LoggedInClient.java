import model.GameData;
import responseException.ResponseException;
import responseGames.GameResponseClass;
import responseGames.PlayerJoinRequest;
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
                - CreateGame <gameName>
                - ListGames
                - JoinGame <gameID> <playerColor, white/black>
                - JoinObserver <gameID>
                - Delete
                """;
    }
    private String logout(String... params) throws UIException, ResponseException {
        if (params.length == 0) {
            server.logoutUser();
            return "User logged out";
        }
        throw new UIException("Expected: Logout");
    }

    private String createGame(String... params) throws UIException, ResponseException {
        if (params.length == 1) {
            server.createGame(new GameData(-1, null, null, params[0], null));
            return String.format("Created game %s", params[0]);
        }
        throw new UIException("Expected: CreateGame <gameName>");
    }

    private String listGames(String... params) throws UIException, ResponseException {
        if (params.length == 0) {
            return server.listGames().toString();
        }
        throw new UIException("Expected: ListGames");
    }

    private String joinGame(String... params) throws UIException, ResponseException {
        if (params.length == 2) {
            server.joinGame(new PlayerJoinRequest(params[1].toUpperCase(), Integer.parseInt(params[0])));
            return String.format("User joined game %s as %s player", params[0], params[1].toLowerCase());
        }
        throw new UIException("Expected: JoinGame <gameID> <playerColor, white/black>");
    }

    private String joinObserver(String... params) throws UIException, ResponseException {
        if (params.length == 1) {
            server.joinGame(new PlayerJoinRequest(null, Integer.parseInt(params[0])));
            return String.format("User joined game %s as an observer", params[0]);
        }
        throw new UIException("Expected: JoinObserver <gameID>");
    }

    private String delete(String... params) throws UIException, ResponseException {
        if (params.length == 0) {
            server.delete();
            return "Deleted all data";
        }
        throw new UIException("Expected: Delete");
    }
}
