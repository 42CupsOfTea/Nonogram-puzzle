package MainProgram;

/**
 * Custom exception class for handling errors when loading a nonogram puzzle.
 */
public class PuzzleLoadException extends Exception {
    
    /**
     * Constructor for the exception with a specific error message.
     * @param message The error message to be passed to the exception.
     */
    public PuzzleLoadException(String message) {
        super(message);  // Pass the message to the parent Exception class
    }

    /**
     * Constructor for the exception with a specific error message and cause.
     * @param message The error message to be passed to the exception.
     * @param cause The underlying cause (another exception) for this exception.
     */
    public PuzzleLoadException(String message, Throwable cause) {
        super(message, cause);  // Pass the message and cause to the parent Exception class
    }
}
