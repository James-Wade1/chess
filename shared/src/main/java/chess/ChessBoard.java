package chess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] chessBoard;

    private HashSet<ChessPosition> whitePieces;
    private HashSet<ChessPosition> blackPieces;

    public ChessBoard() {
        chessBoard = new ChessPiece[8][8];
        whitePieces = new HashSet<>();
        blackPieces = new HashSet<>();
    }

    public ChessBoard(ChessBoard oldBoard) {
        this();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece oldPiece = oldBoard.getPiece(new ChessPosition(i,j));
                if (oldPiece != null) {
                    this.addPiece(new ChessPosition(i,j), new ChessPiece(oldPiece.getTeamColor(), oldPiece.getPieceType()));
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(chessBoard);
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        chessBoard[position.getRow()-1][position.getColumn()-1] = piece;
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                whitePieces.add(position);
            } else {
                blackPieces.add(position);
            }
        }
    }

    public void removePiece(ChessPosition position) {
        ChessPiece piece = this.getPiece(position);
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                whitePieces.remove(position);
            } else {
                blackPieces.remove(position);
            }
        }
        chessBoard[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return chessBoard[position.getRow()-1][position.getColumn()-1];
    }

    public HashSet<ChessPosition> getColorPieces(ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            return whitePieces;
        }
        else {
            return blackPieces;
        }
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            for (ChessPosition position : whitePieces) {
                if (getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                    return position;
                }
            }

            //throw new RuntimeException("White king not found on board");
        }
        else {
            for (ChessPosition position : blackPieces) {
                if (getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                    return position;
                }
            }
            //throw new RuntimeException("Black king not found on board");
        }
        return null;
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
            whitePieces.add(new ChessPosition(1,i+1));
        }
        // Pawn Row
        for (int i = 0; i < 8; i++) {
            chessBoard[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            whitePieces.add(new ChessPosition(2,i+1));
        }

        // Make black's pieces
        // Back row
        for (int i = 0; i < 8; i++) {
            chessBoard[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, backRow[i]);
            blackPieces.add(new ChessPosition(8,i+1));
        }
        // Pawn Row
        for (int i = 0; i < 8; i++) {
            chessBoard[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            blackPieces.add(new ChessPosition(7,i+1));
        }
    }
}
