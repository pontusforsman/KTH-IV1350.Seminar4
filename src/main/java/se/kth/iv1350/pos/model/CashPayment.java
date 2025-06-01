package se.kth.iv1350.pos.model;

/**
 * Represents a <code>CashPayment</code> made by a customer.
 * Used in the payment process of a <code>Sale</code>.
 */
class CashPayment {
    private Amount amountPaid;

    /**
     * Creates a new instance representing a payment with the specified {@link Amount}.
     *
     * @param amountPaid The amount of money paid.
     */
    CashPayment(Amount amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
     * Gets the <code>Amount</code> paid.
     *
     * @return The amount paid, or null if the payment was invalid.
     */
    Amount getAmountPaid() {
        return amountPaid;
    }

    /**
     * Calculates and updates the internal state with the difference between
     * the amount paid and the total cost of the sale.
     * If amountPaid is null, does nothing.
     *
     * @param sale The sale for which payment is being made.
     */
    void calculateTotalCost(Sale sale) {
        if (amountPaid == null) {
            return;
        }
        Amount totalCost = sale.getTotalCost();
        amountPaid = amountPaid.subtract(totalCost);
    }
} 