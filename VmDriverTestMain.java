/**
 * The main class that serves as the entry point for the Vending Machine Factory application.
 * This class contains the main method that initializes the GUI components and starts the application.
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

import javax.swing.*;

public class VmDriverTestMain {
    public static void main(String[] args) {
        // Run the GUI code on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // Creates the view (GUI frame)
                VendingMachineFactoryView view = new VendingMachineFactoryView();

                // Creates the controller and pass the view to it
                VendingMachineFactoryController controller = new VendingMachineFactoryController(view);


                // Makes the GUI visible
                view.setVisible(true);
            }
        });
    }
}

