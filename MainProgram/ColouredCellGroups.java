package MainProgram;

import java.awt.*;
import java.util.Objects;

/**
 * Class holds information on groups of colored cells
 */
public class ColouredCellGroups {
    private Color colour;
    private int count;

    /**
     * Constructor to initialize a coloured cell group.
     * @param colour the color of the cell group
     * @param noOfCells the number of cells in the group
     */
    public ColouredCellGroups(Color colour, int noOfCells) {
        this.colour = colour;
        this.count = noOfCells;
    }


    /**
     * Returns the number of cells in a group
     * @return count
     */
    public Integer getCount() { return count; }



    /**
     * Sets the cell count of a group
     * @param count count
     */
    public void setCount(int count) { this.count = count; }

    /**
     * Compares this object with another for equality.
     * @param obj the object to compare
     * @return true if both objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object check
        if (!(obj instanceof ColouredCellGroups cellGroups)) return false; // Check if obj is a ColouredCellGroup

        // Compare colour and count properties for equality
        return count == cellGroups.count && colour.equals(cellGroups.colour);
    }

    /**
     * Generates a hash code based on colour and count.
     * @return hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(colour, count); // Generate hash using colour and count
    }
}
