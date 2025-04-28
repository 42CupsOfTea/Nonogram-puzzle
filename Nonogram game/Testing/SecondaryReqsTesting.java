package Testing;

import MainProgram.*;
import org.junit.Test;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * {@code SecondaryReqsTesting } tests functionality indicated in the Secondary Requirements.
 */
public class SecondaryReqsTesting {

    // ~     SAVE - LOAD    ~//

    /**
     * Tests that a save directory is properly created with a state_save and color_save file
     * @throws Exception issues with files or puzzle loading
     */
    @Test
    public void testSaveFileExists() throws Exception {
        File saveDirectory = new File("./savedPuzzels/Test_file");
        try {
            // Loading puzzle elements
            PuzzleLoader loader = new PuzzleLoader("./Testing/testData/Test_file.json");
            Level level = loader.getLevel();
            NonogramModel model = new NonogramModel(level);
            model.toggleCell(0, 0); //changes to fill

            // Saving the puzzle
            model.savePuzzle();

            // Child element of saveDirectory
            File[] subdir = saveDirectory.listFiles(); // directory only : SAVE_(number)

            // Grandchild elements of saveDirectory
            assert subdir != null;
            File[] subdir2 = subdir[0].listFiles(); // files: state_save color_save


            boolean hasStateSave = false;
            boolean hasColorSave = false;

            // Checking if files contain state or color
            for (File f : subdir2) {
                if (f.getName().equals("state_save")) {
                    hasStateSave = true;
                }
                if (f.getName().equals("color_save")) {
                    hasColorSave = true;
                }
            }

            // does main save directory exist
            assertTrue(saveDirectory.exists());

            // does child directory exist
            assertEquals(1, subdir.length);
            assertTrue(subdir[0].isDirectory());

            // do the grandchild files exist with proper names
            assertEquals(2, subdir2.length);
            assertTrue(hasStateSave && hasColorSave);

        } finally {
            tearDownSaveAndLoad(saveDirectory); // to delete test directory
        }
    }

    /**
     * Tests if {@code .loadPuzzle()} and {@code .savePuzzle()} both work correctly
     * @throws Exception includes any puzzle loading or file deletion exception
     */
    @Test
    public void testSaveAndLoadPuzzle() throws Exception {
        try {
            PuzzleLoader loader = new PuzzleLoader("./Testing/testData/Test_file.json");
            NonogramModel model = new NonogramModel(loader.getLevel());
            Color firstColor = Color.decode(loader.getLevel().getColorOptions().get(0));

            model.setCurrentFillColor(firstColor);
            model.toggleCell(0, 0); // FILLED
            model.savePuzzle(); //

            NonogramModel model2 = new NonogramModel(loader.getLevel());
            model2.loadPuzzle(new File("./savedPuzzels/Test_file/SAVE_1/state_save"), new File("./savedPuzzels/Test_file/SAVE_1/color_save"));

            assertEquals(States.FILLED, model2.getCellState(0, 0));
            assertEquals(firstColor, model2.getGridColor(0, 0));
        } finally {
            File directory = new File("./savedPuzzels/Test_file");
            tearDownSaveAndLoad(directory);
        }


    }

    /**
     * Ensures {@code model} does not allow for an invalid file to be loaded
     * @throws Exception contains {@code PuzzleLoadException.class}
     */
    @Test
    public void testLoadInvalidPuzzle() throws Exception {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/Test_file.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);

