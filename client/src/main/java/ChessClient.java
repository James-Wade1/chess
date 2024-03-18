import ServerFacade.ServerFacade;
import UserStates.UserState;
import responseException.ResponseException;
import ui.UIException;

public class ChessClient {

    private String serverURL;

    private ServerFacade server;

    private UserState state;

    private LoggedOutClient loggedOutClient;

    private LoggedInClient loggedInClient;

    private GameplayClient gameplayClient;

    public ChessClient(String serverURL) {
        this.serverURL = serverURL;
        this.state = UserState.LOGGEDOUT;
        this.server = new ServerFacade(this.serverURL);
        this.loggedOutClient = new LoggedOutClient(this.server);
        this.loggedInClient = new LoggedInClient(this.server);
        this.gameplayClient = new GameplayClient(this.server);
    }

    public String eval(String userInput) {
        try {
            var tokens = userInput.split(" ");
            String output = "";
            if (state == UserState.LOGGEDOUT) {
                output = loggedOutClient.eval(userInput);
                if (tokens[0].equals("Login")) {
                    state = UserState.LOGGEDIN;
                }
                return output;
            }
            else if (state == UserState.LOGGEDIN) {
                output = loggedInClient.eval(userInput);

                if (tokens[0].equals("Logout")) {
                    state = UserState.LOGGEDOUT;
                }
                else if (tokens[0].equals("JoinGame") || tokens[0].equals("JoinObserver")) {
                    //state = UserStates.UserState.GAMEPLAY;
                    return gameplayClient.printBoard();
                }
                else if (tokens[0].equals("Delete")) {
                    state = UserState.LOGGEDOUT;
                }
                return output;
            }
            else if (state == UserState.GAMEPLAY) {
                //return gameplayClient.printBoard();
                return "";
            }
            else {
                throw new ResponseException(500, "Unknown failure");
            }
        } catch (UIException ex) {
            return ex.getMessage();
        } catch (ResponseException ex) {
            return String.format("Failure: %d", ex.statusCode());
        }
    }



    private String logout() throws UIException {
        if (state == UserState.LOGGEDOUT) {
            throw new UIException("Need to be logged in first");
        }
        return "";
    }

    public String help() {
        String helpOutput = "";
        if (state == UserState.LOGGEDOUT) {
            return loggedOutClient.help();
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

    public String getUserState() {
        switch (state) {
            case LOGGEDOUT -> {return "[LOGGED_OUT]";}
            case LOGGEDIN -> {return "[LOGGED_IN]";}
            case GAMEPLAY -> {return "[GAMEPLAY]";}
        }
        return null;
    }
}
