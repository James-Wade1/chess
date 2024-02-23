package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import responseException.ResponseException;

public class CreateGameService extends Service {

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public int createGame(String authToken, String gameName) throws ResponseException {
        if (myAuthDAO.getAuth(authToken) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (myGameDAO.getGame(gameName) != null) {
            throw new ResponseException(400, "Error: bad request");
        }

        return myGameDAO.createGame(gameName);
    }
}
