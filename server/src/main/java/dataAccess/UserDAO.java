package dataAccess;
import model.UserData;

public interface UserDAO extends DAO {

    public void createUser(UserData newUser);

    public UserData getUser(String username);

    public void clearUsers();

    public int getSize();
}
