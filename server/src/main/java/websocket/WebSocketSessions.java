package websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    private final ConcurrentHashMap<Integer, HashMap<String, Session>> sessionMap;

    public WebSocketSessions() {
       sessionMap = new ConcurrentHashMap<>();
    }

    public void addSessionToGame(int gameID, String authToken, Session session) {
        sessionMap.get(gameID).put(authToken, session);
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {
        HashMap<String, Session> game = sessionMap.get(gameID);
        if (game != null) {
            game.remove(authToken);
        }
    }

    public void removeSession(Session session) {
        for (HashMap<String, Session> game : sessionMap.values()) {
            // Remove the session if it matches the provided session
            game.entrySet().removeIf(entry -> entry.getValue() == session);
        }
    }

    public HashMap<String, Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
