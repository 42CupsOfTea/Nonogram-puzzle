package MainProgram;

/**
 * Enum representing the possible states of a nonogram grid cell.
 */
public enum States {
    FILLED,  // The cell is filled (marked with a color or symbol)
    EMPTY,   // The cell is empty (not filled)
    UNKNOWN // The state of the cell is unknown (not yet determined)
}
