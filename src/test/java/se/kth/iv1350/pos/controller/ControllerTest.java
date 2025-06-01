package se.kth.iv1350.pos.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import se.kth.iv1350.pos.integration.ConsolePrinter;
import se.kth.iv1350.pos.integration.ItemNotFoundException;
import se.kth.iv1350.pos.integration.RegistryCreator;
import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.SaleItemDTO;

/**
 * Unit tests for the {@link Controller} class. Covers sale start, item entry, invalid item,
 * multiple items, payment, and receipt printing.
 */
public class ControllerTest {
    private Controller controller;

    /**
     * Sets up a new Controller instance before each test.
     */
    @BeforeEach
    public void setUp() {
        controller = new Controller(RegistryCreator.getInstance(), new ConsolePrinter());
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        controller = null;
    }

    /**
     * Verifies that startSale initializes a new sale and enterItem adds an item.
     */
    @Test
    public void testStartSaleAndEnterItem() throws Exception {
        controller.startSale(); // Ensure a sale is started before entering item
        SaleItemDTO itemDTO = controller.enterItem("1");
        assertNotNull(itemDTO, "enterItem should return a SaleItemDTO for a valid item.");
        assertEquals("1", itemDTO.item().id(), "Item ID should match the entered ID.");
    }

    /**
     * Verifies that entering an invalid item ID returns null.
     */
    @Test
    public void testEnterInvalidItem() throws Exception {
        controller.startSale();
        try {
            controller.enterItem("invalid");
            Assertions.fail("Expected ItemNotFoundException");
        } catch (ItemNotFoundException e) {
            // expected
        }
    }

    /**
     * Verifies that adding multiple items and ending the sale works as expected.
     */
    @Test
    public void testAddMultipleItemsAndEndSale() throws Exception {
        controller.startSale();
        controller.enterItem("1");
        controller.enterItem("2");
        assertTrue(controller.endSale().asDouble() > 0,
                "Total should be greater than zero after adding items.");
    }

    /**
     * Verifies that payment and change calculation work as expected.
     */
    @Test
    public void testPaymentAndChange() throws Exception {
        controller.startSale();
        controller.enterItem("1");
        // Do not call endSale() before payment, as it resets the sale
        Amount payment = Amount.of(100);
        Amount change = controller.enterPayment(payment);
        assertNotNull(change, "Change should be returned after payment.");
        // The item with ID "1" has price 10 and VAT 0, so total is 10, change should be 90
        assertEquals(90.0, change.asDouble(), 0.001,
                "Change should be payment minus total (VAT-inclusive).");
    }

    /**
     * Verifies that an ItemNotFoundException is thrown for a missing item.
     */
    @Test
    public void testEnterItemThrowsItemNotFoundException() {
        controller.startSale();
        ItemNotFoundException thrown = Assertions.assertThrows(ItemNotFoundException.class, () -> {
            controller.enterItem("notfound");
        });
        assertNotNull(thrown, "ItemNotFoundException should be thrown when item is not found.");
    }

    /**
     * Verifies that an OperationFailedException is thrown for a simulated database failure.
     */
    @Test
    public void testEnterItemThrowsOperationFailedException() {
        controller.startSale();
        OperationFailedException thrown =
                Assertions.assertThrows(OperationFailedException.class, () -> {
                    controller.enterItem("dbfail");
                });
        assertNotNull(thrown, "OperationFailedException should be thrown when database fails.");
    }
}
