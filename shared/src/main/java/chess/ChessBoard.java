package chess;
import java.sql.Array;
import java.util.ArrayList;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] chessBoard;

    public ChessBoard() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        chessBoard = new ChessPiece[8][8];

        ChessPiece.PieceType[] backRow = { ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

        // Make white's pieces
        // Back row
        for (int i = 0; i < 8; i++) {
            chessBoard[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, backRow[i]);
        }
        // Pawn Row
        for (int i = 0; i < 8; i++) {
            chessBoard[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        // Make black's pieces
        // Back row
        for (int i = 0; i < 8; i++) {
            chessBoard[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, backRow[i]);
        }
        // Pawn Row
        for (int i = 0; i < 8; i++) {
            chessBoard[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }
}
