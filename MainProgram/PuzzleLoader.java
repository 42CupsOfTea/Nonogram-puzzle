package MainProgram;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.json.*;

/**
 * Class responsible for loading a nonogram puzzle level from a JSON file.
 */
public class PuzzleLoader {

    private Level level;  // Holds the level information after it is loaded

    /**
     * Constructor initializes a PuzzleLoader object and loads a level from the given JSON file path.
     * @param jsonFilePath The path to the JSON file containing the nonogram level.
     * @throws PuzzleLoadException If there is an error loading the puzzle.
     */
    public PuzzleLoader(String jsonFilePath) throws PuzzleLoadException {
        // Attempt to load the level from the specified JSON file
        loadLevel(jsonFilePath);
    }

    /**
     * Returns the loaded level.
     * @return The Level object representing the loaded puzzle.
     */
    public Level getLevel () {
        return level;
    }

    /**
     * Loads the nonogram level from a JSON file.
     * @param jsonFilePath The path to the JSON file.
     * @throws PuzzleLoadException If the file cannot be read or if the format is invalid.
     */
    private void loadLevel(String jsonFilePath) throws PuzzleLoadException {
        List<String> colorList = new ArrayList<>();  // List to hold the colors used in the puzzle

        try {
            // Read the JSON file as a string
            String jsonFile = new String(Files.readAllBytes(Paths.get(jsonFilePath)));

            // Parse the JSON string into a JSONObject
            JSONObject levelObject = new JSONObject(jsonFile);
            String levelName = levelObject.getString("name");  // Get the name of the level

            // Attempt to load the colors for the puzzle (if specified)
            try {
                JSONObject colors = levelObject.getJSONObject("states");
                colorList = getColorOptions(colors);  // Extract the color options
            } catch (JSONException e) {  // If no colors are specified, default to black
                colorList.add("0x000000");
            }

            // Load the row constraints and colors
            JSONArray rows = levelObject.getJSONArray("rows");
            List<List<HintNumberAndColour>> rowHintsAndColors = getHintsAndColors(rows, colorList);

            // Load the column constraints and colors
            JSONArray columns = levelObject.getJSONArray("columns");
            List<List<HintNumberAndColour>> columnHintsAndColors = getHintsAndColors(columns, colorList);

            // Create a new Level object with the loaded data
            level = new Level(levelName, colorList, rowHintsAndColors, columnHintsAndColors);

        } catch (IOException e) {
            // Handle errors reading the file
            throw new PuzzleLoadException("Failed to read puzzle file: " + e.getMessage(), e);
        } catch (JSONException e) {
            // Handle errors with the JSON format
            throw new PuzzleLoadException("Invalid puzzle file format: " + e.getMessage(), e);
        } catch (Exception e) {
            // Handle any other unexpected errors
            throw new PuzzleLoadException("Error loading puzzle: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts the colors specified in the puzzle JSON file.
     * @param colors The JSON object containing color information.
     * @return A list of color strings representing the colors in the puzzle.
     */
    private List<String> getColorOptions(JSONObject colors) {
        List<String> colorList = new ArrayList<>();
        int incrementer = 1;  // Used to check for color keys like "COLOUR_1", "COLOUR_2", etc.

        // Loop through the keys in the 'states' JSON object
        for (String key : colors.keySet()) {
            // If the key matches the format "COLOUR_X", add the corresponding color to the list
            if (key.equals("COLOUR_".concat(String.valueOf(incrementer)))) {
                colorList.add(colors.getString(key));
                incrementer++;
            }
        }
        return colorList;
    }

    /**
     * Extracts the hints (numbers and colors) for each row or column in the nonogram.
     * @param colorsArray The JSON array containing the hint data for rows or columns.
     * @param colorList The list of colors used in the puzzle.
     * @return A 2D list of HintNumberAndColour objects representing the constraints for each row/column.
     */
    public List<List<HintNumberAndColour>> getHintsAndColors(JSONArray colorsArray, List<String> colorList) {
        List<List<HintNumberAndColour>> hints = new ArrayList<>();

        // Loop through each element (row or column) in the colorsArray
        for (int i = 0; i < colorsArray.length(); i++) {
            JSONArray colorsJsonArray = colorsArray.getJSONArray(i);  // Get the hints for the current row/column
            List<HintNumberAndColour> currentColHints = new ArrayList<>();

            // Loop through the hints in the current row/column
            for (int j = 0; j < colorsJsonArray.length(); j++) {
                JSONObject hint = colorsJsonArray.getJSONObject(j);
                int count = hint.getInt("count");  // Get the count of consecutive cells
                String color;

                // Try to get the color for the hint, default to black if not specified
                try {
                    color = hint.getString("color");
                    String number = color.replaceAll("[^0-9]", "");
                    int index = Integer.parseInt(number) - 1;  // Parse the color index
                    color = colorList.get(index);  // Get the actual color from the list
                } catch (JSONException e) {
                    color = "#000000";  // Default to black if no color is specified
                }

                // Add the hint (count and color) to the list
                currentColHints.add(new HintNumberAndColour(count, color));
            }
            hints.add(currentColHints);  // Add the current row/column's hints to the overall list
        }

        return hints;
    }
}
