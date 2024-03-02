package handlers;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class Handler {
    protected AuthDAO myAuthDAO;
    protected GameDAO myGameDAO;
    protected UserDAO myUserDAO;
    protected Handler(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.myAuthDAO = authDAO;
        this.myGameDAO = gameDAO;
        this.myUserDAO = userDAO;
    }
}
