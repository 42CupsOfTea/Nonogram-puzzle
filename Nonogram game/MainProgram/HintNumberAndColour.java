package MainProgram;

/**
 * Class represents a single nonogram constraint, which has a{@code Hint} number and {@code colour}.
 */
public class HintNumberAndColour {
    private Integer Hint;  // Holds the constraint number for the hint
    private String colour; // Holds the color associated with the hint

    /**
     * Constructs a hint, which consists of the constraint number and its colour.
     * @param hint constraint number of a hint
     * @param colour color value for the hint
     */
    public HintNumberAndColour(Integer hint, String colour) {
        this.Hint = hint;  // Initialize the constraint number (hint)
        this.colour = colour;  // Initialize the associated color
    }

    /**
     * Getter for colour
     * @return colour
     */
    public String getColour() {
        return colour;  // Returns the color of the hint
    }

    /**
     * Getter for hint
     * @return Hint
     */
    public Integer getHint() {
        return Hint;  // Returns the constraint number (hint)
    }

}
