package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessPiece.PieceType pieceType;
    private ChessGame.TeamColor teamColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        teamColor = pieceColor;
        pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition) == null) {
            return null;
        }
        PieceType type = board.getPiece(myPosition).getPieceType();
        Collection<ChessMove> moves;

        moves = switch (type) {
            case PieceType.PAWN -> pawnMoves(board, myPosition);
            case PieceType.ROOK -> rookMoves(board, myPosition);
            case PieceType.KNIGHT -> knightMoves(board, myPosition);
            case PieceType.BISHOP -> bishopMoves(board, myPosition);
            case PieceType.QUEEN -> queenMoves(board, myPosition);
            case PieceType.KING -> kingMoves(board, myPosition);
        };

        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int x = myPosition.getRow();
        int y = myPosition.getColumn();

        // Note: x is the vertical axis on the chessboard

        //Traveling to top right
        outer:
        while(x <= 8) {
            while(y <= 8) {
                x++;
                y++;
                if (y > 8 || x > 8) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(x,y);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!validPosition(moves, newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        x = myPosition.getRow();
        y = myPosition.getColumn();

        // Go to top left
        outer:
        while(x <= 8) {
            while(y >= 1) {
                x++;
                y--;
                if (y < 1 || x > 8) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(x,y);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!validPosition(moves, newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        x = myPosition.getRow();
        y = myPosition.getColumn();

        // Go to bottom right
        outer:
        while(x >= 1) {
            while(y <= 8) {
                x--;
                y++;
                if (y > 8 || x < 1) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(x,y);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!validPosition(moves, newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        x = myPosition.getRow();
        y = myPosition.getColumn();

        // Go to bottom left
        outer:
        while(x >= 1) {
            while(y >= 1) {
                x--;
                y--;
                if (y < 1 || x < 1) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(x,y);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!validPosition(moves, newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        return moves;
    }

    private boolean validPosition(HashSet<ChessMove> moves, ChessPiece newPositionPiece, ChessPosition myPosition, ChessPosition newPosition, PieceType promotionPiece) {
        if (newPositionPiece == null) {
            moves.add(new ChessMove(myPosition, newPosition, promotionPiece));
            return true;
        }
        else {
            if (newPositionPiece.getTeamColor() == ChessGame.TeamColor.WHITE) { return false; }
            if (newPositionPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                moves.add(new ChessMove(myPosition, newPosition, promotionPiece));
                return false;
            }
        }
        return true;
    }
}