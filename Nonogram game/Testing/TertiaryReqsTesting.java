package Testing;

import MainProgram.*;
import Screen.GameUI;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;


/**
 * {@code TertiaryReqsTesting} tests functionalities indicated in the Tertiary Requirements.
 */
public class TertiaryReqsTesting {

    private static NonogramModel model;
    private static GameUI gameUI;
    private static DeductiveSolver ds;


    /**
     * Sets up the environment for checking the solvers
     * @throws Exception file or puzzle loading issues
     */
    @Before
    public void setUp() throws Exception {
        PuzzleLoader loader = new PuzzleLoader("./data/smiler.json");
        Level level = loader.getLevel();
        model = new NonogramModel(level);
        gameUI = new GameUI(model);
        ds = new DeductiveSolver(model, gameUI);
    }

    // ~   DEDUCTIVE SOLVER ~ //
    /**
     * Test ensures that {@code DeductiveSolver.getSearchSpace()} returns a correct {@code SearchSpace} object
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void gettingSearchSpace() throws PuzzleLoadException {

        SearchSpace sSp = ds.getSearchSpace(); // gets searchspace

        // search space object is not null
        assertNotNull(sSp);

        List<List<Combination>> columnCombinations = sSp.getColCombinations();
        List<List<Combination>> rowCombinations = sSp.getRowCombinations();

        // containing elements are not null
        assertNotNull(columnCombinations);
        assertNotNull(rowCombinations);

        int columnLength = model.getCols();
        int rowLength = model.getRows();

        // size of both 2d lists should be column and row amount respectively
        assertEquals(model.getCols(), columnCombinations.size());
        assertEquals(model.getRows(), rowCombinations.size());

    }

    /**
     * Test loops through all files which are able to be solved by {@code DeductiveSolver}, and checks if the solving output is valing through using a {@code NonogramChecker} object, which was validated in {@code SecondaryReqsTesting}.
     * @throws PuzzleLoadException puzzle is not loaded correctly
     */
    @Test
    public void checkingDeductiveSolver() throws PuzzleLoadException {

        NonogramChecker checker = new NonogramChecker(model);
        File allJsonFilesDir = new File("./data/");
        File[] jsonFiles = allJsonFilesDir.listFiles();

        for (File file : jsonFiles) {
            String filePath = file.getPath();
            Level newLevel = new PuzzleLoader(filePath).getLevel();
            model.setLevel(newLevel);
            gameUI.updateGUI();

            boolean nonValidFiles = filePath.contains("unsolvable_smiler")
                    || filePath.contains("player")
                    || filePath.contains("multi_checks")
                    || filePath.contains("test1_tutorial");


            if (!nonValidFiles) {
                ds.solve();
                assertTrue(checker.isSolved());
            }




        }
    }


}
