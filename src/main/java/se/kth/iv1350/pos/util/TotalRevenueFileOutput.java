package se.kth.iv1350.pos.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import se.kth.iv1350.pos.model.RevenueObserver;
import se.kth.iv1350.pos.model.SaleDTO;

/**
 * This class implements the {@link RevenueObserver} interface to log total revenue to a file. It
 * appends the total revenue information to a file named "revenue.log".
 */
public class TotalRevenueFileOutput implements RevenueObserver {
    private static final String FILE_NAME = "revenue.log";
    private static double totalRevenue = 0.0;

    @Override
    public void saleCompleted(SaleDTO sale) {
        calculateTotalRevenue(sale);
        logTotalRevenue();
    }

    /**
     * Calculates and updates the running total revenue after a sale.
     * 
     * @param sale The completed sale.
     */
    private void calculateTotalRevenue(SaleDTO sale) {
        totalRevenue += sale.total().asDouble();
    }

    /**
     * Logs the current running total revenue to the revenue.log file.
     */
    private void logTotalRevenue() {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME, true))) {
            out.println(String.format("Total revenue: %.2f SEK", totalRevenue));
        } catch (IOException e) {
            System.err.println("Could not log total revenue to file.");
        }
    }
}
