package dataAccess;

import model.AuthData;

import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    static HashSet<AuthData> authDataset = new HashSet<AuthData>();

    public MemoryAuthDAO() {}

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authDataset.add(newAuth);

        return newAuth;
    }

    public AuthData getAuth(String authToken) {
        for (AuthData authData : authDataset) {
            if (authData.authtoken().equals(authToken)) {
                return authData;
            }
        }

        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        AuthData dataToRemove = null;
        for (AuthData authData : authDataset) {
            if (authData.authtoken().equals(authToken)) {
                dataToRemove = authData;
            }
        }

        if (dataToRemove == null) {
            throw new DataAccessException("AuthData to be removed not found");
        }
        else {
            authDataset.remove(dataToRemove);
        }
    }

    public void clearAuthData() {
        authDataset = new HashSet<AuthData>();
    }
}
