package service;

import dataAccess.*;

public class ClearService {

    MemoryAuthDAO myAuthDAO = new MemoryAuthDAO();
    MemoryGameDAO myGameDAO = new MemoryGameDAO();
    MemoryUserDAO myUserDAO = new MemoryUserDAO();

    public ClearService() {}

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
