package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleDTO;

/**
 * Simulates an external <code>AccountingRegistry</code> system. Receives sale data as
 * {@link se.kth.iv1350.pos.model.SaleDTO} from the controller.
 */
public class AccountingRegistry {
    private static AccountingRegistry instance;

    /**
     * Returns the singleton instance of <code>AccountingRegistry</code>. If it does not exist, it
     * is created.
     *
     * @return The singleton <code>AccountingRegistry</code> instance.
     */
    static AccountingRegistry getInstance() {
        if (instance == null) {
            instance = new AccountingRegistry();
        }
        return instance;
    }

    private AccountingRegistry() {}

    /**
     * Updates the <code>AccountingRegistry</code> system with the completed sale data. This method
     * simulates updating an external accounting system by printing a message to the console.
     *
     * @param saleDTO The {@link SaleDTO} containing sale information.
     */
    public void updateAccounting(SaleDTO saleDTO) {
        System.out.printf("[%s]: Accounting updated%n", this.getClass().getSimpleName());
    }
}
