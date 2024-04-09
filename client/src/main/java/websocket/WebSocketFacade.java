package websocket;
import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import responseException.ResponseException;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import javax.websocket.*;

public class WebSocketFacade extends Endpoint {

    private Session session;

    private GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) throws ResponseException {
        try {
            this.gameHandler = gameHandler;
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                            LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                            gameHandler.updateGame(loadGameMessage.getGame());
                        }
                        else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
                            ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                            gameHandler.printMessage(EscapeSequences.SET_TEXT_COLOR_RED + errorMessage.getMessage());
                        }
                        else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
                            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                            gameHandler.printMessage(EscapeSequences.SET_TEXT_COLOR_BLUE + notificationMessage.getMessage());
                        }
                        else {
                            throw new Exception("Error: Invalid server message");
                        }
                    } catch (Exception ex) {
                        gameHandler.printMessage(EscapeSequences.SET_TEXT_COLOR_RED + ex.getMessage());
                    }
                }
            });

        } catch (DeploymentException | URISyntaxException | IOException ex) {
            System.out.println(ex.getMessage());
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void send(UserGameCommand command) throws IOException {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    public void joinPlayer(String userInput, String authToken) throws IOException {
        var tokens = userInput.split(" ");
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        ChessGame.TeamColor playerColor;
        if (params[1].equalsIgnoreCase("WHITE")) {
            playerColor = ChessGame.TeamColor.WHITE;
        }
        else {
            playerColor = ChessGame.TeamColor.BLACK;
        }
        send(new JoinPlayerCommand(UserGameCommand.CommandType.JOIN_PLAYER, authToken, Integer.parseInt(params[0]), playerColor));
    }

    public void joinObserver(String userInput, String authToken) throws IOException {
        var tokens = userInput.split(" ");
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        send(new JoinObserverCommand(UserGameCommand.CommandType.JOIN_OBSERVER, authToken, Integer.parseInt(params[0])));
    }

    public void makeMove(int gameID, ChessMove move, String authToken) throws IOException {
        MakeMoveCommand makeMoveCommand = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
        send(makeMoveCommand);
    }

    public void leaveGame(String authToken, int gameID) throws IOException {
        send(new LeaveCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
    }

    public void resignGame(int gameID, String authToken) throws IOException {
        send(new ResignCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID));
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
