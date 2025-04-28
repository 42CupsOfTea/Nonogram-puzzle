package MainProgram;

import java.util.List;

/**
 * Represents the search space for a nonogram puzzle, holding valid combinations of filled cells 
 * for rows and columns.
 */
public class SearchSpace {
    private List<List<Combination>> rowCombinations;  // List holding valid combinations for rows
    private List<List<Combination>> colCombinations;  // List holding valid combinations for columns

    /**
     * Constructor to initialize the SearchSpace object with row and column combinations.
     * @param rowCombinations A list of valid row combinations.
     * @param colCombinations A list of valid column combinations.
     */
    SearchSpace (List<List<Combination>> rowCombinations, List<List<Combination>> colCombinations) {
        this.rowCombinations = rowCombinations;
        this.colCombinations = colCombinations;
    }

    /**
     * Getter for column combinations.
     * @return A list of valid column combinations.
     */
    public List<List<Combination>> getColCombinations() {
        return colCombinations;
    }

    /**
     * Getter for row combinations.
     * @return A list of valid row combinations.
     */
    public List<List<Combination>> getRowCombinations() {
        return rowCombinations;
    }

    /**
     * Setter for column combinations.
     * @param colCombinations A list of valid column combinations.
     */
    public void setColCombinations(List<List<Combination>> colCombinations) {
        this.colCombinations = colCombinations;
    }

    /**
     * Setter for row combinations.
     * @param rowCombinations A list of valid row combinations.
     */
    public void setRowCombinations(List<List<Combination>> rowCombinations) {
        this.rowCombinations = rowCombinations;
    }
}
