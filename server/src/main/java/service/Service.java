package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class Service {

    protected AuthDAO myAuthDAO;
    protected GameDAO myGameDAO;
    protected UserDAO myUserDAO;

    protected Service(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        this.myAuthDAO = authDAO;
        this.myGameDAO = gameDAO;
        this.myUserDAO = userDAO;
    }
}
