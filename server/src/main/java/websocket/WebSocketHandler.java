package websocket;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import responseException.ResponseException;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions sessions;

    AuthDAO myAuthDAO;
    GameDAO myGameDAO;

    public WebSocketHandler(AuthDAO myAuthDAO, GameDAO myGameDAO) {
        this.myAuthDAO = myAuthDAO;
        this.myGameDAO = myGameDAO;
        sessions = new WebSocketSessions();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, ResponseException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayerCommand command = new Gson().fromJson(message, JoinPlayerCommand.class);
                joinPlayer(session, command);
            }
            case JOIN_OBSERVER -> {}
            case MAKE_MOVE -> {}
            case LEAVE -> {}
            case RESIGN -> {}
        }
    }

    private void joinPlayer(Session session, JoinPlayerCommand command) throws ResponseException, IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        this.sessions.addSessionToGame(gameID, authToken, session);

        LoadGameMessage rootMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, myGameDAO.getGame(gameID).game());
        sendMessage(gameID, rootMessage, authToken);

        String notificationMessage = String.format("%s joined the game as the %s player", myAuthDAO.getAuth(authToken).username(), command.getPlayerColor().name().toLowerCase());
        broadcastMessage(gameID, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage), authToken);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {}

    @OnWebSocketClose
    public void onClose(Session session) {}

    @OnWebSocketError
    public void onError(Session session) {}

    private void sendMessage(int gameID, ServerMessage serverMessage, String authToken) throws IOException {
        HashMap<String, Session> game = sessions.getSessionsForGame(gameID);
        Session session = game.get(authToken);
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }

    private void broadcastMessage(int gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
        HashMap<String, Session> game = sessions.getSessionsForGame(gameID);

        for (Map.Entry<String, Session> entry : game.entrySet()) {
            String authToken = entry.getKey();
            Session session = entry.getValue();
            if (!authToken.equals(exceptThisAuthToken)) {
                session.getRemote().sendString(new Gson().toJson(message));
            }
        }
    }
}
