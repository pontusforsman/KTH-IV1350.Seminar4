package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.integration.ItemDTO;

/**
 * Represents an item in a <code>Sale</code>, including its quantity and price calculations. Used
 * internally by {@link Sale} and converted to {@link SaleItemDTO} for transfer between layers.
 */
class SaleItem {
    private final ItemDTO item;
    private int quantity;

    SaleItem(ItemDTO item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Gets the item data.
     *
     * @return The {@link ItemDTO} for this sale item.
     */
    ItemDTO getItem() {
        return item;
    }

    /**
     * Gets the quantity of this item in the sale.
     *
     * @return The quantity.
     */
    int getQuantity() {
        return quantity;
    }

    /**
     * Gets the line total (price × quantity), assuming price already includes VAT.
     *
     * @return The total amount for this item line.
     */
    Amount getLineTotal() {
        double itemPrice = item.price();
        double vatRate = item.vatRate();
        // Add VAT to price
        return Amount.of(itemPrice * (1 + vatRate) * quantity);
    }

    /**
     * Gets the total VAT for this item line.
     *
     * @return The VAT amount for this item line.
     */
    Amount getLineTotalVat() {
        double itemPrice = item.price();
        double vatRate = item.vatRate();
        return Amount.of(itemPrice * vatRate * quantity);
    }

    /**
     * Converts this sale item to a DTO for transfer between layers.
     *
     * @return The {@link SaleItemDTO} representing this item.
     */
    SaleItemDTO toDTO() {
        return new SaleItemDTO(item, quantity, getLineTotal(), getLineTotalVat());
    }

    /**
     * Updates the quantity of this item in the sale. Negative quantities are ignored.
     *
     * @param quantity The quantity to add (must be non-negative).
     */
    void updateQuantity(int quantity) {
        if (quantity < 0) {
            return;
        }
        this.quantity = quantity;
    }

    /**
     * Increments the quantity of this item in the sale by 1.
     */
    void incrementQuantity() {
        this.quantity++;
    }
}
