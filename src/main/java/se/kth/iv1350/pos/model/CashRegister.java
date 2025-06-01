package se.kth.iv1350.pos.model;

import se.kth.iv1350.pos.controller.Controller;

/**
 * Represents a <code>CashRegister</code> that handles the store's cash. Updated by
 * {@link Controller} during payment.
 */
public class CashRegister {
    private static final double INITIAL_BALANCE = 10000;
    private Amount balance;

    /**
     * Creates a new instance with the initial balance.s
     */
    public CashRegister() {
        this.balance = Amount.of(INITIAL_BALANCE);
    }

    /**
     * Gets the current balance in the cash register.
     *
     * @return The current balance.
     */
    public Amount getBalance() {
        return balance;
    }

    /**
     * Updates the balance by adding the specified amount.
     *
     * @param amount The amount to add to the balance.
     */
    void updateBalance(Amount amount) {
        balance = balance.add(amount);
    }
}
