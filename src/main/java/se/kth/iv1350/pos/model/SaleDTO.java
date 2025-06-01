package se.kth.iv1350.pos.model;

import java.util.List;

/**
 * Data Transfer Object for a completed <code>Sale</code>, used to transfer sale data between layers.
 * Contains a list of {@link SaleItemDTO}, the total {@link Amount}, and total VAT as {@link Amount}.
 */
public record SaleDTO(List<SaleItemDTO> items, Amount total, Amount totalVat) {
} 