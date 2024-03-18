import chess.ChessBoard;

public class GameplayClient {

    ServerFacade server;

    public GameplayClient(ServerFacade server) {
        this.server = server;
    }

    public String printBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        String boardStr = board.toString();
        return board.toString();
    }
}
