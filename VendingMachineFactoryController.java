/**
 * The VendingMachineFactoryController class acts as a controller for the Vending Machine Factory application.
 * It handles user interactions with the GUI and manages the creation and testing of vending machines.
 *
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class VendingMachineFactoryController {

    private VendingMachineFactoryView view;
    CreateVendingMachine newVendingMachine;
    private MachineState machineState;

    /**
     * Creates a new instance of the VendingMachineFactoryController with the specified VendingMachineFactoryView.
     * @param view The VendingMachineFactoryView to associate with the controller.
     */
    public VendingMachineFactoryController(VendingMachineFactoryView view) {
        this.view = view;
        this.machineState = new NoMachineState(view);
        initFactoryPanel();
        initTestingPanel();
    }

    /**
     * Initializes the factory panel functionality in the associated VendingMachineFactoryView.
     * Sets up action listeners for the "Create a Vending Machine" button, "Test Vending Machine" button, and "EXIT" button.
     */
    private void initFactoryPanel() {
        view.setCreateButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // called when Create a Vending Machine button is clicked.
                int createChoice = JOptionPane.showOptionDialog(

                        view,
                        "Choose the type of vending machine you want to create:",
                        "Create Vending Machine",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Regular", "Special"},
                        "Regular"
                );

                // Based on user choice, shows a dialog to get more input for the vending machine specs.
                if (createChoice == JOptionPane.YES_OPTION) {
                    int regVendingSlots = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter number of slots for the machine:"));
                    int regVendingItems = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter maximum quantity of items per slot:"));

                    // Creates the regular vending machine
                    newVendingMachine = new CreateVendingMachine(regVendingSlots, regVendingItems, view);

                    JOptionPane.showMessageDialog(view, "Creating regular vending machine...");
                    JOptionPane.showMessageDialog(view, "Regular vending machine created!");
                    machineState = new RegularMachineState(view, newVendingMachine);
                } else if (createChoice == JOptionPane.NO_OPTION) {
                    int specVendingSlots = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter number of slots for the machine:"));
                    int specVendingItems = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter maximum quantity of items per slot:"));

                    // Creates the special vending machine
                    newVendingMachine = new CreateVendingMachine(specVendingSlots, specVendingItems, "Special");

                    JOptionPane.showMessageDialog(view, "Creating special vending machine...");
                    JOptionPane.showMessageDialog(view, "Special vending machine created!");
                    machineState = new SpecialMachineState(view, newVendingMachine);
                }
            }
        });

        view.setTestButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to the testing panel
                view.switchToTestingPanel();
            }
        });

        view.setExitButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exit the program
                System.exit(0);
            }
        });
    }

    /**
     * Initializes the testing panel functionality in the associated VendingMachineFactoryView.
     */
    private void initTestingPanel() {
        view.setTestFeaturesButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                machineState.handleTestFeatures();
            }
        });


        view.setExitTestingButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch back to the factory panel
                view.switchToFactoryPanel();
            }
        });

        view.setMaintenanceButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                machineState.handleMaintenance();
            }
        });
    }
}
