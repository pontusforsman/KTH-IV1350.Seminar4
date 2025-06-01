package se.kth.iv1350.pos.integration;

public class ItemNotFoundException extends Exception {

    /**
     * Constructs an ItemNotFoundException with a specified message.
     *
     * @param message The detail message for the exception.
     */
    public ItemNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs an ItemNotFoundException with a specified cause.
     *
     * @param cause The cause of the exception.
     */
    public ItemNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an ItemNotFoundException with a specified message and cause.
     *
     * @param message The detail message for the exception.
     * @param cause The cause of the exception.
     */
    public ItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
