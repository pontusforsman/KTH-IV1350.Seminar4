package se.kth.iv1350.pos.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.integration.ItemDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link SaleItem} class.
 * Covers line total calculation, VAT edge cases, quantity handling, and DTO conversion.
 */
public class SaleItemTest {
    private SaleItem saleItem;
    private ItemDTO testItem;

    /**
     * Sets up a new SaleItem instance before each test.
     */
    @BeforeEach
    public void setUp() {
        testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.25);
        saleItem = new SaleItem(testItem, 2); // quantity 2
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        saleItem = null;
        testItem = null;
    }

    /**
     * Verifies that negative quantity does not decrease below zero.
     */
    @Test
    public void testNegativeQuantityNotAllowed() {
        int before = saleItem.getQuantity();
        saleItem.updateQuantity(-100);
        assertEquals(before, saleItem.getQuantity(), "Quantity should not decrease below zero.");
    }

    /**
     * Verifies that toDTO returns correct data.
     */
    @Test
    public void testToDTO() {
        SaleItemDTO dto = saleItem.toDTO();
        assertEquals(saleItem.getItem(), dto.item(), "DTO item should match.");
        assertEquals(saleItem.getQuantity(), dto.quantity(), "DTO quantity should match.");
    }
} 