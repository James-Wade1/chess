package chess;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements PieceMovesCalculator {
    static public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        // Note: col is the vertical axis on the chessboard

        int row = myRow;
        int col = myCol;
        //Traveling to top right
        outer:
        while(row <= 8) {
            while(col <= 8) {
                row++;
                col++;
                if (col > 8 || row > 8) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(row,col);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        row = myRow;
        col = myCol;

        // Go to top left
        outer:
        while(row <= 8) {
            while(col >= 1) {
                row++;
                col--;
                if (col < 1 || row > 8) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(row,col);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        row = myRow;
        col = myCol;

        // Go to bottom right
        outer:
        while(row >= 1) {
            while(col <= 8) {
                row--;
                col++;
                if (col > 8 || row < 1) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(row,col);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        row = myRow;
        col = myCol;

        // Go to bottom left
        outer:
        while(row >= 1) {
            while(col >= 1) {
                row--;
                col--;
                if (col < 1 || row < 1) {
                    break outer;
                } //break if coordinates are out of bounds
                ChessPosition newPosition = new ChessPosition(row,col);
                ChessPiece newPositionPiece = board.getPiece(newPosition);
                if (!PieceMovesCalculator.boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        return moves;
    }
}
