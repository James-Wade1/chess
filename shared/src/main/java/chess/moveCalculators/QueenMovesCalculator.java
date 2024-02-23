package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();

        moves.addAll(BishopMovesCalculator.pieceMoves(board, myPosition));
        moves.addAll(RookMovesCalculator.pieceMoves(board, myPosition));

        return moves;
    }
}
