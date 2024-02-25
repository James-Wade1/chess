package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public class SQLGameDAO implements GameDAO {

    public int createGame(String gameName) {
        return 0;
    }

    public GameData getGame(String gameName) {
        return new GameData(0, "", "", "", new ChessGame());
    }

    public GameData getGame(int gameID) {
        return new GameData(0, "", "", "", new ChessGame());
    }

    public HashSet<GameData> listGames() {
        return new HashSet<GameData>();
    }

    public void updateGame(GameData updatedGame) throws DataAccessException {

    }

    public void clearGames() {

    }
}
