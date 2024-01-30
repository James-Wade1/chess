package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = board.getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        boolean isKing = (piece.getPieceType() == ChessPiece.PieceType.KING);

        HashSet<ChessMove> moves = piece.pieceMoves(board, startPosition);
        ChessBoard boardCopy = new ChessBoard(this.board); //Store the old board

        for (Iterator<ChessMove> i = moves.iterator(); i.hasNext();) {
            ChessMove move = i.next();
            ChessPosition currentPositionCol = move.getStartPosition();
            ChessPosition endPositionCol = move.getEndPosition();
            if (isKing && isCastlingMove(move)) {
                moveCastling(move);
            }
            else {
                movePiece(move);
            }
            if (isInCheck(piece.getTeamColor())) {
                i.remove();
            }


            board = new ChessBoard(boardCopy);
        }

        board = boardCopy;

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition currentPosition = move.getStartPosition();
        ChessPiece currentPiece = board.getPiece(currentPosition);

        if (currentPiece != null && getTeamTurn() == currentPiece.getTeamColor()) {
            HashSet<ChessMove> validMoves = (HashSet<ChessMove>) validMoves(currentPosition);
            if (validMoves.contains(move)) {
                if (currentPiece.getPieceType() == ChessPiece.PieceType.KING && !currentPiece.isHasMoved() && isCastlingMove(move)) {
                    currentPiece.setHasMoved(true);
                    //Set the rook so that it's marked as having moved
                    moveCastling(move);
                }
                else {
                    movePiece(move);
                    currentPiece.setHasMoved(true);
                    }
                if (move.getPromotionPiece() != null) {
                    board.getPiece(move.getEndPosition()).setPieceType(move.getPromotionPiece());
                }
                if (getTeamTurn() == TeamColor.WHITE) {
                    setTeamTurn(TeamColor.BLACK);
                }
                else {
                    setTeamTurn(TeamColor.WHITE);
                }
            }
            else {
                throw new InvalidMoveException();
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        HashSet<ChessPosition> opponentPieces;

        ChessPosition kingPosition = board.getKingPosition(teamColor);

        if (teamColor == TeamColor.WHITE) {
            opponentPieces = board.getColorPieces(TeamColor.BLACK);
        }
        else {
            opponentPieces = board.getColorPieces(TeamColor.WHITE);
        }

        for (ChessPosition position : opponentPieces) {
            HashSet<ChessMove> potentialMoves = board.getPiece(position).pieceMoves(board,position);
            for (ChessMove potentialMove : potentialMoves) {
                if (potentialMove.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
        //Maybe monitor this by having a hashset of all the pieces that have the King in their line of sight? But that would require a lot of computations
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        HashSet<ChessMove> potentialMoves = board.getPiece(kingPosition).pieceMoves(board,kingPosition);

        return isInCheck(teamColor) && isInStalemate(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);
        HashSet<ChessMove> potentialMoves = board.getPiece(kingPosition).pieceMoves(board,kingPosition);

        ChessBoard boardCopy = new ChessBoard(board);

        for (ChessMove potentialMove : potentialMoves) {
            movePiece(potentialMove);
            if (!isInCheck(teamColor)) {
                return false;
            }

            board = new ChessBoard(boardCopy);
        }

        board = boardCopy;

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private void movePiece(ChessMove move) {
        board.removePiece(move.getEndPosition());
        board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
        board.removePiece(move.getStartPosition());
    }

    private boolean isCastlingMove(ChessMove move) {
        ChessPosition currentPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        return (currentPosition.getColumn() == endPosition.getColumn() + 2) || (currentPosition.getColumn() == endPosition.getColumn() - 2);
    }

    private void moveCastling(ChessMove move) {
        ChessPiece king = board.getPiece(move.getStartPosition());
        ChessPosition kingPosition = move.getStartPosition();
        ChessPosition newKingPosition = move.getEndPosition();
        ChessPosition rookPosition;
        ChessPosition newRookPosition;

        if (newKingPosition.getColumn() > kingPosition.getColumn()) {
            rookPosition = new ChessPosition(kingPosition.getRow(), 8);
            newRookPosition = new ChessPosition(kingPosition.getRow(), 6);
        }
        else {
            rookPosition = new ChessPosition(kingPosition.getRow(), 1);
            newRookPosition = new ChessPosition(kingPosition.getRow(), 4);
        }
        ChessPiece rook = board.getPiece(rookPosition);

        king.setHasMoved(true);
        rook.setHasMoved(true);

        board.addPiece(newKingPosition, king);
        board.removePiece(kingPosition);
        board.addPiece(newRookPosition,rook);
        board.removePiece(rookPosition);
    }
}
