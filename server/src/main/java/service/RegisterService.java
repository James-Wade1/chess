package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import responseException.ResponseException;

public class RegisterService {

    MemoryAuthDAO myAuthDAO = new MemoryAuthDAO();
    MemoryGameDAO myGameDAO = new MemoryGameDAO();
    MemoryUserDAO myUserDAO = new MemoryUserDAO();

    public RegisterService() {}

    public String registerUser(String username, String password, String email) throws ResponseException {
        if (myUserDAO.getUser(username) == null) {
            myUserDAO.createUser(username, password, email);
            return myAuthDAO.createAuth(username);
        }
        else {
            throw new ResponseException(403, "Error: already taken");
        }
    }
}
