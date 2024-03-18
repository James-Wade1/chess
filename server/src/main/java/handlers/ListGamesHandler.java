package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import responseException.ResponseException;
import model.GameResponseClass;
import service.ListGamesService;
import spark.Request;
import spark.Response;

import java.util.HashSet;

public class ListGamesHandler extends Handler {
    ListGamesService myListGamesService;
    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
        myListGamesService = new ListGamesService(myAuthDAO, myGameDAO, myUserDAO);
    }

    public Object listGames(Request req, Response res) throws ResponseException {
        String authToken = req.headers("authorization");

        res.status(200);
        HashSet<GameData> myListedGames = myListGamesService.listGames(authToken);
        return new Gson().toJson(new GameResponseClass(myListedGames));
    }
}
