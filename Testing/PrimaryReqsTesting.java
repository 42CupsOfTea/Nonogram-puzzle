package Testing;

import MainProgram.*;
import org.junit.Test;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * {@code PrimaryReqsTesting} tests functionalities indicated by the Primary Requirements
 */
public class PrimaryReqsTesting {

    //~     PUZZLE LOADER     ~//
    /**
     * A valid level should be correctly loaded within PuzzleLoader
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testValidPuzzleLoading() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./data/horse.json");
        assertNotNull(loader.getLevel());
        assertEquals("horse", loader.getLevel().getName().toLowerCase());
        assertFalse(loader.getLevel().getRowHintColors().isEmpty()); // there are row hints
        assertFalse(loader.getLevel().getColHintColors().isEmpty()); // there are column hints
    }

    /**
     * An invalid level should throw a PuzzleLoadException within PuzzleLoader
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testInvalidPuzzleLoading() throws PuzzleLoadException {
        assertThrows(PuzzleLoadException.class, () -> new PuzzleLoader("./data/invalid.json"));
    }

    /**
     * A non-colored puzzle will contain black as the ONLY color option a user can pick from
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testDefaultColorHandling() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./data/horse.json");
        assertFalse(loader.getLevel().getColorOptions().isEmpty());
        assertTrue(loader.getLevel().getColorOptions().contains("0x000000") || loader.getLevel().getColorOptions().contains("#000000"));
    }

    //~     NONOGRAM MODEL ~ //

    /**
     * Builds a black-and-white level to be used for the {@code NonogramModel} tests
     * @return a sample level
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    public Level modelSetup() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./data/smiler.json");
        return loader.getLevel();
    }


    /**
     * Tests if the cell toggling, between FILLED EMPTY UNKNOWN, works properly
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testToggleCell() throws PuzzleLoadException {
        NonogramModel model = new NonogramModel(modelSetup());
        model.toggleCell(0, 0); // Toggle a cell to FILLED
        assertEquals(States.FILLED, model.getCellState(0, 0));

        model.toggleCell(0, 0); // Toggle the same cell to EMPTY
        assertEquals(States.EMPTY, model.getCellState(0, 0));

        model.toggleCell(0, 0); // Toggle the same cell to UNKNOWN
        assertEquals(States.UNKNOWN, model.getCellState(0, 0));
    }



    /**
     * Tests if the reset puzzle function {@code model.resetGrid()} works for all nonograms
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testResetPuzzle() throws PuzzleLoadException {
        NonogramModel model = new NonogramModel(modelSetup());

        model.toggleCell(0, 0);
        assertEquals(States.FILLED, model.getCellState(0, 0));
        assertEquals(Color.BLACK, model.getGridColor(0,0));

        model.resetGrid();
        assertEquals(States.UNKNOWN, model.getCellState(0, 0));
        assertEquals(Color.WHITE, model.getGridColor(0,0));


    }


    //~     NONOGRAM CHECKER ~ //

    /**
     * Tests if the checker can correctly identify a 'solved' nonogram puzzle
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testCorrectSolutionNoColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/no_color.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);
        NonogramChecker checker = new NonogramChecker(model);


        model.toggleCell(0, 0); // Fill the first row (3 black cells)
        model.toggleCell(0, 1);
        model.toggleCell(1, 1); // Fill the second row (1 black cell)

        assertTrue(checker.isSolved());

    }

    /**
     * Tests if the checker can identify an invalid nonogram solution, and display the incorrect rows
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testIncorrectRowsNoColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/no_color.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);
        NonogramChecker checker = new NonogramChecker(model);

        model.toggleCell(0, 0);
        model.toggleCell(0, 1);


        List<Integer> incorrectRows = checker.getIncorrectRows();
        assertFalse(checker.isSolved());
        assertFalse(incorrectRows.isEmpty());
        assertTrue(incorrectRows.contains(2));  // row 2 is incorrect

    }

    /**
     * Tests if the checker can identify an invalid nonogram solution, and display the incorrect columns
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testIncorrectColumnsNoColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/no_color.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);
        NonogramChecker checker = new NonogramChecker(model);

        model.toggleCell(0, 1);

        List<Integer> incorrectColumns = checker.getIncorrectColumns();
        assertFalse(checker.isSolved());
        assertFalse(incorrectColumns.isEmpty());
        assertTrue(incorrectColumns.contains(2) && incorrectColumns.contains(1)); //column 2 and 1 are incorrect
    }


    }



