package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {

    private final WebSocketSessions sessions;

    public WebsocketHandler() {
        sessions = new WebSocketSessions();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> {}
            case JOIN_OBSERVER -> {}
            case MAKE_MOVE -> {}
            case LEAVE -> {}
            case RESIGN -> {}
        }
    }
}
