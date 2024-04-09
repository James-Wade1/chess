package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import responseException.ResponseException;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@WebSocket
public class WebSocketHandler {

    private WebSocketSessions sessions = null;

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
            case JOIN_OBSERVER -> {
                JoinObserverCommand command = new Gson().fromJson(message, JoinObserverCommand.class);
                joinObserver(session, command);
            }
            case MAKE_MOVE -> {
                MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
                makeMove(session, command);
            }
            case LEAVE -> {
                LeaveCommand command = new Gson().fromJson(message, LeaveCommand.class);
                leaveGame(session, command);
            }
            case RESIGN -> {}
        }
    }

    private void joinPlayer(Session session, JoinPlayerCommand command) throws ResponseException, IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        AuthData authData = myAuthDAO.getAuth(authToken);
        if (authData == null) {
            sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: bad authToken"));
            return;
        }
        String username = authData.username();
        GameData gameData = myGameDAO.getGame(gameID);
        if (gameData == null) {
            sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: bad game ID"));
            return;
        }

        if (command.getPlayerColor() == ChessGame.TeamColor.WHITE) {
            if (gameData.whiteUsername() == null) {
                sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: http join request not yet called"));
                return;
            }
            else if (!gameData.whiteUsername().equals(username)) {
                sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: white spot already occupied"));
                return;
            }
        }
        else if (command.getPlayerColor() == ChessGame.TeamColor.BLACK) {
            if (gameData.blackUsername() == null) {
                sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: http join request not yet called"));
                return;
            }
            else if (!gameData.blackUsername().equals(username)) {
                sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: black spot already occupied"));
                return;
            }
        }


        this.sessions.addSessionToGame(gameID, authToken, session);
        LoadGameMessage rootMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
        sendMessage(gameID, rootMessage, authToken);

        String notificationMessage = String.format("%s joined the game as the %s player", myAuthDAO.getAuth(authToken).username(), command.getPlayerColor().name().toLowerCase());
        broadcastMessage(gameID, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage), authToken);
    }

    private void joinObserver(Session session, JoinObserverCommand command) throws IOException, ResponseException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        AuthData authData = myAuthDAO.getAuth(authToken);
        if (authData == null) {
            sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: bad authToken"));
            return;
        }
        String username = authData.username();
        GameData game = myGameDAO.getGame(gameID);
        if (game == null) {
            sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: bad game ID"));
            return;
        }

        this.sessions.addSessionToGame(gameID, authToken, session);
        LoadGameMessage rootMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
        sendMessage(gameID, rootMessage, authToken);

        String notificationMessage = String.format("%s joined the game as an observer", username);
        broadcastMessage(gameID, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage), authToken);
    }

    private void makeMove(Session session, MakeMoveCommand command) throws ResponseException, IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        ChessGame game = myGameDAO.getGame(gameID).game();
        ChessMove move = command.getMove();
        AuthData authData = myAuthDAO.getAuth(authToken);

        if (authData == null) {
            sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid authtoken"));
            return;
        }
        if (game == null) {
            sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: Invalid game ID"));
            return;
        }
        String username = authData.username();

        try {
            game.makeMove(move);
        } catch (InvalidMoveException ex) {
            sendMessage(session, new ErrorMessage(ServerMessage.ServerMessageType.ERROR, ex.getMessage()));
            return;
        }

        LoadGameMessage rootMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        sendMessage(gameID, rootMessage, authToken);

        NotificationMessage notificationMessage = getNotificationMessage(move, username);
        broadcastMessage(gameID, notificationMessage, authToken);
    }

    private static NotificationMessage getNotificationMessage(ChessMove move, String username) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();
        int endRow = endPosition.getRow();
        int endCol = endPosition.getColumn();

        char startColChar = (char) (startCol + 96);
        char endColChar = (char) (endCol + 96);

        String startCoordinates = Character.toString(startColChar) + Integer.toString(startRow);
        String endCoordinates = Character.toString(endColChar) + Integer.toString(endRow);
        String message = String.format("%s moved his piece from %s to %s", username, startCoordinates, endCoordinates);

        if (move.getPromotionPiece() != null) {
            message += "and promoted it to " + move.getPromotionPiece().name().toLowerCase();
        }

        return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
    }

    private void leaveGame(Session session, LeaveCommand command) throws ResponseException, IOException {
        int gameID = command.getGameID();
        String authToken = command.getAuthString();
        String notificationMessage = String.format("%s is leaving the game", myAuthDAO.getAuth(authToken).username());
        broadcastMessage(gameID, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, notificationMessage), authToken);
        this.sessions.removeSessionFromGame(gameID, authToken, session);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {}

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
    }

    @OnWebSocketError
    public void onError(Throwable cause) {
        return;
    }


    private void sendMessage(int gameID, ServerMessage serverMessage, String authToken) throws IOException {
        HashMap<String, Session> game = sessions.getSessionsForGame(gameID);
        Session session = game.get(authToken);
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }

    private void sendMessage(Session session, ServerMessage serverMessage) throws IOException {
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }

    private void broadcastMessage(int gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
        HashMap<String, Session> game = sessions.getSessionsForGame(gameID);
        Iterator<Map.Entry<String, Session>> iterator = game.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Session> entry = iterator.next();
            String authToken = entry.getKey();
            Session session = entry.getValue();

            if (session.isOpen()) {
                if (!authToken.equals(exceptThisAuthToken)) {
                    session.getRemote().sendString(new Gson().toJson(message));
                }
            }
            else {
                iterator.remove();
            }
        }

    }
}
