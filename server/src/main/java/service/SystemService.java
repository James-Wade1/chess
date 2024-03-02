package service;

import dataAccess.*;
import responseException.ResponseException;

import java.util.HashSet;

public class SystemService extends Service {

    public SystemService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public static HashSet<DAO> instantiateDAOs() {
        HashSet<DAO> myDAOHashset = new HashSet<DAO>();
        myDAOHashset.add(new SQLUserDAO());
        myDAOHashset.add(new SQLAuthDAO());
        myDAOHashset.add(new SQLGameDAO());

        return myDAOHashset;
    }
    public void clearData() throws ResponseException {
        clearAuthTokens();
        clearGames();
        clearUsers();
    }

    private void clearUsers() throws ResponseException {
        myUserDAO.clearUsers();
    }

    private void clearGames() throws ResponseException {
        myGameDAO.clearGames();
    }

    private void clearAuthTokens() throws ResponseException {
        myAuthDAO.clearAuthData();
    }
}
