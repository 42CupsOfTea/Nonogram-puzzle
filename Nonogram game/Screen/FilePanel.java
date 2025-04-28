package Screen;

import MainProgram.Level;
import MainProgram.NonogramModel;
import MainProgram.PuzzleLoadException;
import MainProgram.PuzzleLoader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import org.json.JSONException;
import MainProgram.Constants;

/**
 * Defines the panel (on the northern part of the gui) which holds different buttons
 */
public class FilePanel extends JPanel implements ActionListener {
    private final NonogramModel model;
    private final GameUI GUI;
    private JButton savePuzzleButton;
    private JButton loadPuzzleButton;
    private JButton choosePuzzleButton;
    private Level oldLevel;

    /**
     * Constructor to initialize the panel
     * @param model nonogram model
     * @param GUI current graphical user interface
     */
    public FilePanel(NonogramModel model, GameUI GUI) {
        this.model = model;
        this.GUI = GUI;
        initializePanel();
    }

    /**
     * Initialize the panel with buttons and layout
     */
    private void initializePanel() {
        setBackground(Color.decode(Constants.NORTH_PANEL)); // Background color from Constants
        setLayout(new BorderLayout());
        setOpaque(true);
        
        oldLevel = model.getLevel();  // Save the current level

        // Panel to choose a new puzzle
        JPanel chooseFileButtonPanel = new JPanel();
        chooseFileButtonPanel.setOpaque(false);
        choosePuzzleButton = new JButton("Choose Puzzle");
        choosePuzzleButton.addActionListener(this);
        chooseFileButtonPanel.add(choosePuzzleButton);

        // Label showing the current level's name and grid dimensions
        JPanel labelPanel = new JPanel();
        labelPanel.setOpaque(false);
        JLabel nameOfLevel = new JLabel(model.getLevel().getName() + " " + model.getRows() + " X " + model.getCols());
        nameOfLevel.setFont(new Font("Arial", Font.BOLD, 40));
        nameOfLevel.setForeground(Color.decode("#F1FAEE"));
        labelPanel.add(nameOfLevel);

        // Panel with buttons to save and load puzzles
        JPanel saveAndLoadPuzzleButtonPanel = new JPanel();
        saveAndLoadPuzzleButtonPanel.setOpaque(false);
        savePuzzleButton = new JButton("Save Puzzle");
        loadPuzzleButton = new JButton("Load Puzzle");
        loadPuzzleButton.addActionListener(this);
        savePuzzleButton.addActionListener(this);
        saveAndLoadPuzzleButtonPanel.add(savePuzzleButton);
        saveAndLoadPuzzleButtonPanel.add(loadPuzzleButton);

        // Adding panels to the main panel
        add(chooseFileButtonPanel, BorderLayout.WEST);
        add(labelPanel, BorderLayout.CENTER);
        add(saveAndLoadPuzzleButtonPanel, BorderLayout.EAST);
    }

    /**
     * ActionListener to handle button clicks
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == choosePuzzleButton) {
            choosePuzzleButtonPressed();
        } else if (source == savePuzzleButton) {
            savePuzzleButtonPressed();
        } else if (source == loadPuzzleButton) {
            loadPuzzleButtonPressed();
        }
    }

    /**
     * Method to choose a new puzzle file
     */
    private void choosePuzzleButtonPressed() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("./data")); // Set current directory for puzzle files

        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Level newLevel = null;
            try {
                newLevel = new PuzzleLoader(selectedFile.getAbsolutePath()).getLevel();  // Load new level
            } catch (PuzzleLoadException e) {
                handlePuzzleLoadError(e);
                newLevel = oldLevel;  // If error, revert to old level
            }
            model.setLevel(newLevel);  // Set the new level in the model
            List<String> currentColOptions = model.getLevel().getColorOptions();  // Get color options
            model.setCurrentFillColor(Color.decode(currentColOptions.get(currentColOptions.size() - 1)));  // Set the initial fill color
            GUI.updateGUI();  // Update the GUI with the new puzzle
        }
    }

    /**
     * Handle errors when loading a puzzle
     * @param e the puzzle loading exception
     */
    private void handlePuzzleLoadError(PuzzleLoadException e) {
        Throwable cause = e.getCause();
        String errorMessage;
        
        if (cause instanceof IOException) {
            errorMessage = "Could not read the puzzle file:\n" + e.getMessage();
        } else if (cause instanceof JSONException) {
            errorMessage = "Invalid puzzle file format:\n" + e.getMessage();
        } else {
            errorMessage = "An unexpected error occurred:\n" + e.getMessage();
        }

        // Show error message dialog
        JOptionPane.showMessageDialog(null, errorMessage, "Puzzle Load Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Method to save the current puzzle
     */
    private void savePuzzleButtonPressed() {
        model.savePuzzle();
        JOptionPane.showMessageDialog(null, "Puzzle saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Method to load a saved puzzle
     */
    private void loadPuzzleButtonPressed() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            String directoryPath = "./savedPuzzels/" + model.getLevel().getName();
            fileChooser.setCurrentDirectory(new File(directoryPath));

            int result = fileChooser.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File saveDirectory = fileChooser.getSelectedFile();
                File[] files = saveDirectory.listFiles();
                Arrays.sort(files);  // Sort files to ensure correct order (color_save first, states_save second)
                File colorSave = files[0];
                File stateSave = files[1];

                if (!model.getLevel().getName().equals(saveDirectory.getParentFile().getName())) {
                    throw new PuzzleLoadException("Trying to load save state from " + saveDirectory.getParentFile().getName() + " but can only load from " + model.getLevel().getName());
                }
                model.loadPuzzle(stateSave, colorSave);  // Load the saved puzzle state and colors
            }

            // Update the grid in the GUI
            for (int i = 0; i < model.getRows(); i++) {
                for (int j = 0; j < model.getCols(); j++) {
                    GUI.updateButton(i, j, model.getGridColor(i, j));
                }
            }
        } catch (PuzzleLoadException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while loading the puzzle: " + e.getMessage(), "Puzzle Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
