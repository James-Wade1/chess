package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        ChessPiece myPositionPiece = board.getPiece(myPosition);

        int promotionRow;
        int travelDirection;
        int startingRow;

        if (myPositionPiece.getTeamColor() == ChessGame.TeamColor.WHITE) { //Move up
            promotionRow = 7;
            travelDirection = 1;
            startingRow = 2;
        }
        else {
            promotionRow = 2;
            travelDirection = -1;
            startingRow = 7;
        }

        ChessPiece.PieceType[] promotionPieces;

        if (myRow == promotionRow) {
            promotionPieces = new ChessPiece.PieceType[]{ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
        }
        else {
            promotionPieces = new ChessPiece.PieceType[]{null};
        }

        int row = myRow + travelDirection;
        for (int col = myCol - 1; col <= myCol + 1; col++) {
            ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
            if (col != myCol) { // A diagonal move
                if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                    for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                    }
                }
            } else { // A forward move
                if (newPositionPiece == null) {
                    for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                        if (myRow == startingRow && board.getPiece(new ChessPosition(row + travelDirection, col)) == null) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row + travelDirection, col), null));
                        }
                    }
                }
            }
        }

        return moves;
    }
}
