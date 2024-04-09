package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {

    int gameID;

    public ResignCommand(CommandType commandType, String authToken, int gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
