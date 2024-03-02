package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import responseException.ResponseException;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Random;

public class SQLGameDAO implements GameDAO {

    private static Random randomID = new Random();

    public SQLGameDAO() {
        try {
            this.configureDatabase();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    public int createGame(String gameName) throws ResponseException {
        int gameID = randomID.nextInt(10000);
        String commands = "INSERT INTO gameDatabase (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)";
        executeUpdate(commands, gameID, null, null, gameName, new Gson().toJson(new ChessGame()));
        return gameID;
    }

    public GameData getGame(String gameName) throws ResponseException {
        String commands = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameDatabase WHERE gameName=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(commands)) {
                ps.setString(1, gameName);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
        return null;
    }

    public GameData getGame(int gameID) throws ResponseException {
        String commands = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameDatabase WHERE gameID=?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(commands)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
        return null;
    }

    public HashSet<GameData> listGames() throws ResponseException {
        HashSet<GameData> gameList = new HashSet<GameData>();
        String commands = "SELECT * FROM gameDatabase";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(commands)) {
                try (var rs = ps.executeQuery()) {
                    while(rs.next()) {
                        String whiteUsername = rs.getString("whiteUsername");
                        int gameID = rs.getInt("gameID");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        ChessGame game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
        return gameList;
    }

    public void updateGame(GameData updatedGame) throws DataAccessException, ResponseException {
        String commands = """
                UPDATE gameDatabase
                SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ?
                WHERE gameID = ?
                """;
        executeUpdate(commands, updatedGame.whiteUsername(), updatedGame.blackUsername(), updatedGame.gameName(), new Gson().toJson(updatedGame.game()), updatedGame.gameID());
    }

    public void clearGames() throws ResponseException {
        String command = "DELETE FROM gameDatabase";
        executeUpdate(command);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  gameDatabase (
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` LONGTEXT NOT NULL,
              INDEX(gameID),
              PRIMARY KEY(gameID),
              FOREIGN KEY(whiteUsername) REFERENCES userDatabase(username),
              FOREIGN KEY(blackUsername) REFERENCES userDatabase(username)
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

    private void executeUpdate(String commands, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(commands)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param == null) ps.setNull(i+1, Types.NULL);
                    else if (param instanceof Integer p) ps.setInt(i+1, p);
                    else if (param instanceof String p) ps.setString(i+1, p);
                }
                ps.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseException(500,e.getMessage());
        }
    }
}
