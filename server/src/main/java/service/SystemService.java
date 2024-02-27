package service;

import dataAccess.*;
import responseException.ResponseException;

import java.util.HashSet;

public class SystemService extends Service {

    public SystemService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public static HashSet<DAO> instantiateDAOs() throws ResponseException {
        HashSet<DAO> myDAOHashset = new HashSet<DAO>();
        myDAOHashset.add(new MemoryAuthDAO());
        myDAOHashset.add(new MemoryGameDAO());
        myDAOHashset.add(new SQLUserDAO());

        return myDAOHashset;
    }
    public void clearData() throws ResponseException {
        clearUsers();
        clearGames();
        clearAuthTokens();
    }

    private void clearUsers() throws ResponseException {
        myUserDAO.clearUsers();
    }

    private void clearGames() {
        myGameDAO.clearGames();
    }

    private void clearAuthTokens() {
        myAuthDAO.clearAuthData();
    }
}
