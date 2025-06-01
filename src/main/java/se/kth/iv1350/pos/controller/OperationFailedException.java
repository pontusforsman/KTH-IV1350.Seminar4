package se.kth.iv1350.pos.controller;

/**
 * Thrown when an operation in the controller layer fails due to an unexpected system error. Wraps
 * lower-level exceptions that should not be exposed to the view.
 */
public class OperationFailedException extends Exception {
    /**
     * Creates a new instance with a message and a cause.
     * 
     * @param message The message describing the failure.
     * @param cause The underlying cause of the failure.
     */
    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
