package MainProgram;
import java.util.List;

/**
 * Class stores information on the result following the {@code NonogramChecker} checking a nonogram solution
 */
public class CheckResult {
    private final boolean isSolved;
    private final List<Integer> incorrectRows;
    private final List<Integer> incorrectColumns;

    /**
     * Constructor to check the nonogram solution status.
     * @param isSolved whether the nonogram is solved
     * @param incorrectRows list of rows that are incorrect
     * @param incorrectColumns list of columns that are incorrect
     */
    public CheckResult(boolean isSolved, List<Integer> incorrectRows, 
                      List<Integer> incorrectColumns) {
        this.isSolved = isSolved;
        this.incorrectRows = incorrectRows;
        this.incorrectColumns = incorrectColumns;
    }

    /**
     * Is the nonogram solved?
     * @return if the nonogram is solved or not
     */
    public boolean isSolved() { return isSolved; }

    /**
     * Gets the incorrect rows of a solution (if they exist)
     * @return incorrectRows
     */
    public List<Integer> getIncorrectRows() { return incorrectRows; }

    /**
     * Gets the incorrect columns of a solution (if they exist)
     * @return incorrectColumns
     */
    public List<Integer> getIncorrectColumns() { return incorrectColumns; }
}
