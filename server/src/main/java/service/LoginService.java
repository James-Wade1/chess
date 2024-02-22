package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import responseException.ResponseException;

public class LoginService extends Service {

    public LoginService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public AuthData loginUser(UserData returningUser) throws ResponseException {
        if (returningUser.username().isEmpty() || returningUser.password().isEmpty()) {
            throw new ResponseException(401, "Error: Unauthorized");
        }

        UserData databankUser = myUserDAO.getUser(returningUser.username());
        if (databankUser == null) {
            throw new ResponseException(401, "Error: Unauthorized");
        }
        if (databankUser.password().equals(returningUser.password())) {
            return myAuthDAO.createAuth(returningUser.username());
        }
        else {
            throw new ResponseException(401, "Error: Unauthorized");
        }
    }

}
