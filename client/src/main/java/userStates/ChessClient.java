package userStates;

import chess.InvalidMoveException;
import serverFacade.ServerFacade;
import responseException.ResponseException;
import ui.ConsoleUI;
import ui.NotificationHandler;
import ui.UIException;

public class ChessClient {

    private final String serverURL;

    private final ServerFacade server;

    private UserState state;

    private final LoggedOutClient loggedOutClient;

    private final LoggedInClient loggedInClient;

    private GameplayClient gameplayClient;

    private final NotificationHandler notificationHandler;

    public ChessClient(String serverURL, ConsoleUI notificationHandler) throws ResponseException {
            this.serverURL = serverURL;
            this.state = UserState.LOGGEDOUT;
            this.server = new ServerFacade(this.serverURL);
            this.notificationHandler = notificationHandler;
            this.loggedOutClient = new LoggedOutClient(this.server);
            this.loggedInClient = new LoggedInClient(this.server);
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

                switch (tokens[0]) {
                    case "Logout", "Delete" -> state = UserState.LOGGEDOUT;
                    case "JoinGame", "JoinObserver" -> {
                        this.gameplayClient = new GameplayClient(this.server, this.serverURL, this.notificationHandler);
                        this.gameplayClient.joinGame(userInput);
                        state = UserState.GAMEPLAY;
                    }
                }
                return output;
            }
            else if (state == UserState.GAMEPLAY) {
                return gameplayClient.eval(userInput);
            }
            else {
                throw new ResponseException(500, "Unknown failure");
            }
        } catch (UIException ex) {
            return ex.getMessage();
        } catch (ResponseException ex) {
            return String.format("Failure: %d", ex.statusCode());
        } catch (InvalidMoveException ex) {
            return String.format("Failure: %s", ex.getMessage());
        } catch (Exception ex) {
            return "Failure: 500";
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
