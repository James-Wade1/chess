import model.AuthData;
import model.UserData;
import responseException.ResponseException;
import ui.UIException;

import java.util.ArrayList;
import java.util.Arrays;

public class ChessClient {

    private String serverURL;

    private ServerFacade server;

    private UserState state;

    public ChessClient(String serverURL) {
        this.serverURL = serverURL;
        this.state = UserState.LOGGEDOUT;
        server = new ServerFacade(this.serverURL);
    }

    public String eval(String userInput) {
        try {
            var tokens = userInput.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "Help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "Quit" -> "Quit";
                case "Help" -> help();
                case "Login" -> login(params);
                case "Register" -> register(params);
                case "Logout" -> logout();
                case "Delete" -> delete(params);
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
                    - Delete
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
                    - Delete
                    """;
        }
        else if (state == UserState.GAMEPLAY) {
            helpOutput = """
                    - Example Output
                    """;
        }
        return helpOutput;
    }

    private String login(String... params) throws UIException, ResponseException {
        if (params.length == 2) {
            UserData returningUser = new UserData(params[0], params[1], null);
            server.login(returningUser);
            state = UserState.LOGGEDIN;
            return String.format("%s logged in", params[0]);
        }
        throw new UIException("Expected: Login <username> <password>");
    }

    private String register(String... params) throws UIException, ResponseException {
        if (params.length == 3) {
            UserData newUser = new UserData(params[0], params[1], params[2]);
            server.register(newUser);
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

    private String delete(String... params) throws UIException, ResponseException {
        state = UserState.LOGGEDOUT;
        if (params.length == 0) {
            server.delete();
            return "Deleted all data";
        }
        throw new UIException("Expected: Delete");
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
