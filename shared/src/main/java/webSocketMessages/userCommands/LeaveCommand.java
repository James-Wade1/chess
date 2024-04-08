package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {

    int gameID;

    public LeaveCommand(CommandType commandType, String authToken, int gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
