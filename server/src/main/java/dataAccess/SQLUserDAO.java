package dataAccess;

import com.google.gson.Gson;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import responseException.ResponseException;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() {
        try {
            DatabaseManager.configureDatabase(createStatements);
        } catch(DataAccessException | ResponseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void createUser(UserData newUser) throws ResponseException {
        String command = "INSERT INTO userDatabase (username, password, email) VALUES (?, ?, ?)";
        DatabaseManager.executeUpdate(command, newUser.username(), encryptUserPassword(newUser.password()), newUser.email());
    }

    public UserData getUser(String username) throws ResponseException {
        String command = "SELECT username, password, email FROM userDatabase WHERE username=? ";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(command)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                }
            }
        } catch (DataAccessException | SQLException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
        return null;
    }

    public void clearUsers() throws ResponseException {
        String command = "DELETE FROM userDatabase";
        DatabaseManager.executeUpdate(command);
    }

    public boolean verifyPassword(String databankPassword, String returningUserPassword) {
        return new BCryptPasswordEncoder().matches(returningUserPassword, databankPassword);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userDatabase (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              INDEX(username),
              PRIMARY KEY(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
            """
    };

    private String encryptUserPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
