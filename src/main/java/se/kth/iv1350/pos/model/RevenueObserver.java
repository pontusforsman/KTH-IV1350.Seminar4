package se.kth.iv1350.pos.model;

/**
 * Observer interface for receiving notifications when a sale has been completed. Implementations
 * can use the provided {@link SaleDTO} to update total revenue, log sales, or display revenue
 * information.
 */
public interface RevenueObserver {
    /**
     * Called when a sale has been completed.
     *
     * @param sale The completed sale's data, including items, totals, and VAT.
     */
    void saleCompleted(SaleDTO sale);
}
