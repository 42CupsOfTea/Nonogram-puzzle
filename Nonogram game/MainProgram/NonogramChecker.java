package MainProgram;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to check solutions for nonograms when the 'check solution' button is pressed.
 */
public class NonogramChecker {
    private final NonogramModel model;  // The model representing the current nonogram puzzle state
    int count = 0;

    /**
     * Constructs a checker object based on the provided NonogramModel.
     * The checker will use the model to validate if the puzzle is solved.
     * @param model The NonogramModel containing the puzzle state.
     */
    public NonogramChecker(NonogramModel model) {
        this.model = model;
    }

    /**
     * Checks if the entire nonogram puzzle has been solved.
     * @return true if all rows and columns are correct, otherwise false.
     */
    public boolean isSolved() {
        return checkAllRows() && checkAllColumns();  // Checks both rows and columns
    }

    /**
     * Returns a list of rows that are incorrectly filled in the nonogram.
     * This method identifies which rows are wrong based on the expected hint constraints.
     * @return List of row numbers (1-based) that are incorrect.
     */
    public List<Integer> getIncorrectRows() {
        List<Integer> incorrectRows = new ArrayList<>();
        for (int row = 0; row < model.getRows(); row++) {
            if (!checkRow(row)) {  // Check if each row matches its expected pattern
                incorrectRows.add(row + 1);  // Add 1 to make it 1-based index
            }
        }
        return incorrectRows;
    }

    /**
     * Returns a list of columns that are incorrectly filled in the nonogram.
     * This method identifies which columns are wrong based on the expected hint constraints.
     * @return List of column numbers (1-based) that are incorrect.
     */
    public List<Integer> getIncorrectColumns() {
        List<Integer> incorrectColumns = new ArrayList<>();
        for (int col = 0; col < model.getCols(); col++) {
            if (!checkColumn(col)) {  // Check if each column matches its expected pattern
                incorrectColumns.add(col + 1);  // Add 1 to make it 1-based index
            }
        }
        return incorrectColumns;
    }

    /**
     * Validates all rows of the nonogram, ensuring each row matches the expected hint.
     * @return true if all rows are correct, otherwise false.
     */
    public boolean checkAllRows() {
        for (int row = 0; row < model.getRows(); row++) {
            if (!checkRow(row)) {  // Check each individual row
                return false;
            }
        }
        return true;
    }

