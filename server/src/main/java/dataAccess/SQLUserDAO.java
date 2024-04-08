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
        executeUpdate(command, newUser.username(), encryptUserPassword(newUser.password()), newUser.email());
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
        executeUpdate(command);
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

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    private String encryptUserPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    private void executeUpdate(String commands, String... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(commands)) {
                for (int i = 0; i < params.length; i ++) {
                    ps.setString(i+1, params[i]);
                }
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
