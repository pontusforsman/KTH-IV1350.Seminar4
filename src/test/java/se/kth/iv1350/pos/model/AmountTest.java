package se.kth.iv1350.pos.model;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Amount} class. Covers arithmetic operations, zero/negative/large
 * values, and multiply logic.
 */
public class AmountTest {
    private Amount hundredAmount;
    private Amount emptyAmount;

    /**
     * Sets up a new Amount instance before each test.
     */
    @BeforeEach
    public void setUp() {
        hundredAmount = Amount.of(100);
        emptyAmount = Amount.of(0);

    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        hundredAmount = null;
        emptyAmount = null;
    }

    /**
     * Verifies that add and subtract methods work correctly.
     */
    @Test
    public void testAdd() {
        Amount toAdd = Amount.of(50);
        assertNotNull(toAdd);
        Amount result = hundredAmount.add(toAdd);
        assertEquals(150, result.asDouble(), "Add should work correctly.");
    }

    @Test
    public void testAddEmpty() {
        Amount toAdd = Amount.of(10);
        assertNotNull(toAdd);
        Amount result = emptyAmount.add(toAdd);
        assertEquals(10, result.asDouble(), "Adding 10 to zero should return 10.");
    }

    @Test
    public void testSubtract() {
        Amount toSubtract = Amount.of(30);
        assertNotNull(toSubtract);
        Amount result = hundredAmount.subtract(toSubtract);
        assertEquals(70, result.asDouble(), "Subtract should work correctly.");
    }

    /**
     * Verifies that zero amounts are handled correctly.
     */
    @Test
    public void testZeroAmount() {
        assertEquals(0, emptyAmount.asDouble(), "Zero amount should have 0 as value.");
    }

    /**
     * Verifies that the multiply method works as expected.
     */
    @Test
    public void testMultiply() {
        double toMultiply = 2;
        Amount result = hundredAmount.multiply(toMultiply);
        assertEquals(200, result.asDouble(), "Multiply should work correctly.");
    }

    @Test
    public void testNegativeAmountThrows() {


        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            Amount.of(-50);
        }, "Creating an Amount with a negative value should throw IllegalArgumentException.");
        assertNotNull(thrown, "Exception should not be null.");
    }

    @Test
    public void testIsZeroPositiveNegative() {
        assertTrue(emptyAmount.isZero(), "isZero should be true for zero");
        assertTrue(hundredAmount.isPositive(), "isPositive should be true for positive");
        assertTrue(!hundredAmount.isNegative(), "isNegative should be false for positive");
    }
}
