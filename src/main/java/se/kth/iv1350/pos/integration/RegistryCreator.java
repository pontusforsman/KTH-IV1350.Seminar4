package se.kth.iv1350.pos.integration;

/**
 * Creates and provides access to external system registries such as {@link InventoryRegistry} and
 * {@link AccountingRegistry}. Used by the {@link Controller} to access integration layer systems.
 */
public class RegistryCreator {
    private static RegistryCreator instance;
    private final InventoryRegistry inventoryRegistry;
    private final AccountingRegistry accountingRegistry;

    /**
     * Returns the singleton instance of <code>RegistryCreator</code>. If it does not exist, it is
     * created.
     *
     * @return The singleton <code>RegistryCreator</code> instance.
     */
    public static RegistryCreator getInstance() {
        if (instance == null) {
            instance = new RegistryCreator();
        }
        return instance;
    }

    /**
     * Creates a new instance of <code>RegistryCreator</code>. Initializes the
     * {@link InventoryRegistry} and {@link AccountingRegistry}. Private to enforce singleton
     * pattern.
     */
    private RegistryCreator() {
        inventoryRegistry = InventoryRegistry.getInstance();
        accountingRegistry = AccountingRegistry.getInstance();
    }

    /**
     * s Returns the {@link InventoryRegistry} instance.
     *
     * @return The <code>InventoryRegistry</code> instance.
     */
    public InventoryRegistry getInventoryRegistry() {
        return inventoryRegistry;
    }

    /**
     * Returns the {@link AccountingRegistry} instance.
     *
     * @return The <code>AccountingRegistry</zcode> instance.
     */
    public AccountingRegistry getAccountingRegistry() {
        return accountingRegistry;
    }
}
