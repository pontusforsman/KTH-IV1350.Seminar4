package se.kth.iv1350.pos.integration;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link InventoryRegistry} class.
 */
public class InventoryRegistryTest {
    private InventoryRegistry inventoryRegistry;

    /**
     * Sets up a new InventoryRegistry instance before each test.
     */
    @BeforeEach
    public void setUp() {
        inventoryRegistry = InventoryRegistry.getInstance();
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        inventoryRegistry = null;
    }

    /**
     * Tests that findItemById returns the correct item.
     */
    @Test
    public void testFindItemById() throws Exception {
        ItemDTO item = inventoryRegistry.findItemById("1");
        assertNotNull(item, "Item should be found in inventory.");
        assertEquals("1", item.id(), "Item ID should match.");
        assertEquals("Medicine", item.name(), "Item name should match.");
    }

    /**
     * Tests that findItemById throws ItemNotFoundException for an invalid item ID.
     */
    @Test
    public void testFindItemByIdThrowsItemNotFoundException() {
        // The result of assertThrows must be used or suppressed
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class, () -> {
            inventoryRegistry.findItemById("notfound");
        }, "Should throw ItemNotFoundException for invalid item ID");
        assertNotNull(thrown);
    }

    /**
     * Tests that findItemById throws InventoryDatabaseException for a specific case.
     */
    @Test
    public void testFindItemByIdThrowsInventoryDatabaseException() {
        DabaseFailureException thrown = assertThrows(DabaseFailureException.class, () -> {
            inventoryRegistry.findItemById("dbfail");
        }, "Should throw InventoryDatabaseException for 'dbfail' item ID");
        assertNotNull(thrown);
    }
}
