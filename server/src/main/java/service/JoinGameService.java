package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.GameData;
import responseException.ResponseException;

public class JoinGameService extends Service {

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException, DataAccessException {
        if (myAuthDAO.getAuth(authToken) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        GameData gameToJoin = myGameDAO.getGame(gameID);
        if (gameToJoin == null) {
            throw new ResponseException(400, "Error: bad request");
        }

        if (playerColor.equals("WHITE")) {
            if (!gameToJoin.whiteUsername().isEmpty()) {
                throw new ResponseException(403, "Error: already taken");
            }
            GameData updatedGame = new GameData(gameToJoin.gameID(), myAuthDAO.getAuth(authToken).username(), gameToJoin.blackUsername(), gameToJoin.gameName(), gameToJoin.game());
            myGameDAO.updateGame(updatedGame);
        }

        else if (playerColor.equals("BLACK")) {
            if (!gameToJoin.blackUsername().isEmpty()) {
                throw new ResponseException(403, "Error: already taken");
            }
            GameData updatedGame = new GameData(gameToJoin.gameID(), gameToJoin.whiteUsername(), myAuthDAO.getAuth(authToken).username(), gameToJoin.gameName(), gameToJoin.game());
            myGameDAO.updateGame(updatedGame);
        }
    }
}
