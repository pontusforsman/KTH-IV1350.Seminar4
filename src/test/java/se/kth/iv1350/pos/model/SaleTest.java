package se.kth.iv1350.pos.model;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pos.integration.ItemDTO;

/**
 * Unit tests for the {@link Sale} class. Covers adding items, handling quantities, and sale
 * completion edge cases.
 */
public class SaleTest {
    private Sale sale;

    /**
     * Sets up a new Sale instance before each test.
     */
    @BeforeEach
    public void setUp() {
        sale = new Sale(new CashRegister());
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        sale = null;
    }

    /**
     * Verifies that adding an item updates the total cost and item list.
     */
    @Test
    public void testAddItemUpdatesTotal() {
        ItemDTO testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.0);
        sale.addItem(testItem);
        assertEquals(100.0, sale.getTotalCost().asDouble(), 0.001,
                "Total cost should be updated after adding item.");
        assertTrue(sale.getItems().containsKey("test1"),
                "Item should be present in the sale after adding.");
    }

    /**
     * Verifies that adding the same item twice increases its quantity and updates the total.
     */
    @Test
    public void testAddSameItemTwiceIncreasesQuantity() {
        ItemDTO testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.0);
        sale.addItem(testItem);
        sale.addItem(testItem);
        assertEquals(200.0, sale.getTotalCost().asDouble(), 0.001,
                "Total should double after adding the same item twice.");
        assertEquals(2, sale.getItems().get("test1").getQuantity(),
                "Quantity should be 2 after adding the same item twice.");
    }

    /**
     * Verifies that adding multiple different items sums their totals with VAT.
     */
    @Test
    public void testAddMultipleDifferentItems() {
        ItemDTO item1 = new ItemDTO("item1", "Item 1", "Desc 1", 50.0, 0.12);
        ItemDTO item2 = new ItemDTO("item2", "Item 2", "Desc 2", 30.0, 0.25);
        sale.addItem(item1);
        sale.addItem(item2);
        double expected = 50.0 * 1.12 + 30.0 * 1.25; // 50 + 6 (12% VAT) + 30 + 7.5 (25% VAT)
        assertEquals(expected, sale.getTotalCost().asDouble(), 0.001,
                "Total should sum both items with VAT.");
    }

    /**
     * Verifies that adding zero quantity does not change the total.
     */
    @Test
    public void testAddZeroQuantity() {
        ItemDTO testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.0);
        sale.addItem(testItem);
        sale.setLastItemQuantity(0);
        assertEquals(0, sale.getTotalCost().asDouble(), 0.001,
                "Total should not change after adding zero quantity.");
    }

    /**
     * Verifies that adding a negative quantity does not change the total.
     */
    @Test
    public void testAddNegativeQuantity() {
        ItemDTO testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.0);
        sale.addItem(testItem);
        try {
            sale.setLastItemQuantity(-2); // Should throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertEquals(100.0, sale.getTotalCost().asDouble(), 0.001,
                "Total should not change after negative quantity.");
    }

    /**
     * Verifies that ending a sale with no items results in zero total.
     */
    @Test
    public void testEndSaleWithNoItems() {
        assertEquals(0.0, sale.completeSale().asDouble(), 0.001,
                "Total should be zero if no items were added.");
    }

    @Test
    public void testAddItem_shouldAddNewItem() {
        Sale anotherSale = new Sale(new CashRegister());
        ItemDTO testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.0);
        anotherSale.addItem(testItem);
        assertEquals(100.0, anotherSale.getTotalCost().asDouble(), 0.001,
                "Total cost should be updated after adding item.");
        assertTrue(anotherSale.getItems().containsKey("test1"),
                "Item should be present in the sale after adding.");
    }

    @Test
    public void updateQuantity() {
        Sale anotherSale = new Sale(new CashRegister());
        ItemDTO testItem = new ItemDTO("test1", "Test Item", "Test Description", 100.0, 0.0);
        anotherSale.addItem(testItem);
        anotherSale.setLastItemQuantity(0);
        assertEquals(0, anotherSale.getTotalCost().asDouble(), 0.001,
                "Total should not change after adding zero quantity.");
    }
}
