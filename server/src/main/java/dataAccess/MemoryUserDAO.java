package dataAccess;

import model.GameData;
import org.eclipse.jetty.server.Authentication;
import java.util.HashSet;
import java.util.Objects;

import model.UserData;

public class MemoryUserDAO implements UserDAO {

    private static HashSet<UserData> userDataset = new HashSet<UserData>();
    public MemoryUserDAO() {}

    /** Creates new user assuming that one is not already in the hashset*/
    public void createUser(String username, String password, String email) {
        userDataset.add(new UserData(username, password, email));
    }

    /** Checks if there is a user already with that username. If so, return that userData. If not, return null*/
    public UserData getUser(String username) {
        for (UserData user : userDataset) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }

    public void clearUsers() {
        userDataset = new HashSet<UserData>();
    }
}
