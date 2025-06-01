package se.kth.iv1350.pos.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for the {@link AccountingRegistry} class.
 */
public class AccountingRegistryTest {
    private AccountingRegistry accountingRegistry;

    /**
     * Sets up a new AccountingRegistry instance before each test.
     */
    @BeforeEach
    public void setUp() {
        accountingRegistry = AccountingRegistry.getInstance();
    }

    /**
     * Cleans up after each test.
     */
    @AfterEach
    public void tearDown() {
        accountingRegistry = null;
    }

    /**
     * Tests that the AccountingRegistry object is created.
     */
    @Test
    public void testConstructor() {
        assertNotNull(accountingRegistry, "AccountingRegistry should be created.");
    }
}