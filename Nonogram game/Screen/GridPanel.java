package Screen;

import javax.swing.*;
import MainProgram.NonogramModel;
import MainProgram.Constants;
import java.awt.*;

/**
 * Class defines the nonogram grid that is displayed to the user through the graphical user interface
 */
public class GridPanel extends JPanel {
    private final NonogramModel model;
    private final GameUI GUI;
    private Button[][] buttons;

    /**
     * Constructor for the grid panel
     * @param model nonogram model
     * @param GUI current graphical user interface
     */
    public GridPanel(NonogramModel model, GameUI GUI) {
        this.model = model;
        this.GUI = GUI;
        initializePanel();
    }

    /**
     * Initialises the grid
     */
    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.decode(Constants.GRID_CHECKER_PANEL_COLOUR)); // Background color for the grid panel

        // Create and configure row and column hint panels
        HintPanel rowHintPanel = new HintPanel(model);
        rowHintPanel.createRowHintPanel();
        rowHintPanel.setPreferredSize(new Dimension(Constants.HINT_PANEL_SIZE, 0));
        rowHintPanel.setOpaque(false);

        HintPanel colHintPanel = new HintPanel(model);
        colHintPanel.setOpaque(false);
        colHintPanel.createColHintPanel();
        colHintPanel.setPreferredSize(new Dimension(0, Constants.HINT_PANEL_SIZE));

        // Create the grid of buttons
        JPanel gridPanel = createButtons();
        gridPanel.setPreferredSize(new Dimension(Constants.GRID_SIZE, Constants.GRID_SIZE));
        gridPanel.setOpaque(false);

        // Main panel that holds row hints, column hints, and the grid
        JPanel mainJPanel = new JPanel(new GridBagLayout());
        mainJPanel.setOpaque(false);

        // Layout configuration for placing components
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        mainJPanel.add(rowHintPanel, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTH;
        mainJPanel.add(colHintPanel, c);

        c.fill = GridBagConstraints.VERTICAL;
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;
        mainJPanel.add(gridPanel, c);

        mainJPanel.setVisible(true);
        add(mainJPanel, BorderLayout.CENTER);
    }

    /**
     * Builds the grid cells
     * @return the jpanel filled with cells
     */
    private JPanel createButtons() {
        // Create a grid of buttons based on the puzzle's dimensions (rows and cols)
        buttons = new Button[model.getRows()][model.getCols()];
        JPanel grid = new JPanel(new GridLayout(model.getRows(), model.getCols()));

        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getCols(); j++) {
                Button button = new Button();
                button.addMouseListener(new ButtonClicked(i, j, model, GUI)); // Add click listener for user interaction
                buttons[i][j] = button;
                grid.add(button);
            }
        }
        return grid;
    }

    /**
     * Updates a button based on input
     * @param row row number
     * @param col column number
     * @param filledColor the color to fill the cell (if it is being filled)
     */
    public void updateButton(int row, int col, Color filledColor) {
        // Update the button color based on the cell state (Filled, Empty, Unknown)
        Button button = buttons[row][col];
        switch (model.getCellState(row, col)) {
            case FILLED:
                button.setBackground(filledColor); // Set color when the cell is filled
                break;
            case UNKNOWN:
                button.setBackground(Color.WHITE); // Set to white for unknown state
                break;
            case EMPTY:
                button.setBackground(Color.decode(Constants.EMPTY_COLOUR)); // Light red for empty state
                break;
        }
    }
}
