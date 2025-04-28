package Screen;

import MainProgram.CheckResult;
import MainProgram.Constants;
import MainProgram.NonogramModel;

import java.awt.*;
import javax.swing.*;

/**
 * Class defines the graphical user interface for the solution and all its child JPanels
 */
public class GameUI extends JFrame {
    private final NonogramModel model;
    private final JPanel northPanel;
    private GridPanel gridPanel;
    private FilePanel filePanel;
    private CheckerPanel checkerPanel;
    private ColoredButtonsPanel coloredButtonsPanel; // stores all buttons to switch between colors

    private boolean isDragging = false;

    /**
     * Builds a graphical user interface based on a nonogram model
     * @param model nonogram model object
     */
    public GameUI(NonogramModel model) {
        super("Nonogram Game");
        this.model = model;
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // North panel configuration (file panel and color buttons)
        northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        filePanel = new FilePanel(model, this);
        coloredButtonsPanel = new ColoredButtonsPanel(model, this);

        northPanel.add(filePanel);
        northPanel.add(coloredButtonsPanel);

        add(northPanel, BorderLayout.NORTH);

        // Grid panel for the puzzle
        gridPanel = new GridPanel(model, this);
        add(gridPanel, BorderLayout.CENTER);

        // Checker panel for puzzle checking (correctness)
        checkerPanel = new CheckerPanel(model, this);
        add(checkerPanel, BorderLayout.SOUTH);
    }

    /**
     * Updates a specific button in the grid with a given color
     * @param row Row index of the button
     * @param col Column index of the button
     * @param color Color to update the button with
     */
    public void updateButton(int row, int col, Color color) {
        gridPanel.updateButton(row, col, color);
    }

    /**
     * Updates all elements within the graphical user interface
     * This method is typically called when the game state changes
     */
    public void updateGUI() {
        // Update grid panel
        remove(gridPanel);
        gridPanel = new GridPanel(model, this);
        add(gridPanel, BorderLayout.CENTER);

        // Update file and color button panels in the north panel
        northPanel.removeAll();
        filePanel = new FilePanel(model, this);
        coloredButtonsPanel = new ColoredButtonsPanel(model, this);
        northPanel.add(filePanel);
        northPanel.add(coloredButtonsPanel);

        // Update checker panel
        remove(checkerPanel);
        checkerPanel = new CheckerPanel(model, this);
        add(checkerPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Displays the result of the solution check
     * @param result Result of the check (whether the puzzle is solved)
     */
    public void displayCheckResult(CheckResult result) {
        if (result == null) {
            JOptionPane.showMessageDialog(this,
                    "Error: No solution data available",
                    "Solution Check",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (result.isSolved()) {
            JOptionPane.showMessageDialog(this,
                    "Congratulations! Puzzle solved!",
                    "Solution Check",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            String message = String.format(
                    "Puzzle not solved yet.%n%s%s",
                    result.getIncorrectRows().isEmpty() ? "" : "Incorrect rows: " + result.getIncorrectRows() + "\n",
                    result.getIncorrectColumns().isEmpty() ? "" : "Incorrect columns: " + result.getIncorrectColumns());

            JOptionPane.showMessageDialog(this,
                    message.trim(), // Remove any trailing newlines
                    "Solution Check",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Display error message
     */
    public void displayErrorMessage() {
        JOptionPane.showMessageDialog(this,
        "Error: No solution data available",
        "Solution Check",
        JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Returns whether the user is currently dragging (interacting with the puzzle)
     * @return true if dragging, false otherwise
     */
    public boolean isDragging() {
        return isDragging;
    }

    /**
     * Sets whether the user is currently dragging (interacting with the puzzle)
     * @param dragging true if dragging, false otherwise
     */
    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }
}
