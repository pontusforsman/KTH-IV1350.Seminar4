package se.kth.iv1350.pos.controller;

import java.util.ArrayList;
import java.util.List;

import se.kth.iv1350.pos.integration.AccountingRegistry;
import se.kth.iv1350.pos.integration.DabaseFailureException;
import se.kth.iv1350.pos.integration.InventoryRegistry;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.ItemNotFoundException;
import se.kth.iv1350.pos.integration.Printer;
import se.kth.iv1350.pos.integration.RegistryCreator;
import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.CashRegister;
import se.kth.iv1350.pos.model.Receipt;
import se.kth.iv1350.pos.model.ReceiptDTO;
import se.kth.iv1350.pos.model.RevenueObserver;
import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.model.SaleItemDTO;
import se.kth.iv1350.pos.util.FileLogger;

/**
 * The <code>Controller</code> handles all calls to the model layer and coordinates the sale
 * process. It acts as the only entry point from the view to the model, ensuring low coupling
 * between layers.
 */
public class Controller {
    private final InventoryRegistry inventoryRegistry;
    private final AccountingRegistry accountingRegistry;
    private final Printer printer;
    private final CashRegister cashRegister;
    private Sale currentSale;
    private final List<RevenueObserver> revenueObservers = new ArrayList<>();

    /**
     * Creates a new <code>Controller</code> instance, with the given {@link RegistryCreator} and
     * {@link Printer}. Also, instantiates the {@link CashRegister}.
     *
     * @param registryCreator Used to get access to external systems.
     * @param printer Used to print the {@link Receipt}.
     */
    public Controller(RegistryCreator registryCreator, Printer printer) {
        inventoryRegistry = registryCreator.getInventoryRegistry();
        accountingRegistry = registryCreator.getAccountingRegistry();
        this.printer = printer;
        this.cashRegister = new CashRegister();
    }

    /**
     * Registers a revenue observer to be attached to all future sales.
     */
    public void addRevenueObserver(RevenueObserver observer) {
        revenueObservers.add(observer);
    }

    /**
     * Starts a new <code>Sale</code>.
     *
     * @throws IllegalStateException if a sale is already in progress.
     */
    public void startSale() {
        if (currentSale != null) {
            FileLogger.log("A sale is already in progress.", new IllegalStateException());
            throw new IllegalStateException("A sale is already in progress.");
        }
        currentSale = new Sale(cashRegister);
        for (RevenueObserver observer : revenueObservers) {
            currentSale.addRevenueObserver(observer);
        }
    }

    /**
     * Adds an item to the current {@link Sale}.
     *
     * @param itemID The <code>String</code> ID of the item to add.
     * @return Information about the added item as a {@link SaleItemDTO}.
     * @throws IllegalStateException if no sale is started.
     * @throws ItemNotFoundException if the item was not found in the inventory.
     * @throws OperationFailedException if a database error occurred or other system error.
     */
    public SaleItemDTO enterItem(String itemID)
            throws ItemNotFoundException, OperationFailedException {
        if (currentSale == null) {
            throw new IllegalStateException("No sale in progress. Call startSale() first.");
        }
        try {
            ItemDTO item = inventoryRegistry.findItemById(itemID);
            return currentSale.addItem(item);
        } catch (ItemNotFoundException e) {
            FileLogger.log("Item not found: " + itemID, e);
            throw e; // Propagate to view for user-friendly message
        } catch (DabaseFailureException e) {
            FileLogger.log("Inventory database error for item: " + itemID, e);
            throw new OperationFailedException(
                    "Could not search for item due to system error. Please try again.", e);
        } catch (Exception e) {
            FileLogger.log("Unexpected error in enterItem for item: " + itemID, e);
            throw new OperationFailedException(
                    "Unexpected system error occurred. Please contact support.", e);
        }
    }

    /**
     * Updates the quantity of the last entered item in the current {@link Sale}.
     *
     * @param quantity The <code>int</code> quantity to add.
     * @return Updated information about the item as a {@link SaleItemDTO} with new quantity.
     * @throws IllegalStateException if no sale is started.
     */
    public SaleItemDTO enterQuantity(int quantity) {
        if (currentSale == null) {
            FileLogger.log("No sale in progress. Call startSale() first.",
                    new IllegalStateException());
            throw new IllegalStateException("No sale in progress. Call startSale() first.");
        }
        return currentSale.setLastItemQuantity(quantity);
    }

    /**
     * Ends the current {@link Sale} and returns the total cost, but does NOT reset the sale.
     * Payment must be processed before the sale is reset.
     *
     * @return The total cost of the sale as an {@link Amount}.
     * @throws IllegalStateException if no sale is started.
     */
    public Amount endSale() {
        if (currentSale == null) {
            FileLogger.log("No sale in progress. Call startSale() first.",
                    new IllegalStateException());
            throw new IllegalStateException("No sale in progress. Call startSale() first.");
        }
        return currentSale.completeSale();
    }

    /**
     * Handles a payment for the current <code>Sale</code>. Updates the cash register, external
     * systems, and prints the receipt. Resets the sale after payment.
     *
     * @param amountPaid The amount paid by the customer as an {@link Amount}.
     * @return The change to give back to the customer as an {@link Amount}.
     * @throws IllegalStateException if no sale is started.
     * @throws OperationFailedException if a system error occurs during payment processing.
     */
    public Amount enterPayment(Amount amountPaid) throws OperationFailedException {
        if (currentSale == null) {
            throw new IllegalStateException("No sale in progress. Call startSale() first.");
        }
        try {
            Amount change = currentSale.processPayment(amountPaid);
            updateRegistries();
            sendReceiptToPrinter();
            currentSale = null; // Reset current sale after processing payment
            return change;
        } catch (Exception e) {
            FileLogger.log("Error during payment processing.", e);
            currentSale = null; // Ensure recovery after error
            throw new OperationFailedException("Payment processing failed. Please try again.", e);
        }
    }

    /**
     * Updates external systems (accounting and inventory registries).
     */
    private void updateRegistries() {
        accountingRegistry.updateAccounting(currentSale.toDTO());
        inventoryRegistry.updateInventory(currentSale.toDTO());
    }

    /**
     * Sends the {@link Receipt} for the current <code>Sale</code> to the {@link Printer}.
     */
    private void sendReceiptToPrinter() {
        Receipt receipt = currentSale.getReceipt();
        if (receipt == null) {
            FileLogger.log("No receipt available for current sale.", new IllegalStateException());
            throw new IllegalStateException("No receipt available for current sale");
        }
        ReceiptDTO receiptDTO = receipt.toDTO();
        printer.print(receiptDTO);
    }

    /**
     * Gets the latest receipt for the current or last sale (for logging purposes).
     * 
     * @return The latest Receipt, or null if none exists.
     */
    public Receipt getReceipt() {
        if (currentSale != null) {
            return currentSale.getReceipt();
        }
        return null;
    }
}
