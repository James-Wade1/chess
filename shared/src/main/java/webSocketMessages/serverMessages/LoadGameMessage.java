package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {

    ChessGame game;

    public LoadGameMessage(ServerMessageType serverMessageType, ChessGame game) {
        super(serverMessageType);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
