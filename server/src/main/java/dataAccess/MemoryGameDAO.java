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

    public int createGame(String gameName) {
        int gameID = randomID.nextInt(10000);
        gameDataset.add(new GameData(gameID, "", "", gameName, new ChessGame()));
        return gameID;
    }

    public GameData getGame(String gameName) {
        for (GameData game : gameDataset) {
            if (game.gameName().equals(gameName)) {
                return game;
            }
        }
        return null;
    }

    public GameData getGame(int gameID) {
        for (GameData game : gameDataset) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public HashSet<GameData> listGames() {
        return gameDataset;
    }

    public void updateGame(GameData updatedGame) throws DataAccessException {
        GameData oldGame = null;
        for (GameData game : gameDataset) {
            if (game.gameID() == updatedGame.gameID()) {
                oldGame = game;
            }
        }
        if (oldGame == null) {
            throw new DataAccessException("Game does not exist");
        }
        else {
            gameDataset.remove(oldGame);
            gameDataset.add(updatedGame);
        }
    }

    public void clearGames() {
        gameDataset = new HashSet<GameData>();
    }
}
