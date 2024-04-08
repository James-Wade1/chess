package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {

    int gameID;

    public JoinObserverCommand(CommandType commandType, String authToken, int gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }
}
