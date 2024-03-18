package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import responseException.ResponseException;
import model.PlayerJoinRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler extends Handler {

    JoinGameService myJoinGameService;
    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
        myJoinGameService = new JoinGameService(myAuthDAO, myGameDAO, myUserDAO);
    }

    public Object joinGame(Request req, Response res) throws ResponseException, DataAccessException {
        String authToken = req.headers("authorization");
        PlayerJoinRequest joinRequest = new Gson().fromJson(req.body(), PlayerJoinRequest.class);
        myJoinGameService.joinGame(authToken, joinRequest.playerColor(), joinRequest.gameID());

        res.status(200);
        return "{}";
    }
}
