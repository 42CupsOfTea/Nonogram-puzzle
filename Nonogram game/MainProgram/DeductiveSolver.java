package MainProgram;

import Screen.GameUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is used to deductively solve nonogram puzzles
 */
public class DeductiveSolver {
    private final NonogramModel model;
    private final GameUI GUI;

    /**
     * Constructor to initialize the solver with the model and GUI.
     * @param model the nonogram game model
     * @param GUI the graphical user interface for updating the grid
     */
    public DeductiveSolver(NonogramModel model, GameUI GUI) {
        this.model = model;
        this.GUI = GUI;
    }

    /**
     * Main solving method to start deduction process.
     */
    public void solve(){
        SearchSpace combinations = getSearchSpace();  // Generate row and column combinations
        deduce(combinations);  // Deduce the solution based on these combinations
    }

    /**
     * Retrieves all row and column combinations for the nonogram.
     * @return SearchSpace object containing row and column combinations
     */
    public SearchSpace getSearchSpace() {
        List<List<HintNumberAndColour>> rows = model.getLevel().getRowHintColors();
        List<List<HintNumberAndColour>> columns = model.getLevel().getColHintColors();
     
        // Generate combinations for rows
        List<List<Combination>> rowCombinations = new ArrayList<>();
        for (List<HintNumberAndColour> rowHint : rows) {
            rowCombinations.add(generateCombinations(rowHint, model.getCols()));
        }

        // Generate combinations for columns    
        List<List<Combination>> colCombinations = new ArrayList<>();
        for (List<HintNumberAndColour> colHint : columns) {
            colCombinations.add(generateCombinations(colHint, model.getRows()));
        }

        return new SearchSpace(rowCombinations, colCombinations);
    }

    /**
     * Generates all possible valid combinations for a given hint and line length.
     * @param hints the hints describing the filled and empty cells
     * @param lineLength the total length of the line (row or column)
     * @return list of valid combinations
     */
    private List<Combination> generateCombinations(List<HintNumberAndColour> hints, int lineLength) {
        List<Combination> validCombinations = new ArrayList<>();
        generate(lineLength, hints, 0, 0, new ArrayList<>(), validCombinations);
        return validCombinations;
    }

    /**
     * Recursively generates all valid combinations based on given hints and current position.
     * @param lineLength total length of the line (row or column)
     * @param hints the hints for the current line
     * @param hintIndex current index in the hint list
     * @param currentPos current position in the line
     * @param current list of current filled/empty cells
     * @param results list of valid combinations generated so far
     */
    private void generate(int lineLength, List<HintNumberAndColour> hints, 
                      int hintIndex, int currentPos, 
                      List<StateAndColor> current, List<Combination> results) {
        // Base case: if all hints have been processed, fill the remaining spaces with EMPTY
        if (hintIndex >= hints.size()) {
            int baseSizeBefore = current.size(); 
            while (current.size() < lineLength) {
                current.add(new StateAndColor(States.EMPTY, Color.decode(Constants.EMPTY_COLOUR)));
            }
            results.add(new Combination(new ArrayList<>(current))); // Add the valid combination
            current.subList(baseSizeBefore, current.size()).clear();  // Restore state
            return;
        }

        HintNumberAndColour currentHint = hints.get(hintIndex);
        int hint = currentHint.getHint();
        String currentColor = currentHint.getColour();
        int maxStart = lineLength - remainingSpaceRequired(hints, hintIndex);

        // Try all possible starting positions for the current hint
        for (int start = currentPos; start <= maxStart; start++) {
            int elementsAddedThisIteration = 0;

            // Add EMPTY cells before the current hint
            for (int i = currentPos; i < start; i++) {
                current.add(new StateAndColor(States.EMPTY, Color.decode(Constants.EMPTY_COLOUR)));
                elementsAddedThisIteration++;
            }

            // Add FILLED cells for the current hint
            for (int i = 0; i < hint; i++) {
                current.add(new StateAndColor(States.FILLED, Color.decode(currentColor)));
                elementsAddedThisIteration++;
            }

            int nextPos = start + hint;

            // If there's a next hint with the same color, add an EMPTY cell between hints
            if (hintIndex < hints.size() - 1) {
                String nextColor = hints.get(hintIndex + 1).getColour();
                if (currentColor.equals(nextColor)) {
                    current.add(new StateAndColor(States.EMPTY, Color.decode(Constants.EMPTY_COLOUR)));
                    elementsAddedThisIteration++;
                    nextPos++;
                }
            }

            // Recurse to the next hint
            generate(lineLength, hints, hintIndex + 1, nextPos, current, results);

            // Restore the state after recursion
            current.subList(current.size() - elementsAddedThisIteration, current.size()).clear();
        }
    }
    
