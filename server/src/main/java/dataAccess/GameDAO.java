package dataAccess;

import model.GameData;

import java.util.HashSet;

public interface GameDAO extends DAO {

    public int createGame(String gameName);

    public GameData getGame(String gameName);

    public GameData getGame(int gameID);

    public HashSet<GameData> listGames();

    public void updateGame(GameData updatedGame) throws DataAccessException;

    public void clearGames();

    public int getSize();
}
