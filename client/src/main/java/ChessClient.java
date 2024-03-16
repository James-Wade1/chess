public class ChessClient {

    private String serverURL;

    private ServerFacade server;

    private UserState state;

    public ChessClient(String serverURL) {
        this.serverURL = serverURL;
        this.state = UserState.SIGNEDOUT;
        server = new ServerFacade(serverURL);
    }

    public String help() {
        String helpOutput = "";
        if (state == UserState.SIGNEDOUT) {
            helpOutput = """
                    - Help
                    - Quit
                    - Login
                    - Register
                    """;
        }
        else if (state == UserState.SIGNEDIN) {
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
}