        assertThrows(PuzzleLoadException.class, () -> model.loadPuzzle(new File("./invalid/path/1"), new File("./invalid/path/2")));

    }

    /**
     * Deletes the save file for Test_file.json
     * @param directory the directory of the save file
     * @throws Exception issues with files
     */
    public static void tearDownSaveAndLoad(File directory) throws Exception {
        if (!directory.exists()) {
            return;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            throw new Exception("Error accessing files in: " + directory);
        }

        for (File file : files) {
            if (file.isDirectory()) {
                tearDownSaveAndLoad(file); // Recurse into subdirectory
            } else {
                if (!file.delete()) {
                    throw new Exception("Failed to delete file: " + file);
                }
            }
        }

        if (!directory.delete()) {
            throw new Exception("Failed to delete directory: " + directory);
        }
    }


    // ~     UNDO    ~ //

    /**
     * Tests if {@code model.undoMove()} properly undoes user moves
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testUndoMove() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./data/smiler.json");
        Level level = loader.getLevel();

        NonogramModel model = new NonogramModel(level);
        model.toggleCell(0, 0);
        assertEquals(States.FILLED, model.getCellState(0, 0));

        model.undoMove();
        assertEquals(States.UNKNOWN, model.getCellState(0, 0));
    }

    /**
     * Tests if {@code model.undoMove()} works properly for color nonograms
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testUndoMoveColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./data/colour_cat.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);

        Color firstColor = Color.decode(level.getColorOptions().get(0));
        model.setCurrentFillColor(firstColor);
        model.toggleCell(0, 0); //FILLED with firstColor

        Color secondColor = Color.decode(level.getColorOptions().get(1));
        model.setCurrentFillColor(secondColor);
        model.toggleCell(0, 0); // FILLED


        assertEquals(secondColor, model.getGridColor(0, 0));
        assertEquals(States.FILLED, model.getCellState(0, 0));

        model.undoMove();

        assertEquals(firstColor, model.getGridColor(0, 0));
        assertEquals(States.FILLED, model.getCellState(0, 0));


    }

    // ~ COLOR NONOGRAMS ~ //


    /**
     * Tests if the checker can identify an invalid, colored nonogram solution, and identify the incorrect rows and columns of the solution
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testIncorrectRowAndColumnsColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/color.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);
        NonogramChecker checker = new NonogramChecker(model);

        model.setCurrentFillColor(Color.decode(level.getColorOptions().getFirst())); //
        model.toggleCell(0, 0);
        model.toggleCell(0, 1);
        model.toggleCell(1, 1);
        model.toggleCell(1, 0); // Incorrect color for this row and column

        java.util.List<Integer> incorrectColumns = checker.getIncorrectColumns();
        List<Integer> incorrectRows = checker.getIncorrectRows();
        assertFalse(checker.isSolved());
        assertFalse(incorrectRows.isEmpty());
        assertFalse(incorrectColumns.isEmpty());
        assertTrue(incorrectRows.contains(2)); // 2nd row is wrong
        assertTrue(incorrectColumns.contains(1)); // 1st column is wrong
    }


    /**
     * Tests if the checker can identify a 'solved' colored nonogram puzzle
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testCorrectColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/color.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);
        NonogramChecker checker = new NonogramChecker(model);

        model.setCurrentFillColor(Color.decode(level.getColorOptions().getFirst())); //
        model.toggleCell(0, 0);
        model.toggleCell(0, 1);
        model.toggleCell(1, 1);

        model.setCurrentFillColor(Color.decode(level.getColorOptions().get(1)));
        model.toggleCell(1, 0);

        assertTrue(checker.isSolved());

    }

    // loader - level

    /**
     * Checks if the level can hold non-black-and-white color options correctly
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testLevelColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/color.json");
        Level level = loader.getLevel();
        List<String> colorOptions = level.getColorOptions();
        assertFalse(colorOptions.isEmpty()); // options are not empty
        assertTrue(colorOptions.contains("#FF0000")); // contains red

    }

    /**
     * When a certain color is chosen and cells are toggled to {@code FILLED}, the color of the cells will be saved inside of {@code gridColors[][]} correctly
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void testChangeInGridColor() throws PuzzleLoadException {
        PuzzleLoader loader = new PuzzleLoader("./Testing/testData/color.json");
        Level level = loader.getLevel();
        NonogramModel model = new NonogramModel(level);

        Color red = Color.decode(level.getColorOptions().get(1));
        model.setCurrentFillColor(red); // RED
        model.toggleCell(0, 0);
        model.toggleCell(0, 1);
        Color black = Color.decode(level.getColorOptions().get(0));
        model.setCurrentFillColor(black);
        model.toggleCell(1,0);
        model.toggleCell(1,1);

        assertTrue (model.getCellState(0,0) == States.FILLED && model.getCellState(0,1) == States.FILLED);
        assertTrue(Objects.equals(model.getGridColor(0, 0), red) && Objects.equals(model.getGridColor(0, 1), red));
        assertTrue(model.getCellState(1,0) == States.FILLED && model.getCellState(1,1) == States.FILLED);
        assertTrue(Objects.equals(model.getGridColor(1, 0), black) && Objects.equals(model.getGridColor(1, 1), black));
    }

}
