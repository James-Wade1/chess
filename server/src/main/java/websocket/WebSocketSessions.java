package websocket;

import spark.Session;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    private final ConcurrentHashMap<String, HashMap<String, Session>> sessionMap;

    public WebSocketSessions() {
       sessionMap = new ConcurrentHashMap<>();
    }

    public void addSessionToGame(String gameID, String authToken, Session session) {
        sessionMap.get(gameID).put(authToken, session);
    }
}
