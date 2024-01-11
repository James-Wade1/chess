package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int pieceRow;
    private int pieceColumn;
    public ChessPosition(int row, int col) {
        if (row >= 1 && row <= 8) {
            pieceRow = --row;
        }
        else {
            throw new RuntimeException("Row is out of bounds");
        }

        if (col >= 1 && col <= 8) {
            pieceColumn = --col;
        }
        else {
            throw new RuntimeException("Column is out of bounds");
        }
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return pieceRow;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return pieceColumn;
    }
}
