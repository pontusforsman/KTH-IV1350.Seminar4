package se.kth.iv1350.pos.model;

/**
 * Represents a receipt for a completed sale, including the sale details, amount paid, and change
 * returned. This class is used to encapsulate the information needed for printing or displaying a
 * receipt.
 */
public class Receipt {
    private final Sale sale;
    private final Amount amountPaid;
    private final Amount change;

    /**
     * Creates a new instance representing the receipt of the specified sale.
     *
     * @param sale The sale proved by this receipt.
     */
    /**
     * Creates a new instance representing the receipt of the specified sale.
     *
     * @param sale The sale proved by this receipt.
     * @param amountPaid How much was paid for the sale.
     * @param change The amount of change returned to the customer.
     */
    public Receipt(Sale sale, Amount amountPaid, Amount change) {
        this.sale = sale;
        this.amountPaid = amountPaid;
        this.change = change;
    }

    /**
     * Gets a DTO representation of this receipt for printing.
     *
     * @return a new ReceiptDTO with all receipt data.
     */
    public ReceiptDTO toDTO() {
        return new ReceiptDTO(sale.getItems().values().stream().map(SaleItem::toDTO).toList(),
                sale.getTotalCost(), sale.getTotalVat(), amountPaid, change);
    }
}
