package MainProgram;
import java.util.ArrayList;
import java.util.List;

/**
 * Class defines information regarding a level, including its constraints, name and colors.
 */
public class Level {
    private final String name; // Name of the level
    private final List<List<HintNumberAndColour>> rowHintColors; // List of row hints with constraint numbers and color
    private final List<List<HintNumberAndColour>> columnHintColors; // List of column hints with constraint numbers and color
    private final List<String> colors; // List of colors used in the level for the nonogram

    /**
     * Constructs a level object that defines the colors and hints of a level
     * @param name name of the level
     * @param colors colors needed to fill in the nonogram
     * @param rowHintColors all row hints composed of constraint numbers and color
     * @param columnHintColors all column hints composed of constraint numbers and colors
     */
    public Level(String name, List<String> colors, List<List<HintNumberAndColour>> rowHintColors, List<List<HintNumberAndColour>> columnHintColors) {
        this.name = name;  // Initialize level name
        this.rowHintColors = rowHintColors;  // Initialize row hints with colors
        this.columnHintColors = columnHintColors;  // Initialize column hints with colors
        this.colors = colors;  // Initialize available colors for the puzzle
    }

    // Getter for level name
    public String getName() {
        return name;  // Return the name of the level
    }

    /**
     * Gets either the constraint numbers of rows or columns (specified by the argument)
     * @param rowOrColHints either column or row hints
     * @return The constraint numbers of rows or columns
     */
    public List<List<Integer>> getRowOrColumnHints(List<List<HintNumberAndColour>> rowOrColHints) {
        List<List<Integer>> Hints = new ArrayList<>();
        // Iterate through each list of hints (rows or columns)
        for (int i = 0; i < rowOrColHints.size(); i++) {
            List<HintNumberAndColour> listOfHints = rowOrColHints.get(i);
            List<Integer> lineHints = new ArrayList<>();
            // For each hint, add the constraint number to the lineHints list
            for (int j = 0; j < listOfHints.size(); j++) {
                lineHints.add(listOfHints.get(j).getHint());
            }
            Hints.add(lineHints);  // Add lineHints to Hints list
        }

        return Hints;  // Return list of constraint numbers
    }

    /**
     * Gets either the color values of hints from rows or columns (specified by the argument)
     * @param rowOrColHints either column or row hints
     * @return The color values of row or column hints
     */
    public List<List<String>> getRowOrColumnColors(List<List<HintNumberAndColour>> rowOrColHints) {
        List<List<String>> Colours = new ArrayList<>();
        // Iterate through each list of hints (rows or columns)
        for (int i = 0; i < rowOrColHints.size(); i++) {
            List<HintNumberAndColour> lineColors = rowOrColHints.get(i);
            List<String> lineColours = new ArrayList<>();
            // For each hint, add its color to the lineColours list
            for (int j = 0; j < lineColors.size(); j++) {
                lineColours.add(lineColors.get(j).getColour());
            }
            Colours.add(lineColours);  // Add lineColours to Colours list
        }

        return Colours;  // Return list of color values
    }

    /**
     * Gets the list of colors needed to fill in the current {@code level} object
     */
    public List<String> getColorOptions() {
        return colors;  // Return the available colors for the level
    }

    /**
     * Gets the row hints, composed of constraint numbers and their corresponding colors
     */
    public List<List<HintNumberAndColour>> getRowHintColors() {
        return rowHintColors;  // Return the row hint colors
    }

    /**
     * Gets column hints, composed of constraint numbers and their corresponding colors
     */
    public List<List<HintNumberAndColour>> getColHintColors() {
        return columnHintColors;  // Return the column hint colors
    }
}
