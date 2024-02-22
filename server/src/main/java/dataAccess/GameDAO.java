package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO {

    public void createGame(String gameName);

    public GameData getGame(String gameName);

    public HashSet<GameData> listGames();

    public void updateGame(String gameName) throws DataAccessException;

    public void clearGames();
}
