package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {

    int gameID;
    ChessMove move;

    public MakeMoveCommand(CommandType commandType, String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
        this.move = move;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
