package responseGames;

import model.GameData;

import java.util.HashSet;

public class GameResponseClass {

    private HashSet<GameList> games;

    public GameResponseClass(HashSet<GameData> myGamesList) {
        this.games = new HashSet<GameList>();
        for (GameData myGame : myGamesList) {
            int gameID = myGame.gameID();
            String whiteUsername = myGame.whiteUsername();
            String blackUsername = myGame.blackUsername();
            String gameName = myGame.gameName();

            this.games.add(new GameList(gameID, whiteUsername, blackUsername, gameName));
        }
    }

    public record GameList(int gameID, String whiteUsername, String blackUsername, String gameName) {}
}
