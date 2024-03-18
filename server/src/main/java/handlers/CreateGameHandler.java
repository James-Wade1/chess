package handlers;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import responseException.ResponseException;
import responseGames.GameIDResponse;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler extends Handler {

    CreateGameService myCreateGameService;
    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
        myCreateGameService = new CreateGameService(myAuthDAO, myGameDAO, myUserDAO);
    }

    public Object createGame(Request req, Response res) throws ResponseException {
        String authToken = req.headers("authorization");
        String gameName = new Gson().fromJson(req.body(), GameData.class).gameName();

        res.status(200);
        int gameID = myCreateGameService.createGame(authToken, gameName);
        return new Gson().toJson(new GameIDResponse(gameID));
    }
}
