package Screen;

import MainProgram.Constants;
import MainProgram.NonogramModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * Panel stores buttons which can be used to switch between colors while filling in a nonogram
 */
public class ColoredButtonsPanel extends JPanel implements ActionListener {
    public NonogramModel model;  // The game model that holds the current state of the puzzle
    public GameUI gameUI;  // The user interface for the game
    public List<String> colorsOfLevel;  // List of color hex codes available for the current level
    public LinkedHashMap<JButton, Color> ButtonsWithColors;  // Maps a button to its corresponding color

    /**
     * Constructor to initialize the panel with the model and game UI
     * @param model nonogram model
     * @param gameUI current graphical user interface
     */
    public ColoredButtonsPanel(NonogramModel model, GameUI gameUI) {
        this.model = model;
        this.gameUI = gameUI;
        this.colorsOfLevel = model.getLevel().getColorOptions();
        initialisePanel();  // Call the method to initialize the panel
    }

    /**
     * Method to initialize the panel with color buttons
     */
    private void initialisePanel(){
        setBackground(Color.decode(Constants.NORTH_PANEL));  // Set the background color of the panel
        setLayout(new FlowLayout());  // Use FlowLayout to arrange the buttons
        List<JPanel> panels = new ArrayList<>();  // List to hold the individual panels for each color button
        ButtonsWithColors = new LinkedHashMap<>();  // Initialize the map to store buttons and their associated colors

        // Loop through the colors and create buttons for each
        for (String color : colorsOfLevel) {
            JPanel panel = new JPanel();  // Create a panel for each button
            JButton button = new JButton();
            button.setBackground(Color.decode(color));  // Set the button's background color
            button.setPreferredSize(new Dimension(50, 50));  // Set a preferred size for the button
            button.addActionListener(this);  // Add an action listener to handle button clicks
            panel.add(button);  // Add the button to the panel
            panels.add(panel);  // Add the panel to the list
            ButtonsWithColors.put(button, Color.decode(color));  // Add the button and its color to the map
        }

        // Try to add the first few panels to the main panel
        try {
            add(panels.get(0), FlowLayout.LEFT);
            add(panels.get(1), FlowLayout.LEFT);
            add(panels.get(2), FlowLayout.LEFT);
            add(panels.get(3), FlowLayout.LEFT);
        }
        catch (Exception e) {
            // If there are fewer panels than expected, do nothing
        }
    }

    /**
     *  Handle the action when a button is clicked
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        // Loop through the map of buttons and colors to find the button that was clicked
        for (Map.Entry<JButton, Color> entry : ButtonsWithColors.entrySet()) {
            JButton button = entry.getKey();
            Color color = entry.getValue();
            if (source == button) {
                changeFillBoxColor(color);  // Change the fill color to the selected color
            }
        }
    }

    /**
     *  Method to update the current fill color in the model
     * @param color color to fill the nonogram with
     */
    public void changeFillBoxColor(Color color) {
        model.setCurrentFillColor(color);  // Set the color in the model
    }
}
