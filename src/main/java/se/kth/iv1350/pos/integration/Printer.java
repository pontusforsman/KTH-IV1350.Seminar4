package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.Receipt;
import se.kth.iv1350.pos.model.ReceiptDTO;

/**
 * This class is responsible for printing the {@link Receipt} to the console.
 */
public interface Printer {
    /**
     * Prints the specified receipt.
     *
     * @param receipt The receipt string to print.
     */
    public void print(ReceiptDTO receipt);
}
