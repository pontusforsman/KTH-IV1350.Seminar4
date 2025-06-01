package se.kth.iv1350.pos.model;

import java.util.List;

/**
 * Data Transfer Object (DTO) for a receipt. This class encapsulates the details of a sale, the
 * amount paid, and any change given.
 * 
 * @param items The list of items in the sale.
 * @param total The total amount for the sale.
 * @param totalVat The total VAT applied to the sale.
 * @param discount The discount applied to the sale.
 * @param amountPaid The total amount paid by the customer.
 * @param change The change returned to the customer after payment.
 */
public record ReceiptDTO(List<SaleItemDTO> items, Amount total, Amount totalVat,
                Amount amountPaid, Amount change) {
}