package responseGames;

import model.GameData;

import java.util.HashSet;

public class GameResponseClass {

    private HashSet<gameList> games;

    public GameResponseClass(HashSet<GameData> myGamesList) {
        this.games = new HashSet<gameList>();
        for (GameData myGame : myGamesList) {
            int gameID = myGame.gameID();
            String whiteUsername;
            String blackUsername;
            if (myGame.whiteUsername() == null) {
                whiteUsername = "";
            }
            else {
                whiteUsername = myGame.whiteUsername();
            }

            if (myGame.blackUsername() == null) {
                blackUsername = "";
            }
            else {
                blackUsername = myGame.blackUsername();
            }

            String gameName = myGame.gameName();

            this.games.add(new gameList(gameID, whiteUsername, blackUsername, gameName));
        }
    }

    public HashSet<gameList> getMyGamesList() {
        return this.games;
    }

    public record gameList(int gameID, String whiteUsername, String blackUsername, String gameName) {}
}
