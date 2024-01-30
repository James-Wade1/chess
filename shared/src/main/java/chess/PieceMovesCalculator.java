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

    static void castlingCalculator(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        HashSet<ChessMove> castlingMoves = new HashSet<ChessMove>();

        ChessPiece king = board.getPiece(myPosition);
        if (king.isHasMoved()) { // can't castle if they've moved
            return;
        }

        int homeRow;
        int homeCol = 5;

        if (king.getTeamColor() == ChessGame.TeamColor.WHITE) {
            homeRow = 1;
        }
        else {
            homeRow = 8;
        }

        ChessPiece testPiece = board.getPiece(new ChessPosition(homeRow, homeCol+1));

        if (board.getPiece(new ChessPosition(homeRow, homeCol+1)) == null && board.getPiece(new ChessPosition(homeRow, homeCol+2)) == null) {
            //Check king-side castle, ignoring checking
            ChessPiece rightRook = board.getPiece(new ChessPosition(homeRow, homeCol+3));
            if (rightRook != null && !rightRook.isHasMoved()) {
                moves.add(new ChessMove(myPosition, new ChessPosition(homeRow,homeCol+2), null));
            }
        }

        if (board.getPiece(new ChessPosition(homeRow, homeCol-1)) == null && board.getPiece(new ChessPosition(homeRow, homeCol-2)) == null && board.getPiece(new ChessPosition(homeRow, homeCol-3)) == null) {
            //Check queen-side castle, ignoring checking
            ChessPiece rightRook = board.getPiece(new ChessPosition(homeRow, homeCol-4));
            if (rightRook != null && !rightRook.isHasMoved()) {
                moves.add(new ChessMove(myPosition, new ChessPosition(homeRow,homeCol-2), null));
            }
        }
    }
}
