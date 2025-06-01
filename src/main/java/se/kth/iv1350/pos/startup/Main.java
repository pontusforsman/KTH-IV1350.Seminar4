package se.kth.iv1350.pos.startup;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.ConsolePrinter;
import se.kth.iv1350.pos.integration.Printer;
import se.kth.iv1350.pos.integration.RegistryCreator;
import se.kth.iv1350.pos.util.TotalRevenueFileOutput;
import se.kth.iv1350.pos.view.TotalRevenueView;
import se.kth.iv1350.pos.view.View;

/**
 * Entry point for the Point of Sale (POS) application. Initializes the system and starts the user
 * interface.
 */
public class Main {

    /**
     * Starts the application.
     *
     * @param args Command line arguments, not used in this application.
     */
    public static void main(String[] args) {
        RegistryCreator registryCreator = RegistryCreator.getInstance();
        Printer printer = new ConsolePrinter();
        Controller controller = new Controller(registryCreator, printer);

        controller.addRevenueObserver(new TotalRevenueView());
        controller.addRevenueObserver(new TotalRevenueFileOutput());

        new View(controller).sampleExecution();

    }
}
