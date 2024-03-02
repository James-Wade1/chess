package chess;

import java.util.Objects;

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
            pieceRow = row;
        }
        else {
            throw new RuntimeException("Row is out of bounds");
        }

        if (col >= 1 && col <= 8) {
            pieceColumn = col;
        }
        else {
            throw new RuntimeException("Column is out of bounds");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return pieceRow == that.pieceRow && pieceColumn == that.pieceColumn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceRow, pieceColumn);
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

    public boolean isAdjacentCol(ChessPosition otherPosition) {
        return (this.getColumn() == otherPosition.getColumn()-1 || this.getColumn() == otherPosition.getColumn()+1) && this.getRow() == otherPosition.getRow();
    }
}
