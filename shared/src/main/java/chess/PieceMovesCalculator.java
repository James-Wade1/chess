package chess;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {

    static void validPosition(HashSet<ChessMove> moves, ChessPiece myPositionPiece, ChessPiece newPositionPiece, ChessPosition myPosition, ChessPosition newPosition, ChessPiece.PieceType promotionPiece) {
        if (newPositionPiece == null) {
            moves.add(new ChessMove(myPosition, newPosition, promotionPiece));
            return;
        } else {
            if (newPositionPiece.getTeamColor() == myPositionPiece.getTeamColor()) {
                return;
            } else if (newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, promotionPiece));
                return;
            }
        }
    }

    static boolean boolValidPosition(HashSet<ChessMove> moves, ChessPiece myPositionPiece, ChessPiece newPositionPiece, ChessPosition myPosition, ChessPosition newPosition, ChessPiece.PieceType promotionPiece) {
        if (newPositionPiece == null) {
            moves.add(new ChessMove(myPosition, newPosition, promotionPiece));
            return true;
        }
        else {
            if (newPositionPiece.getTeamColor() == myPositionPiece.getTeamColor()) { return false; }
            else if (newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                moves.add(new ChessMove(myPosition, newPosition, promotionPiece));
                return false;
            }
        }
        return true;
    }

    static boolean inBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
