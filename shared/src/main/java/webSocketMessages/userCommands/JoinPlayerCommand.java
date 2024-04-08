package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {

    int gameID;
    ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(UserGameCommand.CommandType commandType, String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
