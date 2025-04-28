package Screen;

import MainProgram.NonogramModel;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * Class defines how the constraints are to be displayed by the graphical user interface
 */
public class HintPanel extends JPanel {
    private final NonogramModel model;

    /**
     * Constructor initializing the HintPanel with the given NonogramModel
     * @param model nonogram model
     */
    public HintPanel(NonogramModel model) {
        super();
        this.model = model;
    }

    /**
     * Creates the row hint panel, which displays hints for rows at the top of the grid
     */
    public void createRowHintPanel() {
        // Get the hints for the rows and their associated colors
        List<List<Integer>> rowHints = model.getLevel().getRowOrColumnHints(model.getLevel().getRowHintColors());
        List<List<String>> rowColours = model.getLevel().getRowOrColumnColors(model.getLevel().getRowHintColors());

        // Set the layout to a grid, with one row for each row of the Nonogram
        setLayout(new GridLayout(rowHints.size(), 1));

        // Loop through each row hint and create a JLabel for each number in the hint
        for (int i = 0; i < rowHints.size(); i++) {
            JPanel individualHintPanel = new JPanel();
            individualHintPanel.setLayout(new GridLayout(1, 0)); // Use a horizontal layout for the hint numbers

            // For each hint in the row, create a label with the corresponding color
            for (int j = 0; j < rowHints.get(i).size(); j++) {
                JLabel hint = new JLabel(String.valueOf(rowHints.get(i).get(j)), SwingConstants.CENTER);
                hint.setOpaque(true);
                hint.setBackground(Color.decode(rowColours.get(i).get(j))); // Set the background color from the model
                hint.setForeground(Color.WHITE); // White text for visibility
                hint.setBorder(new LineBorder(Color.BLACK, 1)); // Border for the hint label

                // If the color is black, change the border to white for better visibility
                if (Objects.equals(Color.decode(rowColours.get(i).get(j)), Color.BLACK)) {
                    hint.setBorder(new LineBorder(Color.WHITE, 1));
                }

                // Add the hint label to the individual hint panel for the row
                individualHintPanel.add(hint);
            }

            // Add the individual hint panel to the main panel
            add(individualHintPanel);
        }
    }

    /**
     * Creates the column hint panel, which displays hints for columns on the left of the grid
     */
    public void createColHintPanel() {
        // Get the hints for the columns and their associated colors
        List<List<Integer>> colHints = model.getLevel().getRowOrColumnHints(model.getLevel().getColHintColors());
        List<List<String>> colColours = model.getLevel().getRowOrColumnColors(model.getLevel().getColHintColors());

        // Set the layout to a grid, with one column for each column of the Nonogram
        setLayout(new GridLayout(1, colHints.size()));

        // Loop through each column hint and create a JLabel for each number in the hint
        for (int i = 0; i < colHints.size(); i++) {
            JPanel individualHintPanel = new JPanel();
            individualHintPanel.setLayout(new GridLayout(0, 1)); // Use a vertical layout for the hint numbers

            // For each hint in the column, create a label with the corresponding color
            for (int j = 0; j < colHints.get(i).size(); j++) {
                JLabel hint = new JLabel(String.valueOf(colHints.get(i).get(j)), SwingConstants.CENTER);
                hint.setOpaque(true);
                hint.setBackground(Color.decode(colColours.get(i).get(j))); // Set the background color from the model
                hint.setForeground(Color.WHITE); // White text for visibility
                hint.setBorder(new LineBorder(Color.BLACK, 1)); // Border for the hint label

                // If the color is black, change the border to white for better visibility
                if (Objects.equals(Color.decode(colColours.get(i).get(j)), Color.BLACK)) {
                    hint.setBorder(new LineBorder(Color.WHITE, 1));
                }

                // Add the hint label to the individual hint panel for the column
                individualHintPanel.add(hint);
            }

            // Add the individual hint panel to the main panel
            add(individualHintPanel);
        }
    }
}
