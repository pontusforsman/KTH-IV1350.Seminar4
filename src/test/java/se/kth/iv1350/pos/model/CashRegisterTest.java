package se.kth.iv1350.pos.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link CashRegister} class.
 */
public class CashRegisterTest {
    private CashRegister cashRegister;

    /**
     * Sets up a new CashRegister instance before each test.
     */
    @BeforeEach
    public void setUp() {
        cashRegister = new CashRegister();
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        cashRegister = null;
    }

    /**
     * Tests that the balance is correctly updated when adding an amount.
     */
    @Test
    public void testUpdateBalance() {
        Amount initialBalance = cashRegister.getBalance();
        Amount amountToAdd = Amount.of(200);
        cashRegister.updateBalance(amountToAdd);
        Assertions.assertNotNull(amountToAdd);
        Amount expectedBalance = initialBalance.add(amountToAdd);
        Amount actualBalance = cashRegister.getBalance();
        assertEquals(expectedBalance.asDouble(), actualBalance.asDouble(),
                "Balance was not correctly updated after updateBalance().");
    }
}
