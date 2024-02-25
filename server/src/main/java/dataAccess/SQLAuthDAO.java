package dataAccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO {

    public AuthData createAuth(String username) {
        return new AuthData("","");
    }

    public AuthData getAuth(String authToken) {
        return new AuthData("","");
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clearAuthData() {
        
    }
}
