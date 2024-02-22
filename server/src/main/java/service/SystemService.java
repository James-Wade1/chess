package service;

import dataAccess.*;

import java.util.HashSet;

public class SystemService extends Service {

    public SystemService(AuthDAO authDAO, GameDAO gameDAO, UserDAO userDAO) {
        super(authDAO, gameDAO, userDAO);
    }

    public static HashSet<DAO> instantiateDAOs() {
        HashSet<DAO> myDAOHashset = new HashSet<DAO>();
        myDAOHashset.add(new MemoryAuthDAO());
        myDAOHashset.add(new MemoryGameDAO());
        myDAOHashset.add(new MemoryUserDAO());

        return myDAOHashset;
    }
    public void clearData() {
        clearUsers();
        clearGames();
        clearAuthTokens();
    }

    private void clearUsers() {
        myUserDAO.clearUsers();
    }

    private void clearGames() {
        myGameDAO.clearGames();
    }

    private void clearAuthTokens() {
        myAuthDAO.clearAuthData();
    }
}
