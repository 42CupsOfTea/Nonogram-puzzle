package Screen;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import MainProgram.NonogramModel;

/**
 * Class defines behaviours once a button has been clicked along the nonogram grid
 */
public class ButtonClicked implements MouseListener {
    private final int row;        // Row index for the button (grid cell)
    private final int col;        // Column index for the button (grid cell)
    private final NonogramModel model;  // The model representing the game state
    private final GameUI GUI;           // The graphical user interface

    /**
     * Constructor to initialize the row, column, model, and GUI
     * @param row row number
     * @param col column number
     * @param model the nonogram model
     * @param GUI the current graphical user interface
     */
    public ButtonClicked(int row, int col, NonogramModel model, GameUI GUI) {
        this.row = row;
        this.col = col;
        this.model = model;
        this.GUI = GUI;
    }

    /**
     * Mouse press event handler
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        // Toggle the state of the cell when the mouse is pressed
        model.toggleCell(row, col);
        // Update the button in the GUI to reflect the new state
        GUI.updateButton(row, col, model.getCurrentFillColor());
        // Indicate that the user is now dragging
        GUI.setDragging(true);
    }

    /**
     * Mouse release event handler
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        // End the dragging action when the mouse is released
        GUI.setDragging(false);
    }

    /**
     * Mouse enter event handler
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        // If the user is still dragging, toggle the cell and update the button
        if (GUI.isDragging()) {
            model.toggleCell(row, col);
            GUI.updateButton(row, col, model.getCurrentFillColor());
        }
    }

    /**
     * Other unused MouseListener methods
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {}

    /**
     * Other unused MouseListener methods
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {}
}
