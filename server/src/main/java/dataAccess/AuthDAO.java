package dataAccess;

import model.AuthData;
import responseException.ResponseException;

import java.util.HashSet;

public interface AuthDAO extends DAO {

    public AuthData createAuth(String username);

    public AuthData getAuth(String authToken) throws ResponseException;

    public void deleteAuth(String authToken) throws DataAccessException;

    public void clearAuthData();
}
