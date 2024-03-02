package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import responseException.ResponseException;

public class RegisterService extends Service {

    public RegisterService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public AuthData registerUser(UserData newUser) throws ResponseException {
        if (newUser.username() == null || newUser.password() == null || newUser.email() == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        if (newUser.username().isEmpty() || newUser.password().isEmpty() || newUser.email().isEmpty()) {
            throw new ResponseException(400, "Error: bad request");
        }

        if (myUserDAO.getUser(newUser.username()) == null) {
            myUserDAO.createUser(newUser);
            return myAuthDAO.createAuth(newUser.username());
        }
        else {
            throw new ResponseException(403, "Error: already taken");
        }
    }
}
