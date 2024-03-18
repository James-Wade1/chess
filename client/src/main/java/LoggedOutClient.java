import model.UserData;
import responseException.ResponseException;
import ui.UIException;

import java.util.Arrays;

public class LoggedOutClient {

    ServerFacade server;

    public LoggedOutClient(ServerFacade server) {
        this.server = server;
    }

    public String eval(String userInput) throws ResponseException, UIException {
        var tokens = userInput.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "Help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "Quit" -> "Quit";
            case "Help" -> help();
            case "Login" -> login(params);
            case "Register" -> register(params);
            case "Delete" -> delete(params);
            default -> "Unknown command. Please try again (check if you're logged in)";
        };
    }

    public String help() {
        return """
                - Help
                - Quit
                - Login <username> <password>
                - Register <username> <password> <email>
                - Delete
                """;
    }

    private String login(String... params) throws UIException, ResponseException {
        if (params.length == 2) {
            UserData returningUser = new UserData(params[0], params[1], null);
            server.loginUser(returningUser);
            return String.format("%s logged in", params[0]);
        }
        throw new UIException("Expected: Login <username> <password>");
    }

    private String register(String... params) throws UIException, ResponseException {
        if (params.length == 3) {
            UserData newUser = new UserData(params[0], params[1], params[2]);
            server.registerUser(newUser);
            return String.format("Registered user %s", params[0]);
        }
        throw new UIException("Expected: Register <username> <password> <email>");
    }

    private String delete(String... params) throws UIException, ResponseException {
        if (params.length == 0) {
            server.delete();
            return "Deleted all data";
        }
        throw new UIException("Expected: Delete");
    }
}
