package dataAccess;
import model.UserData;
import responseException.ResponseException;

public interface UserDAO extends DAO {

    public void createUser(UserData newUser) throws ResponseException;

    public UserData getUser(String username) throws ResponseException;

    public void clearUsers() throws ResponseException;

    public boolean verifyPassword(String databankPassword, String returningUserPassword);
}
