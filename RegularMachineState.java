import javax.swing.JOptionPane;

public class RegularMachineState implements MachineState {

    private final VendingMachineFactoryView view;
    private final CreateVendingMachine createVendingMachine;

    public RegularMachineState(VendingMachineFactoryView view, CreateVendingMachine createVendingMachine) {
        this.view = view;
        this.createVendingMachine = createVendingMachine;
    }

    @Override
    public void handleTestFeatures() {
        String vendingMachineData = createVendingMachine.displayRegVend();
        if (!vendingMachineData.isEmpty()) {
            while (true) { // Infinite loop until the user enters -1
                // Gets an item from the regular vending machine
                Item item = createVendingMachine.regVendMachine.getSlots()[0][0];
                double currentChange = createVendingMachine.regVendMachine.currentChange();

                // Passes the item as an argument to the ItemDetailsDialog constructor
                new ItemDetailsDialog(item, currentChange, vendingMachineData);

                String slotInput = JOptionPane.showInputDialog(view, "Enter slot number (Slot 1 is 0, Slot 2 is 1, Slot 3 is 2, etc) or -1 to exit:");
                int slotNumber = Integer.parseInt(slotInput);

                if (slotNumber != -1) {
                    // Checks if the slot number is valid
                    if (slotNumber >= 0 && slotNumber < createVendingMachine.regVendMachine.getSlots().length) {
                        // Gets an item from the specified slot
                        item = createVendingMachine.regVendMachine.getSlots()[slotNumber][0];
                        currentChange = createVendingMachine.regVendMachine.currentChange();

                        // Passes the item as an argument to the ItemDetailsDialog constructor
                        ItemDetailsDialog itemDetailsDialog = new ItemDetailsDialog(item, currentChange, vendingMachineData);
                        itemDetailsDialog.setVisible(true);

                        // Shows a dialog box to get the payment from the user
                        String paymentInput = JOptionPane.showInputDialog(view, "Enter payment:");
                        double payment = Double.parseDouble(paymentInput);

                        // Processes the payment for the selected slotNumber
                        if (payment <= currentChange) {
                            createVendingMachine.regVendMachine.receivePayment(slotNumber, payment);

                            JOptionPane.showMessageDialog(view, "Dispensing " + item.getName() + "..." + " It contains " + item.getCalories() + " calories!");
                            JOptionPane.showMessageDialog(view, "Transaction Successful!");
                            JOptionPane.showMessageDialog(view, "You purchased:  " + item.getName());
                            JOptionPane.showMessageDialog(view, "Here is your change: " + (payment - item.getPriceForControl()));

                            double stock = createVendingMachine.regVendMachine.getStockQuantity(item);
                            if (stock > 0) {
                                // createVendingMachine.regVendMachine.setStockQuantity(item, stock);
                            } else {
                                JOptionPane.showMessageDialog(view, "Sorry, " + item.getName() + " is out of stock.");
                            }

                            vendingMachineData = createVendingMachine.displayRegVend();
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
    }

    @Override
    public void handleMaintenance() {
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
                createVendingMachine.regVendMachine.restockItem(stockSelect, 0, numStock);
                stockSelect = Integer.parseInt(JOptionPane.showInputDialog(view, "Restocked " + numStock + " to slot " + numStock));
                break;

            case 2:
                int slotPrice;
                double stockPrice;
                slotPrice = Integer.parseInt(JOptionPane.showInputDialog(view, "Which slot do you want to set a price for?\n\nSlots 1 - 8: "));
                stockPrice = Double.parseDouble(JOptionPane.showInputDialog(view, "Set new price: "));
                createVendingMachine.regVendMachine.setSlotItemPrice(slotPrice, stockPrice);
                break;

            case 3:
                Money totalEarnings = createVendingMachine.regVendMachine.getTotalEarnings();
                JOptionPane.showMessageDialog(view, "Total earnings: " + totalEarnings.getAmount() + " php");
                break;

            case 4:
                createVendingMachine.regVendMachine.replenishChange();
                JOptionPane.showMessageDialog(view, "Change replenished successfully.");
                break;

            case 5:
                createVendingMachine.regVendMachine.displaySummary(
                        createVendingMachine.regVendMachine.getSlots(),
                        createVendingMachine.regVendMachine.inventory,
                        createVendingMachine.regVendMachine.getSales()
                );
                break;

            case 6:
                view.switchToFactoryPanel();
                break;

            default:
                JOptionPane.showMessageDialog(view, "Invalid choice!");
        }
    }
}

