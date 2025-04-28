package MainProgram;

import Screen.GameUI;

/**
 * Class allows for nonogram solutions to be checked
 */
public class CheckerController {
    private final NonogramModel model;
    private final GameUI gui;

    /**
     * Constructor for the CheckerController.
     * @param model the nonogram game model
     * @param gui the graphical user interface
     */
    public CheckerController(NonogramModel model, GameUI gui) {
        this.model = model;
        this.gui = gui;
    }

    /**
     * Checks the user's solution and displays the result.
     */
    public void checkSolution() {
        CheckResult result = model.checkSolution();
        gui.displayCheckResult(result);
    }

}

/*
 * Follows MVC pattern by separating game logic and UI handling.
 */
