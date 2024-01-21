package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        ChessPiece myPositionPiece = board.getPiece(myPosition);

        if (myPositionPiece.getTeamColor() == ChessGame.TeamColor.WHITE) { //Move up

            if (myRow == 7) { // Promotion possible
                ChessPiece.PieceType[] promotionPieces = { ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT };
                int row = myRow + 1;
                for (int col = myCol - 1; col <= myCol + 1; col++) {
                    ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
                    if (col != myCol) { // A diagonal move
                        if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                            for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                            }
                        }
                    }
                    else {
                        if (newPositionPiece == null) {
                            for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                            }
                        }
                    }
                }
            }
            else { // Promotion not possible
                int row = myRow + 1;
                for (int col = myCol - 1; col <= myCol + 1; col++) {
                    ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
                    if (col != myCol) { // A diagonal move
                        if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                        }
                    }
                    else {
                        if (newPositionPiece == null) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                            if (myRow == 2 && board.getPiece(new ChessPosition(row + 1, col)) == null) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                            }
                        }
                    }
                }
            }
        }
        else { //If it is a black piece
            if (myRow == 2) {
                ChessPiece.PieceType[] promotionPieces = { ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT };
                int row = myRow - 1;
                for (int col = myCol - 1; col <= myCol + 1; col++) {
                    ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
                    if (col != myCol) { // A diagonal move
                        if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                            for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                            }
                        }
                    }
                    else {
                        if (newPositionPiece == null) {
                            for (ChessPiece.PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                            }
                        }
                    }
                }
            }
            else {
                int row = myRow - 1;
                for (int col = myCol - 1; col <= myCol + 1; col++) {
                    ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
                    if (col != myCol) { // A diagonal move
                        if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                        }
                    } else {
                        if (newPositionPiece == null) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                            if (myRow == 7 && board.getPiece(new ChessPosition(row - 1, col)) == null) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }
}
