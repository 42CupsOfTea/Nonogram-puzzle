package MainProgram;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Stack;

/**
 * Class defines logic and information on the current puzzle.
 */
public class NonogramModel {
    private int rows;
    private int cols;
    private States[][] grid;  // 2D array representing the grid of the nonogram puzzle
    private Level level;  // The current level of the puzzle
    private final NonogramChecker checker;  // The checker responsible for verifying puzzle completion
    private final Stack<Move> moves = new Stack<>();  // Stack to keep track of moves for undo functionality
    private Color currentFillColor;  // The current color used for filling cells in the nonogram
    private Color[][] gridColors;  // 2D array to hold the color for each cell in the grid

    /**
     * Constructor initializes the nonogram grid based on the provided level.
     * @param level The current level of the puzzle
     */
    public NonogramModel(Level level) {
        this.level = level;
        this.currentFillColor = Color.BLACK;  // Default fill color is black
        this.rows = level.getRowOrColumnHints(level.getRowHintColors()).size();
        this.cols = level.getRowOrColumnHints(level.getColHintColors()).size();
        this.grid = new States[rows][cols];
        this.gridColors = new Color[rows][cols];
        this.checker = new NonogramChecker(this);

        // Initialize the grid with UNKNOWN states and default white colors
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = States.UNKNOWN;
                gridColors[i][j] = Color.WHITE;  // Default color is white
            }
        }
    }

    /**
     * Sets a new level for the nonogram, resetting the grid and color settings.
     * @param newLevel The new level to set
     */
    public void setLevel(Level newLevel) {
        this.level = newLevel;
        reset();
    }

    /**
     * Resets the grid and color settings when a new level is loaded.
     */
    private void reset() {
        this.rows = level.getRowOrColumnHints(level.getRowHintColors()).size();
        this.cols = level.getRowOrColumnHints(level.getColHintColors()).size();
        this.grid = new States[rows][cols];
        this.gridColors = new Color[rows][cols];

        // Reset the grid to its initial state (UNKNOWN) and color to white
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = States.UNKNOWN;
                gridColors[i][j] = Color.WHITE;  // Reset color array
            }
        }
    }

    /**
     * Resets the entire grid and color array to their initial states (UNKNOWN and white).
     */
    public void resetGrid () {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = States.UNKNOWN;
                gridColors[i][j] = Color.WHITE;  // Default color is white
            }
        }
    }

    /**
     * Toggles the state of a cell between EMPTY, UNKNOWN, or FILLED.
     * Also updates the color of the cell based on the current fill color.
     * @param row The row index of the cell
     * @param col The column index of the cell
     */
    public void toggleCell(int row, int col) {
        States originalState = grid[row][col];  // Capture the original state of the cell
        Color originalColor = gridColors[row][col];  // Capture the original color of the cell
        Color newColour = getCurrentFillColor();  // Get the current fill color for the toggle operation

        // If the cell is filled with the current color or is empty, toggle between states
        if (newColour.equals(originalColor) || originalState.equals(States.EMPTY)) { 
            switch(originalState) {
                case EMPTY:
                    grid[row][col] = States.UNKNOWN;
                    gridColors[row][col] = Color.WHITE;  // Reset color to white for UNKNOWN state
                    break;
                case FILLED:
                    grid[row][col] = States.EMPTY;
                    gridColors[row][col] = Color.decode(Constants.EMPTY_COLOUR);  // Change to a light red color for EMPTY state
                    break;
                case UNKNOWN:
                    grid[row][col] = States.FILLED;
                    gridColors[row][col] = this.currentFillColor;  // Set cell to filled with the current color
                    break;
            }
        } else {
            // If the current color is different, mark the cell as filled with the new color
            grid[row][col] = States.FILLED;
            gridColors[row][col] = this.currentFillColor;
        }

        // Save the move for undo functionality
        Move newMove = new Move(row, col, originalState, originalColor);
        moves.push(newMove);
    }

    /**
     * Loads a nonogram puzzle from the given files. Reads the grid's state and color information.
     * @param fileToLoad The file containing the state of the puzzle
     * @param color_save The file containing the color information for the puzzle
     */
    public void loadPuzzle(File fileToLoad, File color_save) throws PuzzleLoadException {
        try (BufferedReader stateReader = new BufferedReader(new FileReader(fileToLoad));
             BufferedReader colorReader = new BufferedReader(new FileReader(color_save))) {
            String stateLine;
            String colorLine;
            int i = 0;
            while ((stateLine = stateReader.readLine()) != null &&
                    (colorLine = colorReader.readLine()) != null) {

                String[] stateValues = stateLine.split(" ");
                String[] colorValues = colorLine.split(" ");

                // Ensure that the state and color lines match
                if (stateValues.length != colorValues.length) {
                    throw new IOException("Mismatch between state and color data at row " + i);
                }

                for (int j = 0; j < stateValues.length; j++) {
                    // Update the state of the grid based on the file data
                    if (stateValues[j].equals("EMPTY")) {
                        grid[i][j] = States.EMPTY;
                    } else if (stateValues[j].equals("FILLED")) {
                        grid[i][j] = States.FILLED;
                    } else if (stateValues[j].equals("UNKNOWN")) {
                        grid[i][j] = States.UNKNOWN;
                    }

                    // Update the color of the cell based on the color file data
                    gridColors[i][j] = Color.decode(colorValues[j]);
                }
                i++;
            }
        } catch (IOException e) {
            if (e.getClass().getName().equals("java.io.FileNotFoundException")) {
                throw new PuzzleLoadException("Level cannot be loaded, INVALID file");
            }
            e.printStackTrace();  // Handle file reading exceptions
        }
    }

    /**
     * Returns the color of a specific cell.
     * @param row The row index of the cell
     * @param col The column index of the cell
     * @return The color of the specified cell
     */
    public Color getGridColor(int row, int col) {
        return gridColors[row][col];
    }

    /**
     * Converts a Color object to a hexadecimal string representation for saving.
     * @param color The Color object to convert
     * @return The hexadecimal string representation of the color
     */
    public static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue()).toUpperCase();
    }

    /**
     * Saves the current puzzle state and color grid to files in the "savedPuzzles" directory.
     */
    public void savePuzzle() {
        StringBuilder states = new StringBuilder();
        StringBuilder colors = new StringBuilder();

        // Build strings representing the state and colors of the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                states.append(grid[i][j].toString() + " ");
                colors.append(colorToHex(gridColors[i][j]) + " ");
            }
            states.append("\n");
            colors.append("\n");
        }

        String directoryPath = "savedPuzzels/";
        File baseDir = new File(directoryPath);
        baseDir.mkdirs();  // Ensure main directory exists

        File levelDir = new File(directoryPath + level.getName());
        levelDir.mkdirs();  // Ensure level directory exists

        // Check existing save directories to determine the next save number
        File[] files = new File(directoryPath.concat(level.getName())).listFiles();
        int count = 0;
        int saveNumber = 0;

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && file.getName().toLowerCase().startsWith("save_")) {
                    count++;
                }
            }
            saveNumber = count + 1;  // Set the next save number
        }
        File saveDir = new File(levelDir, "SAVE_" + saveNumber);
        saveDir.mkdirs();  // Create save directory with parents

        // Create files for state and color saves
        File colorSave = new File(saveDir, "color_save");
        File stateSave = new File(saveDir, "state_save");

        try {
            colorSave.createNewFile();
            stateSave.createNewFile();
            System.out.println("Saving Puzzle");

            // Write the state and color data to the respective files
            FileWriter colorWriter = new FileWriter(colorSave);
            FileWriter stateWriter = new FileWriter(stateSave);

            colorWriter.write(colors.toString());
            stateWriter.write(states.toString());

            // Close the file writers
            colorWriter.close();
            stateWriter.close();
        } catch (IOException e) {
            e.printStackTrace();  // Handle file writing exceptions
        }
    }

    /**
     * Undoes the last move, restoring the grid and color to the previous state.
     * @return The move that was undone
     */
    public Move undoMove() {
        if (!moves.isEmpty()) {
            Move moveToUndo = moves.pop();  // Pop the most recent move from the stack
            int col = moveToUndo.getColumnToUndo();
            int row = moveToUndo.getRowToUndo();
            States previousState = moveToUndo.getStateToUndo();
            Color previousColor = moveToUndo.getColorToUndo();

            // Restore the previous state and color
            grid[row][col] = previousState;
            gridColors[row][col] = previousColor;

            return moveToUndo;
        }
        return null;  // Return null if there are no moves to undo
    }

    /**
     * Returns the current level of the nonogram.
     * @return The current level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Returns the state of a specific cell.
     * @param row The row index of the cell
     * @param col The column index of the cell
     * @return The state of the specified cell
     */
    public States getCellState(int row, int col) {
        return grid[row][col];
    }

    /**
     * Returns the number of rows in the grid.
     * @return The number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the grid.
     * @return The number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Checks if the puzzle is solved by using the NonogramChecker.
     * @return True if the puzzle is solved, false otherwise
     */
    public boolean isSolved() {
        return checker.isSolved();
    }

    /**
     * Checks the solution's correctness and returns any errors in the puzzle.
     * @return A CheckResult object with the solution status and incorrect rows/columns
     */
    public CheckResult checkSolution() {
        boolean solved = checker.isSolved();
        List<Integer> badRows = solved ? List.of() : checker.getIncorrectRows();
        List<Integer> badCols = solved ? List.of() : checker.getIncorrectColumns();
        return new CheckResult(solved, badRows, badCols);
    }

    /**
     * Sets the grid of the nonogram model to a new 2D state array.
     * @param grid The new grid to set
     */
    public void setGrid(States[][] grid) {
        this.grid = grid;
    }

    /**
     * Sets the color grid to a new 2D color array.
     * @param gridColors The new grid colors to set
     */
    public void setGridColors(Color[][] gridColors) {
        this.gridColors = gridColors;
    }

    /**
     * Returns the 2D array of grid colors.
     * @return The grid colors
     */
    public Color[][] getGridColors() {
        return gridColors;
    }

    /**
     * Returns the 2D array of grid states.
     * @return The grid states
     */
    public States[][] getGrid() {
        return grid;
    }

    /**
     * Returns the current fill color.
     * @return The current fill color
     */
    public Color getCurrentFillColor() {
        return currentFillColor;
    }

    /**
     * Sets the current fill color.
     * @param color The color to set as the current fill color
     */
    public void setCurrentFillColor(Color color) {
        currentFillColor = color;
    }
}
