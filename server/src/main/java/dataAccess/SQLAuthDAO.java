package dataAccess;

import model.AuthData;
import model.UserData;
import responseException.ResponseException;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() {
        try {
            DatabaseManager.configureDatabase(createStatements);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AuthData createAuth(String username) throws ResponseException {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        String commands = "INSERT INTO authDatabase (authToken, username) VALUES (?, ?)";
        executeUpdate(commands, authToken, username);
        return newAuth;
    }

    public AuthData getAuth(String authToken) throws ResponseException {
        String command = "SELECT authToken, username FROM authDatabase WHERE authToken=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(command)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("authToken"), rs.getString("username"));
                    }
                }
            }
        } catch(DataAccessException | SQLException ex) {
            throw new ResponseException(500, "Expected no error but got " + ex);
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            AuthData authToDelete = getAuth(authToken);
            if (authToDelete == null) {
                throw new DataAccessException("AuthData to be removed not found");
            }
            String command = "DELETE FROM authDatabase WHERE authToken=?";
            executeUpdate(command, authToken);
        } catch (ResponseException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void clearAuthData() throws ResponseException {
        String command = "DELETE FROM authDatabase";
        executeUpdate(command);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authDatabase (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              INDEX(authToken),
              PRIMARY KEY(authToken),
              FOREIGN KEY(username) REFERENCES userDatabase(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin
            """
    };

    private void executeUpdate(String commands, String... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(commands)) {
                for (int i = 0; i < params.length; i++) {
                    ps.setString(i+1, params[i]);
                }
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}
