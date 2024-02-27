package dataAccess;

import model.GameData;
import org.eclipse.jetty.server.Authentication;
import java.util.HashSet;
import java.util.Objects;

import model.UserData;
import responseException.ResponseException;

public class MemoryUserDAO implements UserDAO {

    private static HashSet<UserData> userDataset = new HashSet<UserData>();
    public MemoryUserDAO() {}

    /** Creates new user assuming that one is not already in the hashset*/
    public void createUser(UserData newUser) throws ResponseException {
        try {
            userDataset.add(newUser);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    /** Checks if there is a user already with that username. If so, return that userData. If not, return null*/
    public UserData getUser(String username) throws ResponseException {
        try {
            for (UserData user : userDataset) {
                if (Objects.equals(user.username(), username)) {
                    return user;
                }
            }
            return null;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void clearUsers() throws ResponseException {
        userDataset = new HashSet<UserData>();
    }

    public boolean verifyPassword(String databankPassword, String returningUserPassword) {
        return databankPassword.equals(returningUserPassword);
    }
}
