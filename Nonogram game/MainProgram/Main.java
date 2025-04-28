package MainProgram;

import javax.swing.SwingUtilities;
import Screen.GameUI;

/**
 * Class facilitates the running of the solution
 */
public class Main {

    /**
     * Main method which loads the nonogram solution
     * @param args Command-line arguments (not used in this case)
     */
    public static void main(String[] args) {
        // This ensures the code is executed on the Event Dispatch Thread (EDT) for Swing UI
        SwingUtilities.invokeLater(() -> {
            // Initialize the currentLevel object to null
            Level currentLevel = null;

            try {
                // Load the current level from a JSON file using PuzzleLoader
                // This loads all the necessary data (row/column hints, color info, etc.)
                currentLevel = new PuzzleLoader("data/smiler.json").getLevel();
            } catch (PuzzleLoadException e) {
                // If loading the level fails, the catch block handles the exception.
                // In this case, the game won't do anything further (can be improved later)
            }

            // Once the level is loaded successfully, initialize the NonogramModel with it
            // This model handles all the game logic and state management
            NonogramModel model = new NonogramModel(currentLevel);

            // Create a new GameUI object, passing the model, and make it visible on the screen
            // The GUI will display the game based on the state of the model
            new GameUI(model).setVisible(true);
        });
    }
}
