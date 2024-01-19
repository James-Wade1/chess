package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class KingMovesCalculator implements PieceMovesCalculator {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        ArrayList<ChessPosition> spotsAroundKing = new ArrayList<ChessPosition>();

        for (int row = myRow - 1; row <= myRow + 1; row++) {
            for (int col = myCol - 1; col <= myCol + 1; col++) {
                if (row > 0 && row < 9 && col > 0 && col < 9) {
                    if (row != myRow || col != myCol) {
                        spotsAroundKing.add(new ChessPosition(row, col));
                    }
                }
            }
        }

        for (ChessPosition newPosition : spotsAroundKing) {
            PieceMovesCalculator.validPosition(moves, board.getPiece(myPosition), board.getPiece(newPosition), myPosition, newPosition, null);
        }

        return moves;
    }
}
