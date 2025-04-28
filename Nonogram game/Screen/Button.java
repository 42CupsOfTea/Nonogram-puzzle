package Screen;

import javax.swing.JButton;
import java.awt.Color;

/**
 * Class defines information on buttons for the nonogram grid
 */
public class Button extends JButton {

    /**
     * Constructor to initialize a button
     */
    public Button() {
        // Set the background color of the button to white
        setBackground(Color.WHITE);

        // Make the button's background opaque, so the color shows
        setOpaque(true);

        // Ensure the content area of the button is filled with the background color
        setContentAreaFilled(true);

        // Enable the button's border to be painted (drawn)
        setBorderPainted(true);
    }
}
