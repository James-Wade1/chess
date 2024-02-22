package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.UUID;
import java.util.Random;

public class MemoryGameDAO implements GameDAO {

    private static HashSet<GameData> gameDataset = new HashSet<GameData>();
    private static Random randomID = new Random();

    public MemoryGameDAO() {}

    public void createGame(String gameName) {
        int gameID = randomID.nextInt(10000);
        gameDataset.add(new GameData(gameID, "", "", gameName, new ChessGame()));
    }

    public GameData getGame(String gameName) {
        for (GameData game : gameDataset) {
            if (game.gameName().equals(gameName)) {
                return game;
            }
        }
        return null;
    }

    public HashSet<GameData> listGames() {
        return gameDataset;
    }

    public void updateGame(String gameName) throws DataAccessException {
        for (GameData game : gameDataset) {
            if (game.gameName().equals(gameName)) {
                //FIXME
            }
        }

        throw new DataAccessException("Game does not exist");
    }

    public void clearGames() {
        gameDataset = new HashSet<GameData>();
    }
}
