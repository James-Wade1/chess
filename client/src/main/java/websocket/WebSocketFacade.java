package websocket;
import com.google.gson.Gson;
import responseException.ResponseException;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
                            gameHandler.printMessage(EscapeSequences.SET_TEXT_COLOR_GREEN + notificationMessage.getMessage());
                        }
                        throw new Exception("Error: Invalid server message");
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

    public void send(UserGameCommand command) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
