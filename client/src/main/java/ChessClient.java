import ui.UIException;

public class ChessClient {

    private String serverURL;

    private ServerFacade server;

    private UserState state;

    public ChessClient(String serverURL) {
        this.serverURL = serverURL;
        this.state = UserState.LOGGEDOUT;
        server = new ServerFacade(serverURL);
    }

    public String eval(String userInput) {
        try {
            var tokens = userInput.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            return switch (cmd) {
                case "Quit" -> "Quit";
                case "Help" -> help();
                case "Login" -> login();
                case "Register" -> register();
                case "Logout" -> logout();
                default -> "Unknown command. Please try again";
            };
        } catch (UIException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        String helpOutput = "";
        if (state == UserState.LOGGEDOUT) {
            helpOutput = """
                    - Help
                    - Quit
                    - Login
                    - Register
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

    private String register() {
        return "";
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
