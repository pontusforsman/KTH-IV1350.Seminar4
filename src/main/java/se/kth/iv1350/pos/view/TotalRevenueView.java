package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.model.RevenueObserver;
import se.kth.iv1350.pos.model.SaleDTO;

public class TotalRevenueView implements RevenueObserver {
    private static double totalRevenue = 0.0;

    @Override
    public void saleCompleted(SaleDTO sale) {
        calculateTotalRevenue(sale);
        showTotalRevenue();
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
     * Displays the current running total revenue to the console.
     */
    private void showTotalRevenue() {
        System.out.println("-------New total revenue received--------");
        System.out.printf("Total revenue: %.2f SEK%n", totalRevenue);
        System.out.println("------------------------------------------\n");
    }
}
