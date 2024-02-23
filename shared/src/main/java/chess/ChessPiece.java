package chess;

import chess.moveCalculators.*;

import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessPiece.PieceType pieceType;
    private ChessGame.TeamColor teamColor;

    private boolean hasMoved;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        teamColor = pieceColor;
        pieceType = type;
        hasMoved = false;
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type, boolean hasMoved) {
        teamColor = pieceColor;
        pieceType = type;
        this.hasMoved = hasMoved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceType == that.pieceType && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceType, teamColor);
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

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * @param type of chess piece to promote to
     */
    public void setPieceType(PieceType type) {
        this.pieceType = type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public HashSet<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition) == null) {
            return null;
        }
        PieceType type = board.getPiece(myPosition).getPieceType();
        HashSet<ChessMove> moves;

        moves = switch (type) {
            case PieceType.PAWN -> (HashSet<ChessMove>) PawnMovesCalculator.pieceMoves(board, myPosition);
            case PieceType.ROOK -> (HashSet<ChessMove>) RookMovesCalculator.pieceMoves(board, myPosition);
            case PieceType.KNIGHT -> (HashSet<ChessMove>) KnightMovesCalculator.pieceMoves(board, myPosition);
            case PieceType.BISHOP -> (HashSet<ChessMove>) BishopMovesCalculator.pieceMoves(board, myPosition);
            case PieceType.QUEEN -> (HashSet<ChessMove>) QueenMovesCalculator.pieceMoves(board, myPosition);
            case PieceType.KING -> (HashSet<ChessMove>) KingMovesCalculator.pieceMoves(board, myPosition);
        };

        return moves;
    }
}