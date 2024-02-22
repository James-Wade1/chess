package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import responseException.ResponseException;

public class LogoutService extends Service {

    public LogoutService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public void logoutUser(String authToken) throws ResponseException {
        try {
            myAuthDAO.deleteAuth(authToken);
        } catch (DataAccessException ex) {
            throw new ResponseException(401, "Error: Unauthorized");
        }
    }

}
