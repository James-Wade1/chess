package chess;

import java.util.Collection;
import java.util.HashSet;

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
        ChessPiece chessPiece = board.getPiece(startPosition);
        if (chessPiece == null) {
            return null;
        }
        ChessPiece.PieceType type = chessPiece.getPieceType();
        Collection<ChessMove> moves;

        moves = switch (type) {
            case ChessPiece.PieceType.PAWN -> PawnMovesCalculator.pieceMoves(board, startPosition);
            case ChessPiece.PieceType.ROOK -> RookMovesCalculator.pieceMoves(board, startPosition);
            case ChessPiece.PieceType.KNIGHT -> KnightMovesCalculator.pieceMoves(board, startPosition);
            case ChessPiece.PieceType.BISHOP -> BishopMovesCalculator.pieceMoves(board, startPosition);
            case ChessPiece.PieceType.QUEEN -> QueenMovesCalculator.pieceMoves(board, startPosition);
            case ChessPiece.PieceType.KING -> KingMovesCalculator.pieceMoves(board, startPosition);
        };

        /*
        If the king is in check, and...
            - the piece to be moved is the king, check to see if he is in checkmate, if not, find the valid moves he can make
            - the piece to be moved is not the king, its set of moves has to bring the king out of check, either by
                1) killing the piece that has it in check (which if there is more than one, this won't be valid)
                2) blocking the piece that has it in check (which if there is more than one, this won't be valid)
                Note: if the king is doubly in check, then you have to move the king. Therefore, if the king is doubly in check, and the selected piece to move is not the king, then there are no valid moves
         */

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
        HashSet<ChessMove> validMoves = (HashSet<ChessMove>) validMoves(currentPosition);

        if (validMoves.contains(move)) {
            ChessPiece chessPiece = board.getPiece(currentPosition);
            //If enemy piece is on finalPosition, then remove that piece and place team's piece there
            //If enemy piece in not on finalPosition, then just place that piece in the new spot
            //If promotion piece is not null, change piece type
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
        throw new RuntimeException("Not implemented");
        //Maybe monitor this by having a hashset of all the pieces that have the King in their line of sight? But that would require a lot of computations
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //If not in check, then return false
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //Keep a hashset in chessboard which keeps track of all the pieces remaining. When they're captured, remove them from this hashset. Iterate through all of the remaining pieces to see if there are valid moves left
        //Assuming this method and the check/checkmate methods above are called at the beginning of each round, you could also store the validMoves in a dictionary for each piece to be called later
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
}
