package Screen;

import MainProgram.CheckerController;
import MainProgram.Constants;
import MainProgram.DeductiveSolver;
import MainProgram.Move; 
import MainProgram.NonogramModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Defines the panel (on the southern part of the gui) which holds different buttons
 */
public class CheckerPanel extends JPanel implements ActionListener {
    private final NonogramModel model;  // The model for the nonogram game state
    private final GameUI GUI;           // The graphical user interface
    private JButton checkSolutionButton;  // Button to check if the solution is correct
    private JButton undoMoveButton;      // Button to undo the last move
    private JButton solverButton;        // Button to solve the puzzle
    private JButton clearMovesButton;    // Button to restart the puzzle (clear all moves)

    /**
     * Constructor to initialize the model and GUI
     * @param model nonogram model
     * @param GUI current graphical user interface
     */
    public CheckerPanel(NonogramModel model, GameUI GUI) {
        this.model = model;
        this.GUI = GUI;
        initializePanel();  // Initialize the panel and its components
    }

    /**
     * Initializes the panel layout and buttons
     */
    private void initializePanel() {
        setLayout(new BorderLayout());  // Use BorderLayout for button placement
        setBackground(Color.decode(Constants.GRID_CHECKER_PANEL_COLOUR));  // Set background color using a constant

        JPanel checkerPanel = new JPanel();  // Panel to hold the buttons
        checkerPanel.setOpaque(false);  // Set the panel to be transparent

        // Create the buttons for various actions
        checkSolutionButton = new JButton("Check Solution");
        undoMoveButton = new JButton("Undo Move");
        solverButton = new JButton("Solve");
        clearMovesButton = new JButton("Restart Puzzle");

        // Add action listeners to buttons
        checkSolutionButton.addActionListener(this);
        undoMoveButton.addActionListener(this);
        clearMovesButton.addActionListener(this);
        solverButton.addActionListener(this);

        // Add buttons to the panel
        checkerPanel.add(checkSolutionButton);
        checkerPanel.add(clearMovesButton);
        checkerPanel.add(undoMoveButton);
        checkerPanel.add(solverButton);

        // Add the checker panel to the main panel (at the bottom)
        add(checkerPanel, BorderLayout.SOUTH);
    }

    /**
     * Handle button clicks
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();  // Identify which button was clicked
        if (source == checkSolutionButton) {
            checkSolutionButtonPressed();  // Check if the solution is correct
        }
        else if (source == undoMoveButton) {
            checkUndoMoveButtonPressed();  // Undo the last move
        } else if (source == clearMovesButton) {
            clearMovesButtonPressed();  // Clear all moves (restart puzzle)
        }
        else if (source == solverButton) {
           solveAndDisplay();  // Solve the puzzle using a solver
        }
    }

    /**
     *  Called when the "Check Solution" button is pressed
     */
    private void checkSolutionButtonPressed() {
        new CheckerController(model, GUI).checkSolution();  // Use the controller to check the solution
    }

    /**
     * Called when the "Undo Move" button is pressed
     */
    private void checkUndoMoveButtonPressed() {
        Move undone = model.undoMove();  // Undo the last move
        if (undone != null) {
            // Update the button to reflect the undone move
            GUI.updateButton(undone.getRowToUndo(), undone.getColumnToUndo(), undone.getColorToUndo());
        }
    }

    /**
     * Called when the "Clear Moves" button is pressed
     */
    private void clearMovesButtonPressed() {
        model.resetGrid();  // Reset the grid to its initial state
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                // Update all buttons to reflect the initial grid color
                GUI.updateButton(i, j, model.getGridColor(i, j));
            }
        }
    }

    /**
     * Called when the "Solve" button is pressed
     */
    private void solveAndDisplay() {
        new DeductiveSolver(model, GUI).solve();  // Solve the puzzle using the DeductiveSolver
    }
}
