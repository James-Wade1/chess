package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessPiece.PieceType pieceType;
    private ChessGame.TeamColor teamColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        teamColor = pieceColor;
        pieceType = type;
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

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(myPosition) == null) {
            return null;
        }
        PieceType type = board.getPiece(myPosition).getPieceType();
        Collection<ChessMove> moves;

        moves = switch (type) {
            case PieceType.PAWN -> pawnMoves(board, myPosition);
            case PieceType.ROOK -> rookMoves(board, myPosition);
            case PieceType.KNIGHT -> knightMoves(board, myPosition);
            case PieceType.BISHOP -> bishopMoves(board, myPosition);
            case PieceType.QUEEN -> queenMoves(board, myPosition);
            case PieceType.KING -> kingMoves(board, myPosition);
        };

        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        ChessPiece myPositionPiece = board.getPiece(myPosition);

        if (myPositionPiece.getTeamColor() == ChessGame.TeamColor.WHITE) { //Move up

            if (myRow == 7) {
                PieceType[] promotionPieces = { PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT };
                int row = myRow + 1;
                for (int col = myCol - 1; col <= myCol + 1; col++) {
                    ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
                    if (col != myCol) { // A diagonal move
                        if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                            for (PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                            }
                        }
                    }
                    else {
                        if (newPositionPiece == null) {
                            for (PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                            }
                        }
                    }
                }
            }
            else {
                int row = myRow + 1;
                for (int col = myCol - 1; col <= myCol + 1; col++) {
                    ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
                    if (col != myCol) { // A diagonal move
                        if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                        }
                    } else {
                        if (newPositionPiece == null) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row, col), null));
                        }
                        if (myRow == 2 && board.getPiece(new ChessPosition(row+1, col)) == null) {
                            moves.add(new ChessMove(myPosition, new ChessPosition(row+1, col), null));
                        }
                    }
                }
            }
        }
        else { //If it is a black piece
            if (myRow == 2) {
                PieceType[] promotionPieces = { PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT };
                int row = myRow - 1;
                for (int col = myCol - 1; col <= myCol + 1; col++) {
                    ChessPiece newPositionPiece = board.getPiece(new ChessPosition(row, col));
                    if (col != myCol) { // A diagonal move
                        if (newPositionPiece != null && newPositionPiece.getTeamColor() != myPositionPiece.getTeamColor()) {
                            for (PieceType promotionPiece : promotionPieces) {
                                moves.add(new ChessMove(myPosition, new ChessPosition(row, col), promotionPiece));
                            }
                        }
                    }
                    else {
                        if (newPositionPiece == null) {
                            for (PieceType promotionPiece : promotionPieces) {
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

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
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
            if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
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
            if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
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
            if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
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
            if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                break outer;
            }
        }

        return moves;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
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
            validPosition(moves, board.getPiece(myPosition), board.getPiece(newPosition), myPosition, newPosition, null);
        }

        return moves;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        // Note: col is the vertical axis on the chessboard

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
                if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

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
                if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

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
                if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        row = myPosition.getRow();
        col = myPosition.getColumn();

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
                if (!boolValidPosition(moves, board.getPiece(myPosition), newPositionPiece, myPosition, newPosition, null)) {
                    break outer;
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
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
            validPosition(moves, board.getPiece(myPosition), board.getPiece(newPosition), myPosition, newPosition, null);
        }

        return moves;
    }

    private boolean boolValidPosition(HashSet<ChessMove> moves, ChessPiece myPositionPiece, ChessPiece newPositionPiece, ChessPosition myPosition, ChessPosition newPosition, PieceType promotionPiece) {
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

    private void validPosition(HashSet<ChessMove> moves, ChessPiece myPositionPiece, ChessPiece newPositionPiece, ChessPosition myPosition, ChessPosition newPosition, PieceType promotionPiece) {
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
}