package MainProgram;

import java.awt.*;

/**
 * Holds a grid cell, containing a state and color.
 */
public class StateAndColor {
    private States currentState;  // The state of the cell (e.g., EMPTY, FILLED, UNKNOWN)
    private Color color;          // The color of the cell

    /**
     * Constructor to initialize the state and color of a grid cell.
     * @param currentState The state of the cell.
     * @param color The color of the cell.
     */
    public StateAndColor(States currentState, Color color) {
        this.currentState = currentState;
        this.color = color;
    }

    /**
     * Getter for the current state of the cell.
     * @return The current state of the cell.
     */
    public States getCurrentState() {
        return currentState;
    }

    /**
     * Getter for the color of the cell.
     * @return The color of the cell.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for the color of the cell.
     * @param color The color to set for the cell.
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Setter for the state of the cell.
     * @param currentState The state to set for the cell.
     */
    public void setCurrentState(States currentState) {
        this.currentState = currentState;
    }
}
