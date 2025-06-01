package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.controller.OperationFailedException;
import se.kth.iv1350.pos.integration.ItemNotFoundException;
import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.SaleItemDTO;

/**
 * Simulated user interface for the POS system. This class is intended for demonstration and testing
 * purposes only. It makes hardcoded calls to the {@link Controller} and prints all output to a
 * configurable output stream (default is System.out).
 * <p>
 * This class represents the <code>View</code> layer in the application.
 */
public class View {
    private static final String ENTER_ITEM = "Add 1 item with item id ";
    private static final String ENTER_QUANTITY = "Set quantity of last item added to %d";
    private static final String NOT_FOUND = "Item not found";
    private static final String END_SALE = "End sale:";
    private static final String PAYMENT = "Amount paid: %s SEK";
    private static final String CHANGE = "Change: %s";

    private final Controller controller;
    private final java.io.PrintStream out;

    /**
     * Creates a View with a custom output stream (for testability).
     *
     * @param controller The controller to use.
     * @param out The output stream to print to (default is System.out).
     */
    public View(Controller controller, java.io.PrintStream out) {
        this.controller = controller;
        this.out = out != null ? out : System.out;
    }

    /**
     * Creates a View with System.out as the output stream.
     *
     * @param controller The controller to use.
     */
    public View(Controller controller) {
        this(controller, System.out);
    }

    /**
     * Simulates user input that drives the program execution, including a discount request.
     */
    public void sampleExecution() {
        displayStartSale();
        displayEnterItem("2");
        displayEnterItem("3");
        int quantityToSet = 3;
        displayEnterQuantity(quantityToSet);
        if (displayEndSale()) {
            double PAYMENT_AMOUNT = 200;
            displayChange(PAYMENT_AMOUNT);
        }

        displayStartSale();
        displayEnterItem("1");
        quantityToSet = 100;
        displayEnterQuantity(quantityToSet);
        if (displayEndSale()) {
            double PAYMENT_AMOUNT2 = 10000;
            displayChange(PAYMENT_AMOUNT2);
        }

        // Additional sample sale
        displayStartSale();
        displayEnterItem("fail");
        displayEnterItem("2");
        int quantityToSet2 = 2;
        displayEnterQuantity(quantityToSet2);
        if (displayEndSale()) {
            double PAYMENT_AMOUNT3 = 500;
            displayChange(PAYMENT_AMOUNT3);
        }

        // Additional: Try invalid and fail item IDs to show error handling
        displayStartSale();
        displayEnterItem("invalid");
        displayEnterItem("dbfail");
        if (displayEndSale()) {
            double PAYMENT_AMOUNT4 = 100;
            displayChange(PAYMENT_AMOUNT4);
        }

        // Do not start a new sale after a cancelled sale
        System.out.println("=========================================");
    }

    private void displayStartSale() {
        StringBuilder builder = new StringBuilder();
        appendLine(builder, "Starting a new sale...");
        endSection(builder);
        print(builder);
        controller.startSale();
    }

    /**
     * Displays the change after payment.
     *
     * @param paymentAmount The amount paid by the customer.
     */
    private void displayChange(double paymentAmount) {
        StringBuilder builder = new StringBuilder();
        appendLine(builder, String.format(PAYMENT, paymentAmount));
        try {
            Amount change = controller.enterPayment(Amount.of(paymentAmount));
            appendLine(builder, String.format(CHANGE, change));
        } catch (OperationFailedException e) {
            handleException(builder, e);
        }
        endSection(builder);
        print(builder);
    }

    /**
     * Displays the running total with item details after adding an item to the sale.
     *
     * @param itemID The ID of the item to add.
     */
    private void displayEnterItem(String itemID) {
        StringBuilder builder = new StringBuilder();
        appendLine(builder, ENTER_ITEM + itemID + " :");
        if (!isValidItemID(itemID)) {
            appendLine(builder, "Invalid item ID. Please enter a numeric ID or 'fail'/'dbfail'.");
            endSection(builder);
            print(builder);
            return;
        }
        try {
            var item = controller.enterItem(itemID);
            displayItemDetails(builder, item);
        } catch (OperationFailedException | ItemNotFoundException e) {
            handleException(builder, e);
        }
        print(builder);
    }

