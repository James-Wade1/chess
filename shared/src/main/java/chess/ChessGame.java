package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import static chess.ChessPiece.PieceType.PAWN;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    boolean previousMoveDoublePawn;

    ChessPosition previousMoveDoublePawnLocation;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        previousMoveDoublePawn = false;
        previousMoveDoublePawnLocation = null;
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
        boolean isPawn = (piece.getPieceType() == PAWN);

        HashSet<ChessMove> moves = piece.pieceMoves(board, startPosition);
        ChessBoard boardCopy = new ChessBoard(this.board); //Store the old board

        for (Iterator<ChessMove> i = moves.iterator(); i.hasNext();) {
            ChessMove move = i.next();
            ChessPosition currentPositionCol = move.getStartPosition();
            ChessPosition endPositionCol = move.getEndPosition();
            if (isKing && isCastlingMove(move)) {
                moveCastling(move);
                if (isInCheck(piece.getTeamColor()) || isInvalidCastling(piece.getTeamColor())) {
                    i.remove();
                }
            }
            else {
                movePiece(move);
                if (isInCheck(piece.getTeamColor())) {
                    i.remove();
                }
            }


            board = new ChessBoard(boardCopy);
        }

        /* Check for en passant valid move */
        if (isPawn && previousMoveDoublePawn) {
            ChessPosition passingPawn = previousMoveDoublePawnLocation;
            if (startPosition.isAdjacentCol(passingPawn)) { // Checks if it's an en passant move
                ChessMove move = getEnPassantMove(startPosition, previousMoveDoublePawnLocation);
                moveEnPassant(move);
                if (!isInCheck(piece.getTeamColor())) {
                    moves.add(move);
                }
            }
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
                if (currentPiece.getPieceType() == ChessPiece.PieceType.KING  && isCastlingMove(move)) {
                    board.getPiece(move.getStartPosition()).setHasMoved(true);
                    if (move.getEndPosition().getColumn() > currentPosition.getColumn()) {
                        board.getPiece(new ChessPosition(currentPosition.getRow(), 8)).setHasMoved(true);
                    }
                    else {
                        board.getPiece(new ChessPosition(currentPosition.getRow(), 1)).setHasMoved(true);
                    }
                    moveCastling(move);
                    previousMoveDoublePawn = false;
                    previousMoveDoublePawnLocation = null;
                }
                else if (currentPiece.getPieceType() == PAWN && isEnPassantMove(move)) {
                    moveEnPassant(move);
                    previousMoveDoublePawn = false;
                    previousMoveDoublePawnLocation = null;
                }
                else if (currentPiece.getPieceType() == PAWN && isDoublePawnMove(move)) {
                    movePiece(move);
                    previousMoveDoublePawnLocation = move.getEndPosition();
                    previousMoveDoublePawn = true;
                }
                else {
                    board.getPiece(move.getStartPosition()).setHasMoved(true);
                    movePiece(move);
                    previousMoveDoublePawn = false;
                    previousMoveDoublePawnLocation = null;
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
        int currentPositionCol = move.getStartPosition().getColumn();
        int endPositionCol = move.getEndPosition().getColumn();
        ChessPosition rookPosition;

        ChessPiece king = board.getPiece(move.getStartPosition());
        if (currentPositionCol < endPositionCol) { // King's side castle
            rookPosition = new ChessPosition(move.getStartPosition().getRow(), 8);
        }
        else {
            rookPosition = new ChessPosition(move.getStartPosition().getRow(), 1);
        }

        ChessPiece rook = board.getPiece(rookPosition);

        return (currentPositionCol == endPositionCol + 2) || (currentPositionCol == endPositionCol - 2) && !king.isHasMoved() && !rook.isHasMoved();
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

    public boolean isInvalidCastling(TeamColor teamColor) {
        HashSet<ChessPosition> opponentPieces;

        ChessPosition kingPosition = board.getKingPosition(teamColor);
        ChessPosition rookPosition;

        if (board.getPiece(new ChessPosition(kingPosition.getRow(), kingPosition.getColumn()-1)) != null) {
            rookPosition = new ChessPosition(kingPosition.getRow(), kingPosition.getColumn()-1);
        }
        else {
            rookPosition = new ChessPosition(kingPosition.getRow(), kingPosition.getColumn()+1);
        }

        if (teamColor == TeamColor.WHITE) {
            opponentPieces = board.getColorPieces(TeamColor.BLACK);
        }
        else {
            opponentPieces = board.getColorPieces(TeamColor.WHITE);
        }

        for (ChessPosition position : opponentPieces) {
            HashSet<ChessMove> potentialMoves = board.getPiece(position).pieceMoves(board,position);
            for (ChessMove potentialMove : potentialMoves) {
                if (potentialMove.getEndPosition().equals(rookPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDoublePawnMove(ChessMove move) {
        ChessGame.TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        int homeRow;
        int doubleMoveRow;

        if (color == TeamColor.WHITE) {
            homeRow = 2;
            doubleMoveRow = 4;
        }
        else {
            homeRow = 7;
            doubleMoveRow = 5;
        }

        return move.getStartPosition().getRow() == homeRow && move.getEndPosition().getRow() == doubleMoveRow;
    }

    public ChessMove getEnPassantMove(ChessPosition startPosition, ChessPosition previousMoveDoublePawnLocation) {
        ChessMove move;

        TeamColor color = board.getPiece(startPosition).getTeamColor();

        int currentColumn = startPosition.getColumn();
        int passingPawnColumn = previousMoveDoublePawnLocation.getColumn();

        if (color == TeamColor.WHITE) {
            move = new ChessMove(startPosition, new ChessPosition(6,passingPawnColumn),null);
        }
        else {
            move = new ChessMove(startPosition, new ChessPosition(3,passingPawnColumn),null);
        }

        return move;
    }

    public boolean isEnPassantMove(ChessMove move) {
        return board.getPiece(move.getEndPosition()) == null && move.getEndPosition().getColumn() != move.getStartPosition().getColumn();
    }

    private void moveEnPassant(ChessMove move) {
        ChessPosition currentPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece teamPiece = board.getPiece(currentPosition);
        TeamColor color = teamPiece.getTeamColor();

        movePiece(move);

        if (color == TeamColor.WHITE) {
            board.removePiece(new ChessPosition(endPosition.getRow()-1,endPosition.getColumn()));
        }
        else {
            board.removePiece(new ChessPosition(endPosition.getRow()+1,endPosition.getColumn()));
        }
    }
}
