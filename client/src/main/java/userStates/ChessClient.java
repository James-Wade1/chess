package userStates;

import serverFacade.ServerFacade;
import responseException.ResponseException;
import ui.ConsoleUI;
import ui.NotificationHandler;
import ui.UIException;

public class ChessClient {

    private String serverURL;

    private ServerFacade server;

    private UserState state;

    private LoggedOutClient loggedOutClient;

    private LoggedInClient loggedInClient;

    private GameplayClient gameplayClient;

    private NotificationHandler notificationHandler;

    public ChessClient(String serverURL, ConsoleUI notificationHandler) throws ResponseException {
            this.serverURL = serverURL;
            this.state = UserState.LOGGEDOUT;
            this.server = new ServerFacade(this.serverURL);
            this.loggedOutClient = new LoggedOutClient(this.server);
            this.loggedInClient = new LoggedInClient(this.server);
            this.gameplayClient = new GameplayClient(this.server, this.serverURL, notificationHandler);
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
                    state = UserState.GAMEPLAY;
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

    public String help() {
        if (state == UserState.LOGGEDOUT) {
            return loggedOutClient.help();
        }
        else if (state == UserState.LOGGEDIN) {
            return loggedInClient.help();
        }
        else {
            return gameplayClient.help();
        }
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