    /**
     * Displays the running total with item details after entering a quantity.
     *
     * @param quantity The quantity to set.
     */
    private void displayEnterQuantity(int quantity) {
        StringBuilder builder = new StringBuilder();
        appendLine(builder, String.format(ENTER_QUANTITY, quantity));
        if (!isValidQuantity(quantity)) {
            appendLine(builder, "Invalid quantity. Please enter a positive number.");
            endSection(builder);
            print(builder);
            return;
        }
        try {
            var updatedItem = controller.enterQuantity(quantity);
            displayItemDetails(builder, updatedItem);
        } catch (Exception e) {
            handleException(builder, e);
        }
        print(builder);
    }

    /**
     * Displays the item details and total cost.
     *
     * @param builder The {@link StringBuilder} to append the details to.
     * @param item The SaleItemDTO containing item details.
     */
    private void displayItemDetails(StringBuilder builder, SaleItemDTO item) {
        appendLine(builder, "Item ID: " + item.item().id());
        appendLine(builder, "Item name: " + item.item().name());
        appendLine(builder, "Item cost: " + item.item().price());
        appendLine(builder, "VAT: " + formatPercentage(item.item().vatRate()));
        appendLine(builder, "Item description: " + item.item().description());
        endSection(builder);
        appendLine(builder, "Total cost (incl VAT): " + item.total());
        appendLine(builder, "Total VAT: " + item.totalVat());
        endSection(builder);
    }

    /**
     * Displays the end of the sale and the total cost.
     * 
     * @return true if the sale was successful, false if cancelled.
     */
    private boolean displayEndSale() {
        StringBuilder builder = new StringBuilder();
        appendLine(builder, END_SALE);
        Amount total = controller.endSale();
        if (total != null && total.asDouble() > 0.0) {
            appendLine(builder, "Total cost (incl VAT): " + total);
            endSection(builder);
            print(builder);
            return true;
        } else {
            appendLine(builder,
                    "No items were successfully registered for this sale. Sale cancelled.");
            endSection(builder);
            print(builder);
            return false;
        }
    }

    /**
     * Formats the percentage to a string with the format "0%".
     *
     * @param percentage The percentage to format.
     * @return The formatted string.
     */
    private String formatPercentage(double percentage) {
        return (int) (percentage * 100) + "%";
    }

    /**
     * Appends a line to the StringBuilder.
     *
     * @param builder The StringBuilder to append to.
     * @param line The line to append.
     */
    private void appendLine(StringBuilder builder, String line) {
        builder.append(line);
        builder.append("\n");
    }

    /**
     * Ends the section by appending a new line.
     *
     * @param builder The {@link StringBuilder} to append to.
     */
    private void endSection(StringBuilder builder) {
        builder.append("\n");
    }

    /**
     * Prints the contents of the StringBuilder to the configured output stream.
     *
     * @param builder The StringBuilder containing the output.
     */
    private void print(StringBuilder builder) {
        out.print(builder.toString());
    }

    /**
     * Handles exceptions and displays a user-friendly message.
     *
     * @param builder The StringBuilder to append the message to.
     * @param e The exception to handle.
     */
    private void handleException(StringBuilder builder, Exception e) {
        if (e instanceof OperationFailedException) {
            appendLine(builder, "A system error occurred. Please try again later.");
        } else if (e instanceof ItemNotFoundException) {
            appendLine(builder, NOT_FOUND);
        } else if (e instanceof IllegalStateException) {
            appendLine(builder, "No sale in progress. Please start a new sale.");
        } else {
            appendLine(builder, "An unexpected error occurred.");
        }
        endSection(builder);
    }

    /**
     * Validates the item ID using OOD principles. Accepts only numeric IDs or known test cases
     * ("fail", "dbfail").
     *
     * @param itemID The item ID to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidItemID(String itemID) {
        if (itemID == null || itemID.isBlank())
            return false;
        if (itemID.equals("fail") || itemID.equals("dbfail"))
            return true;
        return itemID.matches("\\d+");
    }

    /**
     * Validates the quantity using OOD principles.
     *
     * @param quantity The quantity to validate.
     * @return true if valid, false otherwise.
     */
    private boolean isValidQuantity(int quantity) {
        return quantity > 0;
    }
}