    /**
     * Validates all columns of the nonogram, ensuring each column matches the expected hint.
     * @return true if all columns are correct, otherwise false.
     */
    public boolean checkAllColumns() {
        for (int col = 0; col < model.getCols(); col++) {
            if (!checkColumn(col)) {  // Check each individual column
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a specific row matches its expected pattern of filled and empty cells, 
     * considering both regular and colored nonograms.
     * @param row The row index to be checked (0-based).
     * @return true if the row matches the expected pattern, otherwise false.
     */
    private boolean checkRow(int row) {
        // Get the expected hints (number of consecutive filled cells) for the row
        List<Integer> expected = model.getLevel().getRowOrColumnHints(model.getLevel().getRowHintColors()).get(row);
        // Get the expected colors for the row (only for colored nonograms)
        List<String> expectedColour = model.getLevel().getRowOrColumnColors(model.getLevel().getRowHintColors()).get(row);
        boolean isColouredNonogram = checkIfColouredNonogram(expectedColour);  // Check if the nonogram uses colors

        if (isColouredNonogram) {
            // For colored nonograms, create expected color groups and validate the row
            List<ColouredCellGroups> expectedGroups = createExpectedGroups(expected, expectedColour);
            return checkColouredLine(expectedGroups, row, true);
        } else {
            // For regular nonograms, just validate the expected block sizes
            return checkLine(expected, row, true);
        }
    }

    /**
     * Checks if a specific column matches its expected pattern of filled and empty cells, 
     * considering both regular and colored nonograms.
     * @param col The column index to be checked (0-based).
     * @return true if the column matches the expected pattern, otherwise false.
     */
    private boolean checkColumn(int col) {
        // Get the expected hints (number of consecutive filled cells) for the column
        List<Integer> expected = model.getLevel().getRowOrColumnHints(model.getLevel().getColHintColors()).get(col);
        // Get the expected colors for the column (only for colored nonograms)
        List<String> expectedColour = model.getLevel().getRowOrColumnColors(model.getLevel().getColHintColors()).get(col);
        boolean isColouredNonogram = checkIfColouredNonogram(expectedColour);  // Check if the nonogram uses colors

        if (isColouredNonogram) {
            // For colored nonograms, create expected color groups and validate the column
            List<ColouredCellGroups> expectedGroups = createExpectedGroups(expected, expectedColour);
            return checkColouredLine(expectedGroups, col, false);
        } else {
            // For regular nonograms, just validate the expected block sizes
            return checkLine(expected, col, false);
        }
    }

    /**
     * Checks if the nonogram has more than one color (other than black).
     * This helps determine if the nonogram is a colored nonogram.
     * @param expectedColour List of expected colors for the row or column.
     * @return true if the nonogram uses colors other than black, otherwise false.
     */
    private boolean checkIfColouredNonogram(List<String> expectedColour) {
        for (String colorStr : expectedColour) {
            Color color = Color.decode(colorStr);  // Convert hex color string to Color object
            if (!color.equals(Color.BLACK)) {  // If any color is not black, it's a colored nonogram
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a nonogram uses colored cells (i.e., colors other than black).
     * @param colorHints List of color hints (for checking if the nonogram is colored).
     * @return true if the nonogram uses colored cells, otherwise false.
     */
    public boolean checkColouredNonogram(List<String> colorHints) {
        return checkIfColouredNonogram(colorHints);
    }

    /**
     * Creates a list of expected colored cell groups from the given counts and colors.
     * Each group represents a series of consecutive filled cells of the same color.
     * @param counts List of consecutive cell counts.
     * @param colorStrings List of color strings representing the colors of the blocks.
     * @return A list of ColouredCellGroups representing the expected color groups.
     */
    private List<ColouredCellGroups> createExpectedGroups(List<Integer> counts, List<String> colorStrings) {
        List<ColouredCellGroups> groups = new ArrayList<>();
        for (int i = 0; i < counts.size(); i++) {
            Color color = Color.decode(colorStrings.get(i));  // Convert hex string to Color
            groups.add(new ColouredCellGroups(color, counts.get(i)));  // Add the group
        }
        return groups;
    }

    /**
     * Checks a line (either row or column) for a regular nonogram, where empty spaces are allowed between filled cells.
     * @param expected List of expected block sizes for the line.
     * @param index The index of the line (either row or column).
     * @param isRow Flag indicating if this is a row (true) or column (false).
     * @return true if the line matches the expected pattern, otherwise false.
     */
    private boolean checkLine(List<Integer> expected, int index, boolean isRow) {
        int currentRun = 0;  // Length of the current filled block
        int expectedIndex = 0;  // Index to track the expected block in the list
        int length = isRow ? model.getCols() : model.getRows();  // Get the length of the line (row or column)

        // Loop through each cell in either a row or column
        for (int i = 0; i < length; i++) {
            States cellState = isRow ? model.getCellState(index, i) : model.getCellState(i, index);  // Get cell state
            if (cellState == States.FILLED) {
                currentRun++;  // Increment the current block length
            }
            // When an empty or unknown cell is encountered after a filled block, validate the current run
            else if (currentRun > 0) {
                if (expectedIndex >= expected.size() || currentRun != expected.get(expectedIndex)) {
                    return false;  // Mismatch in expected block size
                }
                expectedIndex++;
                currentRun = 0;
            }
        }

        // Check the last run after the loop ends, which handles the case where the line ends with FILLED cells
        if (currentRun > 0) {
            if (expectedIndex >= expected.size() || currentRun != expected.get(expectedIndex)) {
                return false;  // Mismatch in expected block size
            }
            expectedIndex++;
        }

        // Ensure that all expected runs were found
        return expectedIndex == expected.size();
    }

    /**
     * Checks a line (either row or column) for a colored nonogram.
     * Compares the extracted color groups to the expected color groups.
     * @param expectedGroups List of expected color groups.
     * @param index The index of the line (either row or column).
     * @param isRow Flag indicating if this is a row (true) or column (false).
     * @return true if the color groups match, otherwise false.
     */
    private boolean checkColouredLine(List<ColouredCellGroups> expectedGroups, int index, boolean isRow) {
        List<ColouredCellGroups> actualGroups = extractColorGroups(index, isRow);  // Extract actual color groups
        if (expectedGroups.size() != actualGroups.size()) {
            return false;  // Different number of groups
        }

        // Compare each color group
        for (int i = 0; i < expectedGroups.size(); i++) {
            ColouredCellGroups expected = expectedGroups.get(i);
            ColouredCellGroups actual = actualGroups.get(i);

            if (!expected.equals(actual)) {
                return false;  // Mismatch in color group
            }
        }
        return true;
    }

    /**
     * Extracts the color groups from a specific row or column.
     * Each group represents consecutive filled cells of the same color.
     * @param index The index of the row or column.
     * @param isRow Flag indicating if this is a row (true) or column (false).
     * @return List of ColouredCellGroups representing the actual color groups in the line.
     */
    private List<ColouredCellGroups> extractColorGroups(int index, boolean isRow) {
        List<ColouredCellGroups> groups = new ArrayList<>();
        Color currentColour = null;
        int currentCount = 0;
        int length = isRow ? model.getCols() : model.getRows();

        // Iterate through each cell in the line (row or column)
        for (int i = 0; i < length; i++) {
            States cellState = isRow ? model.getCellState(index, i) : model.getCellState(i, index);  // Get cell state
            Color cellColour = isRow ? model.getGridColor(index, i) : model.getGridColor(i, index);  // Get cell color

            if (cellState == States.FILLED) {
                // If we encounter a new color, end the current group and start a new one
                if (!cellColour.equals(currentColour)) {
                    if (currentCount > 0) {
                        groups.add(new ColouredCellGroups(currentColour, currentCount));
                    }
                    currentColour = cellColour;
                    currentCount = 1;
                } else {
                    currentCount++;  // Continue the current color group
                }
            } else {
                // End of a filled block, add the current group if there is one
                if (currentCount > 0) {
                    groups.add(new ColouredCellGroups(currentColour, currentCount));
                    currentColour = null;
                    currentCount = 0;
                }
            }
        }
        // Add the last group if exists
        if (currentCount > 0) {
            groups.add(new ColouredCellGroups(currentColour, currentCount));
        }
        return groups;
    }
}

/* Edge Cases Handled:
    - Empty line: If expected is empty, only returns true if no FILLED cells are present.
    - Too many blocks: If more runs than expected are found, returns false.
    - Incomplete puzzle: Only checks FILLED cells (UNKNOWN treated as breaks).
    - Wrong block sizes: If any block size mismatch occurs, returns false immediately.
*/
