package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        int row = myRow;
        int col = myCol;

        //Going up
        outer:
        while (row <= 8) {
            row++;
            if (row > 8) {
                break outer;
            } //break if coordinates are out of bounds
            ChessPosition newPosition = new ChessPosition(row,col);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                break outer;
            }
        }

        row = myRow;

        //Going down
        outer:
        while (row >= 1) {
            row--;
            if (row < 1) {
                break outer;
            } //break if coordinates are out of bounds
            ChessPosition newPosition = new ChessPosition(row,col);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                break outer;
            }
        }

        row = myRow;

        //Going left
        outer:
        while (col >= 1) {
            col--;
            if (col < 1) {
                break outer;
            } //break if coordinates are out of bounds
            ChessPosition newPosition = new ChessPosition(row,col);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                break outer;
            }
        }

        col = myCol;

        //Going right
        outer:
        while (col <= 8) {
            col++;
            if (col > 8) {
                break outer;
            } //break if coordinates are out of bounds
            ChessPosition newPosition = new ChessPosition(row,col);
            ChessPiece newPositionPiece = board.getPiece(newPosition);
            if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                break outer;
            }
        }

        return moves;
    }
}