    /**
     * Calculates the remaining space required for all subsequent hints.
     * @param hints the list of hints
     * @param currentIndex the current hint index
     * @return the total remaining space needed for the hints from current index onwards
     */
    private int remainingSpaceRequired(List<HintNumberAndColour> hints, int currentIndex) {
        int space = 0;
        int requiredSpaces = 0;
    
        for (int i = currentIndex; i < hints.size(); i++) {
            space += hints.get(i).getHint();
            if (i < hints.size() - 1) {
                String currentColor = hints.get(i).getColour();
                String nextColor = hints.get(i + 1).getColour();
                if (currentColor.equals(nextColor)) {
                    requiredSpaces++; // Space needed between same-colored hints
                }
            }
        }
    
        space += requiredSpaces;
        return space;
    }

    /**
     * Deduce the solution by comparing possible combinations with the current grid state.
     * @param searchSpace the generated row and column combinations
     */
    public void deduce(SearchSpace searchSpace) {
        List<List<Combination>> rowCombs = searchSpace.getRowCombinations();
        List<List<Combination>> colCombs = searchSpace.getColCombinations();
        StateAndColor[][] grid = initialiseGrid();

        boolean changed;
        int iterations = 0;
        try {
            // Keep iterating until no more changes are detected or max iterations reached
            do {
                iterations++;
                changed = false;
                for (int i = 0; i < model.getRows(); i++) {
                    changed |= processLine(rowCombs.get(i), i, true, grid);
                }
                pruneConflictingCombinations(colCombs,grid,false); // Prune invalid column combinations

                for (int j = 0; j < model.getCols(); j++) {
                    changed |= processLine(colCombs.get(j), j, false, grid);
                }
                pruneConflictingCombinations(rowCombs, grid, true); // Prune invalid row combinations

                // Update the grid based on deductions
                for (int i = 0; i < model.getRows(); i++) {
                    for (int j = 0; j < model.getCols(); j++) {
                        GUI.updateButton(i, j, grid[i][j].getColor());
                    }
                }
            } while (changed && iterations < 10);

            updateGUI(grid); // Update the GUI with the final grid state
    } catch (Exception e) {
        GUI.displayErrorMessage();
    }
    }

    /**
     * Processes a single line (row or column) to update the grid based on matching combinations.
     * @param lineCombinations list of possible combinations for the line
     * @param lineIndex index of the current line (row or column)
     * @param isRow true if processing a row, false for a column
     * @param grid the current grid state
     * @return true if any change occurred, false otherwise
     */
    private boolean processLine(List<Combination> lineCombinations, int lineIndex, boolean isRow, StateAndColor[][] grid) {
        boolean changed = false;
            int lineLength = lineCombinations.get(0).getCombination().size();

        // Loop through each position in the line to check if all combinations match
        for (int pos = 0; pos < lineLength; pos++) {
            StateAndColor reference = lineCombinations.get(0).getCombination().get(pos);
            boolean allMatch = true;

            // Compare the position in all combinations to ensure they match
            for (int k = 1; k < lineCombinations.size(); k++) {
                StateAndColor current = lineCombinations.get(k).getCombination().get(pos);
                if (!statesMatch(current, reference)) {
                    allMatch = false;
                    break;
                }
            }

            // If all combinations match, update the grid at that position
            if (allMatch) {
                StateAndColor existing;
                if (isRow) {
                    existing = grid[lineIndex][pos];
                }
                else {
                    existing = grid[pos][lineIndex];
                }
                // Only update if the cell is in an unknown state
                if (existing.getCurrentState().equals(States.UNKNOWN)) {
                    updateGridCell(lineIndex, pos, reference, isRow, grid);
                    changed = true;
                }
            }
        }
            return changed; // Return if any change occurred
        
    }

