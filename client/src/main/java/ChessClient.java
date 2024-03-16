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
        var tokens = userInput.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";
        return switch(cmd) {
            case "Quit" -> "Quit";
            case "Help" -> help();
            default -> "Unknown command. Please try again";
        };
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

    public String getUserState() {
        switch (state) {
            case LOGGEDOUT -> {return "[LOGGED_OUT]";}
            case LOGGEDIN -> {return "[LOGGED_IN]";}
            case GAMEPLAY -> {return "[GAMEPLAY]";}
        }
        return null;
    }
}
