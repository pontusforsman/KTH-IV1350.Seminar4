package se.kth.iv1350.pos.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import se.kth.iv1350.pos.integration.ItemDTO;

/**
 * Represents a <code>Sale</code> transaction, containing items, totals, and receipt generation. The
 * <code>Sale</code> is managed by the {@link Controller Controller} and provides data via
 * {@link SaleDTO}.
 */
public class Sale {
    private final LinkedHashMap<String, SaleItem> items;
    private final CashRegister cashRegister;
    private final List<RevenueObserver> revenueObservers = new ArrayList<>();
    private Amount total;
    private Amount totalVat;
    private Receipt receipt;
    private SaleState state;

    /**
     * Creates a new instance of <code>Sale</code>. Initializes an empty sale with no items and zero
     * totals.
     */
    public Sale(CashRegister cashRegister) {
        this.items = new LinkedHashMap<>();
        this.cashRegister = cashRegister;
        this.total = Amount.zero();
        this.totalVat = Amount.zero();
        this.state = new SaleInProgressState(this);
    }

    /**
     * Adds an item to the current <code>Sale</code>.
     *
     * @param item The {@link ItemDTO} to add. Must not be null.
     * @return A {@link SaleItemDTO} with updated sale information, or null if item is null.
     */
    public SaleItemDTO addItem(ItemDTO item) {
        return state.addItem(item);
    }

    /**
     * Updates the quantity of the last entered item in the current <code>Sale</code>.
     *
     * @param quantity The <code>int</code> quantity to add. Must be non-negative.
     * @return A {@link SaleItemDTO} with updated sale information, or <code>null</code> if there
     *         are no items in the sale, the quantity is invalid, or no last item exists.
     */
    public SaleItemDTO setLastItemQuantity(int quantity) {
        return state.setLastItemQuantity(quantity);
    }

    /**
     * Completes the <code>Sale</code> and returns the total cost.
     *
     * @return The total cost as an {@link Amount}.
     */
    public Amount completeSale() {
        return state.completeSale();
    }

    /**
     * Registers a payment and returns the change.
     *
     * @param amountPaid The amount paid as an {@link Amount}. Must not be null or negative.
     * @return The change as an {@link Amount}.
     * @throws IllegalArgumentException if the payment amount is null or negative.
     */
    public Amount processPayment(Amount amountPaid) {
        return state.processPayment(amountPaid);
    }

    /**
     * Sets the current state of the sale (used by state pattern).
     * 
     * @param newState The new state to transition to.
     */
    private void setState(SaleState newState) {
        this.state = newState;
    }

    /**
     * Gets the {@link Receipt} for this <code>Sale</code>.
     *
     * @return The {@link Receipt} for this <code>Sale</code>.
     */
    public Receipt getReceipt() {
        return receipt;
    }

    /**
     * Gets the total cost for this sale.
     *
     * @return The total cost as an {@link Amount}.
     */
    public Amount getTotalCost() {
        return total;
    }

    /**
     * Gets the total VAT for this sale.
     *
     * @return The total VAT as an {@link Amount}.
     */
    public Amount getTotalVat() {
        return totalVat;
    }

    /**
     * Creates a {@link SaleDTO} representing this sale's data for transfer between layers.
     *
     * @return a new <code>SaleDTO</code> with all items, total, and VAT.
     */
    public SaleDTO toDTO() {
        List<SaleItemDTO> itemDTOs = items.values().stream().map(SaleItem::toDTO).toList();
        return new SaleDTO(itemDTOs, total, totalVat);
    }

    /**
     * Gets all items in this sale as an unmodifiable map.
     *
     * @return An unmodifiable map of item IDs to {@link SaleItem}.
     */
    Map<String, SaleItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /**
     * Adds a revenue observer to this sale. Null observers are ignored.
     *
     * @param observer The observer to add.
     */
    public void addRevenueObserver(RevenueObserver observer) {
        if (observer != null) {
            revenueObservers.add(observer);
        }
    }

    /**
     * Notifies all registered revenue observers that a sale has been completed.
     *
     * @param saleDTO The completed sale's data.
     */
    public void saleCompleted(SaleDTO saleDTO) {
        notifyRevenueObservers(saleDTO);
    }

    /**
     * Notifies all registered revenue observers with the completed sale data.
     *
     * @param saleDTO The completed sale's data.
     */
    private void notifyRevenueObservers(SaleDTO saleDTO) {
        for (RevenueObserver observer : revenueObservers) {
            observer.saleCompleted(saleDTO);
        }
    }

