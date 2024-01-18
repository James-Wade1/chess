package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        ArrayList<ChessPosition> spotsAroundKnight = new ArrayList<ChessPosition>();

        for (int row = myRow - 2; row <= myRow + 2; row += 4) {
            for (int col = myCol - 1; col <= myCol + 1; col += 2) {
                if (row > 0 && row < 9 && col > 0 && col < 9) {
                    spotsAroundKnight.add(new ChessPosition(row, col));
                }
            }
        }

        for (int col = myCol - 2; col <= myCol + 2; col += 4) {
            for (int row = myRow - 1; row <= myRow + 1; row += 2) {
                if (row > 0 && row < 9 && col > 0 && col < 9) {
                    spotsAroundKnight.add(new ChessPosition(row, col));
                }
            }
        }

        for (ChessPosition newPosition : spotsAroundKnight) {
            PieceMovesCalculator.validPosition(moves, board.getPiece(myPosition), board.getPiece(newPosition), myPosition, newPosition, null);
        }

        return moves;
    }
}
