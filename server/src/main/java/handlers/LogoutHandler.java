package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import org.eclipse.jetty.websocket.server.ServletWebSocketResponse;
import responseException.ResponseException;
import service.LogoutService;
import spark.Request;
import spark.Response;

import java.util.Set;

public class LogoutHandler extends Handler {

    LogoutService myLogoutService;
    public LogoutHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
        myLogoutService = new LogoutService(myAuthDAO, myGameDAO, myUserDAO);
    };

    public Object logoutUser(Request req, Response res) throws ResponseException {
        String authToken = req.headers("authorization");

        myLogoutService.logoutUser(authToken);

        res.status(200);
        return "{}";
    }
}
