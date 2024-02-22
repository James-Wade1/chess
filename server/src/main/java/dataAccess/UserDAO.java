package dataAccess;
import model.UserData;

public interface UserDAO {

    public void createUser(String username, String password, String email);

    public UserData getUser(String username);

    public void clearUsers();
}
