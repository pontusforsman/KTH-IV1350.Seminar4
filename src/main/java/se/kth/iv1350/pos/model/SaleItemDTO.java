package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ItemDTO;

/**
 * Data Transfer Object for a <code>Sale</code> item, used to transfer item data between layers.
 * Contains the {@link ItemDTO}, quantity, line total, and line VAT as {@link Amount}.
 */
public record SaleItemDTO(ItemDTO item, int quantity, Amount total, Amount totalVat) {
}
