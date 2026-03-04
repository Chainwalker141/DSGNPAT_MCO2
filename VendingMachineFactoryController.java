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
    private boolean regularMachineCreated = false;
    private boolean specialMachineCreated = false;

    /**
     * Creates a new instance of the VendingMachineFactoryController with the specified VendingMachineFactoryView.
     * @param view The VendingMachineFactoryView to associate with the controller.
     */
    public VendingMachineFactoryController(VendingMachineFactoryView view) {
        this.view = view;
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
                    regularMachineCreated = true;
                } else if (createChoice == JOptionPane.NO_OPTION) {
                    int specVendingSlots = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter number of slots for the machine:"));
                    int specVendingItems = Integer.parseInt(JOptionPane.showInputDialog(view, "Enter maximum quantity of items per slot:"));

                    // Creates the special vending machine
                    newVendingMachine = new CreateVendingMachine(specVendingSlots, specVendingItems, "Special");

                    JOptionPane.showMessageDialog(view, "Creating special vending machine...");
                    JOptionPane.showMessageDialog(view, "Special vending machine created!");
                    specialMachineCreated = true;
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
                if (!regularMachineCreated && !specialMachineCreated) {
                    JOptionPane.showMessageDialog(view, "Please create a vending machine first...");
                    return;
                }

                int testingChoice = JOptionPane.showOptionDialog(
                        view,
                        "Choose the type of testing you want to perform:",
                        "Test Machine Features",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{"Regular Vending Machine", "Special Vending Machine"},
                        "Regular Vending Machine"
                );


                if (testingChoice == JOptionPane.YES_OPTION && regularMachineCreated) {
                    String vendingMachineData = newVendingMachine.displayRegVend();
                    if (!vendingMachineData.isEmpty()) {
                        while (true) { // Infinite loop until the user enters -1
                            // Gets an item from the regular vending machine
                            Item item = newVendingMachine.regVendMachine.getSlots()[0][0];
                            double currentChange = newVendingMachine.regVendMachine.currentChange();

                            // Passes the item as an argument to the ItemDetailsDialog constructor
                            new ItemDetailsDialog(item, currentChange, vendingMachineData);

                            String slotInput = JOptionPane.showInputDialog(view, "Enter slot number (Slot 1 is 0, Slot 2 is 1, Slot 3 is 2, etc) or -1 to exit:");
                            int slotNumber = Integer.parseInt(slotInput);

                            if (slotNumber != -1) {
                                // Checks if the slot number is valid
                                if (slotNumber >= 0 && slotNumber < newVendingMachine.regVendMachine.getSlots().length) {
                                    // Gets an item from the specified slot
                                    item = newVendingMachine.regVendMachine.getSlots()[slotNumber][0];
                                    currentChange = newVendingMachine.regVendMachine.currentChange();

                                    // Passes the item as an argument to the ItemDetailsDialog constructor
                                    ItemDetailsDialog itemDetailsDialog = new ItemDetailsDialog(item, currentChange, vendingMachineData);
                                    itemDetailsDialog.setVisible(true);

                                    // Shows a dialog box to get the payment from the user
                                    String paymentInput = JOptionPane.showInputDialog(view, "Enter payment:");
                                    double payment = Double.parseDouble(paymentInput);

                                    // Processes the payment for the selected slotNumber
                                    if(payment <= currentChange) {
                                        newVendingMachine.regVendMachine.receivePayment(slotNumber, payment);

                                        JOptionPane.showMessageDialog(view, "Dispensing " + item.getName() + "..." + " It contains " + item.getCalories() + " calories!");
                                        JOptionPane.showMessageDialog(view, "Transaction Successful!");
                                        JOptionPane.showMessageDialog(view, "You purchased:  " + item.getName());
                                        JOptionPane.showMessageDialog(view, "Here is your change: " + (payment - item.getPriceForControl()));

                                        double stock = newVendingMachine.regVendMachine.getStockQuantity(item);
                                        if(stock > 0) {
                                            //newVendingMachine.regVendMachine.setStockQuantity(item, stock);
                                        } else {
                                            JOptionPane.showMessageDialog(view, "Sorry, " + item.getName() + " is out of stock.");
                                        }

                                        vendingMachineData = newVendingMachine.displayRegVend();
                                        itemDetailsDialog.updateTextArea(vendingMachineData);


                                    } else {
                                        JOptionPane.showMessageDialog(view, "There is not enough change available for the transaction...");
                                    }

                                } else {
                                    JOptionPane.showMessageDialog(view, "Invalid slot number. Please enter a valid slot number.");
                                }
                            } else {
                                JOptionPane.showMessageDialog(view, "Good bye and have a nice day!");
                                break;
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(view, "Regular vending machine data is empty.");
                    }
                } else if (testingChoice == JOptionPane.NO_OPTION && specialMachineCreated) {
                    int customChoice = JOptionPane.showOptionDialog(
                            view,
                            "Choose the type of feature you want",
                            "Special Vending Machine Features",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new Object[]{"Default Vending Machine Features", "Custom product feature (Burger)"},
                            "Default Vending Machine features"
                    );

                    if (customChoice == JOptionPane.YES_OPTION && specialMachineCreated) {
                        String vendingMachineData = newVendingMachine.displaySpecVend();
                        if (!vendingMachineData.isEmpty()) {
                            while (true) {
                                Item item = newVendingMachine.specVendMachine.getSlots()[0][0];
                                double currentChange = newVendingMachine.specVendMachine.currentChange();

                                new ItemDetailsDialog(item, currentChange, vendingMachineData);

                                String slotInput = JOptionPane.showInputDialog(view, "Enter slot number (Slot 1 is 0, Slot 2 is 1, Slot 3 is 2, etc) or -1 to exit:");
                                int slotNumber = Integer.parseInt(slotInput);

                                if (slotNumber != -1) {
                                    if (slotNumber >= 0 && slotNumber < newVendingMachine.specVendMachine.getSlots().length) {
                                        item = newVendingMachine.specVendMachine.getSlots()[slotNumber][0];
                                        currentChange = newVendingMachine.specVendMachine.currentChange();

                                        ItemDetailsDialog itemDetailsDialog = new ItemDetailsDialog(item, currentChange, vendingMachineData);
                                        itemDetailsDialog.setVisible(true);

                                        String paymentInput = JOptionPane.showInputDialog(view, "Enter payment:");
                                        double payment = Double.parseDouble(paymentInput);

                                        if(payment <= currentChange) {
                                            newVendingMachine.specVendMachine.receivePayment(slotNumber, payment);

                                            JOptionPane.showMessageDialog(view, "Dispensing " + item.getName() + "..." + " It contains " + item.getCalories() + " calories!");
                                            JOptionPane.showMessageDialog(view, "Transaction Successful!");
                                            JOptionPane.showMessageDialog(view, "You purchased:  " + item.getName());
                                            JOptionPane.showMessageDialog(view, "Here is your change: " + (payment - item.getPriceForControl()));

                                            double stock = newVendingMachine.specVendMachine.getStockQuantity(item);
                                            if(stock > 0) {
                                                //newVendingMachine.specVendMachine.setStockQuantity(item, stock);
                                            } else {
                                                JOptionPane.showMessageDialog(view, "Sorry, " + item.getName() + " is out of stock.");
                                            }

                                            vendingMachineData = newVendingMachine.displaySpecVend();
                                            itemDetailsDialog.updateTextArea(vendingMachineData);


                                        } else {
                                            JOptionPane.showMessageDialog(view, "There is not enough change available for the transaction...");
                                        }

                                    } else {
                                        JOptionPane.showMessageDialog(view, "Invalid slot number. Please enter a valid slot number.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(view, "Good bye and have a nice day!");
                                    break;
                                }
                            }
                            JOptionPane.showMessageDialog(view, "Special vending machine data is empty.");
                        }
                    } else if (customChoice == JOptionPane.NO_OPTION && specialMachineCreated) {
                        // Test the custom product feature
                        newVendingMachine.specVendMachine.createCustomProduct(); // Call the method to handle the custom product feature
                    } else {
                        JOptionPane.showMessageDialog(view, "Please create the chosen vending machine first...");
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "Please create the chosen vending machine first...");
                }
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
                if ("Regular".equals(newVendingMachine.getVendingType())) {
                    // Regular Vending Machine Maintenance
                    String maintenanceMenu = "\nREGULAR VENDING MACHINE MAINTENANCE\n\n"
                            + "[1] Restock items\n"
                            + "[2] Set prices for an item\n"
                            + "[3] Collect payment\n"
                            + "[4] Replenish change\n"
                            + "[5] Summary of transactions\n"
                            + "[6] MENU\n\n"
                            + "Your choice: ";
                    int maintenanceChoice = Integer.parseInt(JOptionPane.showInputDialog(view, maintenanceMenu));

                    switch (maintenanceChoice) {
                        case 1:
                            int stockSelect, numStock;
                            stockSelect = Integer.parseInt(JOptionPane.showInputDialog(view, "Select a slot you want to restock: "));
                            numStock = Integer.parseInt(JOptionPane.showInputDialog(view, "Select number of items you want to restock: "));
                            newVendingMachine.regVendMachine.restockItem(stockSelect, 0, numStock);
                            stockSelect = Integer.parseInt(JOptionPane.showInputDialog(view, "Restocked " + numStock + " to slot " + numStock));
                            break;

                        case 2:
                            int slotPrice;
                            double stockPrice;
                            slotPrice = Integer.parseInt(JOptionPane.showInputDialog(view, "Which slot do you want to set a price for?\n\nSlots 1 - 8: "));
                            stockPrice = Double.parseDouble(JOptionPane.showInputDialog(view, "Set new price: "));
                            newVendingMachine.regVendMachine.setSlotItemPrice(slotPrice, stockPrice);
                            break;

                        case 3:
                            // method to collect all earnings made by machine so far
                            Money totalEarnings = newVendingMachine.regVendMachine.getTotalEarnings();
                            JOptionPane.showMessageDialog(view, "Total earnings: " + totalEarnings.getAmount() + " php");
                            break;

                        case 4:
                            // method to replenish the total change
                            newVendingMachine.regVendMachine.replenishChange();
                            JOptionPane.showMessageDialog(view, "Change replenished successfully.");
                            break;

                        case 5:
                            // method to display summary of transactions
                            newVendingMachine.regVendMachine.displaySummary(newVendingMachine.regVendMachine.getSlots(), newVendingMachine.regVendMachine.inventory, newVendingMachine.regVendMachine.getSales());
                            break;

                        case 6:
                            // "MENU"
                            view.switchToFactoryPanel();
                            break;

                        default:
                            JOptionPane.showMessageDialog(view, "Invalid choice!");
                    }
                } else if ("Special".equals(newVendingMachine.getVendingType())) {
                    // Special Vending Machine Maintenance
                    String maintenanceMenu = "\nSPECIAL VENDING MACHINE MAINTENANCE\n\n"
                            + "[1] Restock items\n"
                            + "[2] Set prices for an item\n"
                            + "[3] Collect payment\n"
                            + "[4] Replenish change\n"
                            + "[5] Summary of transactions\n"
                            + "[6] MENU\n\n"
                            + "Your choice: ";
                    int maintenanceChoice = Integer.parseInt(JOptionPane.showInputDialog(view, maintenanceMenu));

                    switch (maintenanceChoice) {
                        case 1:
                            int stockSelect, numStock;
                            stockSelect = Integer.parseInt(JOptionPane.showInputDialog(view, "Select a slot you want to restock: "));
                            numStock = Integer.parseInt(JOptionPane.showInputDialog(view, "Select number of items you want to restock: "));
                            newVendingMachine.specVendMachine.restockItem(stockSelect, 0, numStock);
                            break;

                        case 2:
                            int slotPrice;
                            double stockPrice;
                            slotPrice = Integer.parseInt(JOptionPane.showInputDialog(view, "Which slot do you want to set a price for?\n\nSlots 1 - 8: "));
                            stockPrice = Double.parseDouble(JOptionPane.showInputDialog(view, "Set new price: "));
                            newVendingMachine.specVendMachine.setSlotItemPrice(slotPrice, stockPrice);
                            break;

                        case 3:
                            Money totalEarnings = newVendingMachine.specVendMachine.getTotalEarnings();
                            JOptionPane.showMessageDialog(view, "Total earnings: " + totalEarnings.getAmount() + " php");
                            break;

                        case 4:
                            newVendingMachine.specVendMachine.replenishChange();
                            JOptionPane.showMessageDialog(view, "Change replenished successfully.");
                            break;

                        case 5:
                            newVendingMachine.specVendMachine.displaySummary(newVendingMachine.specVendMachine.getSlots(), newVendingMachine.specVendMachine.inventory, newVendingMachine.specVendMachine.getSales());
                            break;

                        case 6:
                            view.switchToFactoryPanel();
                            break;

                        default:
                            JOptionPane.showMessageDialog(view, "Invalid choice!");
                    }
                }
            }
        });
    }
}