    /**
 * Prunes invalid combinations based on the current state of the grid.
 * @param combinations the list of possible combinations for each line (row or column)
 * @param grid the current grid state
 * @param isRow true if pruning row combinations, false for columns
 */
    private void pruneConflictingCombinations (List<List<Combination>> combinations, StateAndColor[][] grid , boolean isRow) {
        for (int lineIndex = 0; lineIndex < combinations.size(); lineIndex++) {
            List<Combination> lineCombinations = combinations.get(lineIndex);
            List<Combination> validCombinationsLine = new ArrayList<>();

            // Check each combination in the line for validity
            for (Combination combination : lineCombinations) {
                boolean valid = true;
                List<StateAndColor> cells = combination.getCombination();

                // Check if any grid cells conflict with the combination
                for (int pos = 0; pos < cells.size(); pos++) {
                    StateAndColor gridCell;
                    if (isRow) {
                        gridCell = grid[lineIndex][pos];
                    }
                    else {
                        gridCell = grid[pos][lineIndex];
                    }
                    StateAndColor combinationCell = cells.get(pos);
                        
                    // If a known grid cell does not match the combination, mark it invalid
                    if (!gridCell.getCurrentState().equals(States.UNKNOWN)) {
                        if(!statesMatch(combinationCell, gridCell)) {
                            valid = false;
                            break;
                        }
                    }
                }

                // If the combination is valid, add it to the list
                if (valid) {
                    validCombinationsLine.add(combination);
                }

                // Update the line with valid combinations after pruning
                combinations.set(lineIndex, validCombinationsLine);
            }
        }
    }

    /**
     * Compares two StateAndColor objects for equality based on state and color.
     * @param a first StateAndColor object
     * @param b second StateAndColor object
     * @return true if both state and color match, false otherwise
     */
    private boolean statesMatch(StateAndColor a, StateAndColor b) {
        return a.getCurrentState().equals(b.getCurrentState()) && a.getColor().equals(b.getColor());
    }

    /**
     * Updates a specific cell in the grid with a new value (either row or column).
     * @param lineIndex the index of the line (row or column)
     * @param pos the position in the line (row or column)
     * @param value the new StateAndColor value to set
     * @param isRow true if updating a row, false for a column
     * @param grid the current grid state
     */
    private void updateGridCell(int lineIndex, int pos, StateAndColor value, boolean isRow, StateAndColor[][] grid) {
        if (isRow) {
            grid[lineIndex][pos] = value; // Update row cell
        } 
        else {
            grid[pos][lineIndex] = value; // Update column cell
        }
    }

    /**
     * Initializes a new grid with UNKNOWN state and default color for all cells.
     * @return a 2D array of StateAndColor objects representing the grid
     */
    private StateAndColor[][] initialiseGrid () {
        StateAndColor[][] grid = new StateAndColor[model.getRows()][model.getCols()];
        // Loop through all grid positions and initialize with UNKNOWN state
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                grid[i][j] = new StateAndColor(States.UNKNOWN, Color.decode(Constants.EMPTY_COLOUR));
            }
        }
        return grid;
    }

    /**
     * Updates the GUI to reflect the current grid state and colors.
     * @param grid the current grid state to display
     */
    private void updateGUI(StateAndColor[][] grid) {
        Color[][] colourArray = new Color[model.getRows()][model.getCols()];
        States[][] stateArray = new States[model.getRows()][model.getCols()];

        // Populate the arrays with the grid's color and state information
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                colourArray[i][j] = grid[i][j].getColor();
                stateArray[i][j] = grid[i][j].getCurrentState();

            }
        }
        // Update the model with the new grid colors and states
        model.setGridColors(colourArray);
        model.setGrid(stateArray);

        // Update the GUI buttons based on the new grid state
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                GUI.updateButton(i, j, grid[i][j].getColor());
            }
        }
    }   
}
