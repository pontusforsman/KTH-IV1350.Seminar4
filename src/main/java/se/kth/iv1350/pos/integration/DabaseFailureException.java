package se.kth.iv1350.pos.integration;

/**
 * Thrown to indicate a database failure in the inventory system (e.g., server not running).
 */
public class DabaseFailureException extends Exception {
    /**
     * Creates a new instance with a detailed message.
     * 
     * @param itemId The item identifier that triggered the database failure.
     */
    public DabaseFailureException(String itemId) {
        super("Database failure when searching for item ID '" + itemId + "'.");
    }
}
