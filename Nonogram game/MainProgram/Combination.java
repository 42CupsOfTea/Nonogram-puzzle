package MainProgram;

import java.util.List;

/**
 * Class represents a certain {@code List<StateAndColor> } line used in the {@code DeductiveSolver}
 */
public class Combination {
    private List<StateAndColor> line; // List representing a row or column, each element contains a state and color

    /**
     * Constructor to initialize a combination (row or column) of cells.
     * @param line the list of StateAndColor objects representing the combination
     */
    public Combination(List<StateAndColor> line) {
        this.line = line;
    }

    /**
     * Returns the list representing the combination (row or column).
     * @return the list of StateAndColor objects
     */
    public List<StateAndColor> getCombination() {
        return line;
    }

}
