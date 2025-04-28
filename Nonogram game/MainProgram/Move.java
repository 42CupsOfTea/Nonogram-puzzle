package MainProgram;

import java.awt.*;

/**
 * Class defines an object that defines a 'move' of a user.
 */
public class Move {
    public final int row;         // Row number where the move was made
    public final int column;      // Column number where the move was made
    public final States states;   // The state of the cell after the move (FILLED, EMPTY, etc.)
    public final Color color;     // The color selected by the user for this cell (if colored nonogram)

    /**
     * Constructs an object which defines one {@code move} made by a user in the nonogram game.
     * This object stores the position (row, column), the updated state of the cell, and its color.
     * 
     * @param row Row number where the move was made.
     * @param column Column number where the move was made.
     * @param states Updated state of the cell (e.g., FILLED, EMPTY, etc.).
     * @param color Color selected by the user for the cell (applicable in colored nonograms).
     */
    public Move(int row, int column, States states, Color color) {
        this.row = row;
        this.column = column;
        this.states = states;
        this.color = color;
    }

    // Getter methods to retrieve the move's information (for undoing moves, etc.)

    /**
     * Gets the row of the move for undoing it.
     * @return The row number where the move was made.
     */
    public int getRowToUndo() {
        return row;
    }

    /**
     * Gets the column of the move for undoing it.
     * @return The column number where the move was made.
     */
    public int getColumnToUndo() {
        return column;
    }

    /**
     * Gets the state of the cell after the move, for undoing it.
     * @return The state of the cell (e.g., FILLED, EMPTY).
     */
    public States getStateToUndo() {
        return states;
    }

    /**
     * Gets the color of the cell after the move, for undoing it (relevant for colored nonograms).
     * @return The color of the cell.
     */
    public Color getColorToUndo() {
        return color;
    }
}
