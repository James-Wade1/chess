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

    private LoggedOutClient loggedOutClient;

    public ChessClient(String serverURL) {
        this.serverURL = serverURL;
        this.state = UserState.LOGGEDOUT;
        this.server = new ServerFacade(this.serverURL);
        this.loggedOutClient = new LoggedOutClient(this.server);
    }

    public String eval(String userInput) {
        try {
            if (state == UserState.LOGGEDOUT) {
                return loggedOutClient.eval(userInput, state);
            }
            else if (state == UserState.LOGGEDIN) {
                return "";
            }
            else if (state == UserState.GAMEPLAY) {
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
