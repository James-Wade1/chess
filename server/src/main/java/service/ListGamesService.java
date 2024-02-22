package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.GameData;
import responseException.ResponseException;

import java.util.HashSet;

public class ListGamesService extends Service {

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public HashSet<GameData> listGames(String authToken) throws ResponseException {
        HashSet<GameData> myGames = null;
        AuthData registeredAuthData = myAuthDAO.getAuth(authToken);
        if (registeredAuthData == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        else {
            return myGameDAO.listGames();
        }
    }
}
