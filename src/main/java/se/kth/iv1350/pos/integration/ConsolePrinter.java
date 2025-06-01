package se.kth.iv1350.pos.integration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.ReceiptDTO;

public class ConsolePrinter implements Printer {

    /**
     * Prints the specified receipt to the console.
     *
     * @param receipt The receipt to print.
     */
    @Override
    public void print(ReceiptDTO receipt) {
        String receiptString = createReceiptString(receipt);
        System.out.println();
        System.out.println(receiptString);
    }

    public String createReceiptString(ReceiptDTO receipt) {
        StringBuilder builder = new StringBuilder();
        appendReceiptHeader(builder);
        appendSaleItems(builder, receipt);
        appendReceiptTotal(builder, receipt);
        appendPaymentInfo(builder, receipt);
        appendReceiptFooter(builder);
        return builder.toString();
    }

    private void appendReceiptHeader(StringBuilder builder) {
        appendLine(builder, "------------------- Begin receipt -------------------");
        appendLine(builder, "Time of Sale: " + getCurrentTime());
        endSection(builder);
    }

    private void appendSaleItems(StringBuilder builder, ReceiptDTO receipt) {
        for (var item : receipt.items()) {
            var itemInfo = item.item();
            int quantity = item.quantity();
            double price = itemInfo.price();
            Amount lineTotal = item.total();
            appendLine(builder, itemInfo.name() + " " + quantity + " x " + formatPrice(price) + " "
                    + formatAmount(lineTotal));
        }
        endSection(builder);
    }

    private void appendReceiptTotal(StringBuilder builder, ReceiptDTO receipt) {
        appendLine(builder, "Total: " + formatAmount(receipt.total()));
        appendLine(builder, "VAT: " + formatPrice(receipt.totalVat().asDouble()));
        endSection(builder);
    }

    private void appendPaymentInfo(StringBuilder builder, ReceiptDTO receipt) {
        appendLine(builder, "Cash: " + formatPrice(receipt.amountPaid().asDouble()) + " SEK");
        appendLine(builder, "Change: " + formatPrice(receipt.change().asDouble()) + " SEK");
    }

    private void appendReceiptFooter(StringBuilder builder) {
        appendLine(builder, "------------------- End receipt ---------------------");
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return now.format(formatter);
    }

    private String formatAmount(Amount amount) {
        if (amount == null)
            return "0:00 SEK";
        return formatPrice(amount.asDouble()) + " SEK";
    }

    private String formatPrice(double price) {
        return String.format("%.2f", price).replace('.', ':');
    }

    private void appendLine(StringBuilder builder, String line) {
        builder.append(line);
        builder.append("\n");
    }

    private void endSection(StringBuilder builder) {
        builder.append("\n");
    }
}
