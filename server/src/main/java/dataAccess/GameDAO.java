package dataAccess;

import model.GameData;
import responseException.ResponseException;

import java.util.HashSet;

public interface GameDAO extends DAO {

    public int createGame(String gameName) throws ResponseException;

    public GameData getGame(String gameName) throws ResponseException;

    public GameData getGame(int gameID) throws ResponseException;

    public HashSet<GameData> listGames() throws ResponseException;

    public void updateGame(GameData updatedGame) throws DataAccessException, ResponseException;

    public void clearGames() throws ResponseException;
}
