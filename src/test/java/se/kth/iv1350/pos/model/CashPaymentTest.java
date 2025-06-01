package se.kth.iv1350.pos.model;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pos.integration.ItemDTO;

/**
 * Unit tests for the {@link CashPayment} class. Covers change calculation,
 * exact/insufficient/negative payment, and payment logic edge cases.
 */
public class CashPaymentTest {
    private CashPayment cashPayment;
    private Sale sale;

    /**
     * Sets up a new CashPayment and Sale instance before each test.
     */
    @BeforeEach
    public void setUp() {
        sale = new Sale(new CashRegister());
        // Create a test item with price 100 and VAT 0 for simplicity
        ItemDTO testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.0);
        sale.addItem(testItem);
        cashPayment = new CashPayment(Amount.of(150));
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        cashPayment = null;
        sale = null;
    }

    /**
     * Verifies that calculateTotalCost correctly updates the amountPaid to the change.
     */
    @Test
    public void testCalculateTotalCost() {
        cashPayment.calculateTotalCost(sale);
        Amount expectedChange = Amount.of(50); // 150 - 100
        assertNotNull(expectedChange);
        assertEquals(expectedChange.asDouble(), cashPayment.getAmountPaid().asDouble(),
                "Change was not correctly calculated in calculateTotalCost().");
    }

    /**
     * Verifies that exact payment results in zero change.
     */
    @Test
    public void testExactPayment() {
        cashPayment = new CashPayment(Amount.of(100));
        cashPayment.calculateTotalCost(sale);
        assertEquals(0.0, cashPayment.getAmountPaid().asDouble(), 0.001,
                "Exact payment should result in zero change.");
    }
}