    /**
     * Updates the running total and VAT for the sale, including any discount.
     */
    void updateRunningTotal() {
        total = Amount.zero();
        totalVat = Amount.zero();
        for (SaleItem item : items.values()) {
            total = total.add(item.getLineTotal());
            totalVat = totalVat.add(item.getLineTotalVat());
        }
    }

    // --- State Pattern ---
    /**
     * State pattern for Sale: controls allowed operations based on sale lifecycle.
     */
    private interface SaleState {
        /** Add an item to the sale (only allowed in-progress). */
        SaleItemDTO addItem(ItemDTO item);

        /** Update quantity of last item (only allowed in-progress). */
        SaleItemDTO setLastItemQuantity(int quantity);

        /** Complete the sale (allowed in-progress and completed). */
        Amount completeSale();

        /** Process payment (only allowed in-progress, once). */
        Amount processPayment(Amount amountPaid);
    }

    /** State for when a sale is in progress and items can be added/modified. */
    private final class SaleInProgressState implements SaleState {
        private final Sale sale;

        SaleInProgressState(Sale sale) {
            this.sale = sale;
        }

        @Override
        public SaleItemDTO addItem(ItemDTO item) {
            return sale.doAddItem(item);
        }

        @Override
        public SaleItemDTO setLastItemQuantity(int quantity) {
            return sale.doSetLastItemQuantity(quantity);
        }

        @Override
        public Amount completeSale() {
            return sale.total;
        }

        @Override
        public Amount processPayment(Amount amountPaid) {
            Amount change = sale.doProcessPayment(amountPaid);
            sale.setState(new SaleCompletedState(sale));
            return change;
        }
    }

    /** State for when a sale is completed and no further changes are allowed. */
    private final class SaleCompletedState implements SaleState {
        private final Sale sale;

        SaleCompletedState(Sale sale) {
            this.sale = sale;
        }

        @Override
        public SaleItemDTO addItem(ItemDTO item) {
            throw new IllegalStateException("Cannot add items after sale is completed.");
        }

        @Override
        public SaleItemDTO setLastItemQuantity(int quantity) {
            throw new IllegalStateException("Cannot update items after sale is completed.");
        }

        @Override
        public Amount completeSale() {
            return sale.total;
        }

        @Override
        public Amount processPayment(Amount amountPaid) {
            throw new IllegalStateException("Payment already processed for this sale.");
        }
    }

    // --- do* methods: Only called by state classes to perform the actual logic. ---
    /**
     * Actually adds an item to the sale. Only called by state classes.
     */
    private SaleItemDTO doAddItem(ItemDTO item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        if (items.containsKey(item.id())) {
            items.get(item.id()).incrementQuantity();
        } else {
            items.put(item.id(), new SaleItem(item, 1));
        }
        updateRunningTotal();
        return new SaleItemDTO(item, items.get(item.id()).getQuantity(), total, totalVat);
    }

    /**
     * Actually updates the quantity of the last item. Only called by state classes.
     */
    private SaleItemDTO doSetLastItemQuantity(int quantity) {
        if (items.isEmpty() || items.lastEntry() == null) {
            throw new IllegalStateException("No items in sale to update quantity");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        var lastEntry = items.lastEntry();
        if (quantity == 0) {
            items.remove(lastEntry.getKey());
            updateRunningTotal();
            return null;
        }
        lastEntry.getValue().updateQuantity(quantity);
        updateRunningTotal();
        return new SaleItemDTO(lastEntry.getValue().getItem(), lastEntry.getValue().getQuantity(),
                total, totalVat);
    }

    /**
     * Actually processes payment for the sale. Only called by state classes.
     */
    private Amount doProcessPayment(Amount amountPaid) {
        if (amountPaid == null) {
            throw new IllegalArgumentException("Amount paid cannot be null");
        }
        if (amountPaid.isNegative()) {
            throw new IllegalArgumentException("Payment amount must be non-negative");
        }
        CashPayment cashPayment = new CashPayment(amountPaid);
        cashPayment.calculateTotalCost(this);
        cashRegister.updateBalance(cashPayment.getAmountPaid());
        Amount change = amountPaid.subtract(total);
        receipt = new Receipt(this, amountPaid, change);
        saleCompleted(this.toDTO());
        return change;
    }
}
