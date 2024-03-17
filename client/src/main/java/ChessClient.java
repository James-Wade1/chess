import model.AuthData;
import responseException.ResponseException;
import ui.UIException;

import java.util.ArrayList;
import java.util.Arrays;

public class ChessClient {

    private String serverURL;

    private ServerFacade server;

    private UserState state;

    private AuthData authTokens = null;

    public ChessClient(String serverURL) {
        this.serverURL = serverURL;
        this.state = UserState.LOGGEDOUT;
        server = new ServerFacade(this.serverURL);
    }

    public String eval(String userInput) {
        try {
            var tokens = userInput.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "Quit" -> "Quit";
                case "Help" -> help();
                case "Login" -> login();
                case "Register" -> register(params);
                case "Logout" -> logout();
                default -> "Unknown command. Please try again";
            };
        } catch (UIException ex) {
            return ex.getMessage();
        } catch (ResponseException ex) {
            return String.format("Failure: %d", ex.statusCode());
        }
    }

    public String help() {
        String helpOutput = "";
        if (state == UserState.LOGGEDOUT) {
            helpOutput = """
                    - Help
                    - Quit
                    - Login <username> <password>
                    - Register <username> <password> <email>
                    """;
        }
        else if (state == UserState.LOGGEDIN) {
            helpOutput = """
                    - Help
                    - Logout
                    - CreateGame
                    - ListGames
                    - JoinGame
                    - JoinObserver
                    """;
        }
        else if (state == UserState.GAMEPLAY) {
            helpOutput = """
                    - Example Output
                    """;
        }
        return helpOutput;
    }

    private String login() {
        return "";
    }

    private String register(String... params) throws UIException, ResponseException {
        if (params.length == 3) {
            server.register(params);
            return String.format("Registered user %s", params[0]);
        }
        throw new UIException("Expected: Register <username> <password> <email>");
    }

    private String logout() throws UIException {
        if (state == UserState.LOGGEDOUT) {
            throw new UIException("Need to be logged in first");
        }
        return "";
    }

    public String getUserState() {
        switch (state) {
            case LOGGEDOUT -> {return "[LOGGED_OUT]";}
            case LOGGEDIN -> {return "[LOGGED_IN]";}
            case GAMEPLAY -> {return "[GAMEPLAY]";}
        }
        return null;
    }
}
