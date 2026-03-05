import javax.swing.JOptionPane;

public class SpecialMachineState implements MachineState {

    private final VendingMachineFactoryView view;
    private final CreateVendingMachine createVendingMachine;

    public SpecialMachineState(VendingMachineFactoryView view, CreateVendingMachine createVendingMachine) {
        this.view = view;
        this.createVendingMachine = createVendingMachine;
    }

    @Override
    public void handleTestFeatures() {
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

        if (customChoice == JOptionPane.YES_OPTION) {
            String vendingMachineData = createVendingMachine.displaySpecVend();
            if (!vendingMachineData.isEmpty()) {
                while (true) {
                    Item item = createVendingMachine.specVendMachine.getSlots()[0][0];
                    double currentChange = createVendingMachine.specVendMachine.currentChange();

                    new ItemDetailsDialog(item, currentChange, vendingMachineData);

                    String slotInput = JOptionPane.showInputDialog(view, "Enter slot number (Slot 1 is 0, Slot 2 is 1, Slot 3 is 2, etc) or -1 to exit:");
                    int slotNumber = Integer.parseInt(slotInput);

                    if (slotNumber != -1) {
                        if (slotNumber >= 0 && slotNumber < createVendingMachine.specVendMachine.getSlots().length) {
                            item = createVendingMachine.specVendMachine.getSlots()[slotNumber][0];
                            currentChange = createVendingMachine.specVendMachine.currentChange();

                            ItemDetailsDialog itemDetailsDialog = new ItemDetailsDialog(item, currentChange, vendingMachineData);
                            itemDetailsDialog.setVisible(true);

                            String paymentInput = JOptionPane.showInputDialog(view, "Enter payment:");
                            double payment = Double.parseDouble(paymentInput);

                            if (payment <= currentChange) {
                                createVendingMachine.specVendMachine.receivePayment(slotNumber, payment);

                                JOptionPane.showMessageDialog(view, "Dispensing " + item.getName() + "..." + " It contains " + item.getCalories() + " calories!");
                                JOptionPane.showMessageDialog(view, "Transaction Successful!");
                                JOptionPane.showMessageDialog(view, "You purchased:  " + item.getName());
                                JOptionPane.showMessageDialog(view, "Here is your change: " + (payment - item.getPriceForControl()));

                                double stock = createVendingMachine.specVendMachine.getStockQuantity(item);
                                if (stock > 0) {
                                    // createVendingMachine.specVendMachine.setStockQuantity(item, stock);
                                } else {
                                    JOptionPane.showMessageDialog(view, "Sorry, " + item.getName() + " is out of stock.");
                                }

                                vendingMachineData = createVendingMachine.displaySpecVend();
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
                JOptionPane.showMessageDialog(view, "Special vending machine data is empty.");
            }
        } else if (customChoice == JOptionPane.NO_OPTION) {
            // Test the custom product feature
            createVendingMachine.specVendMachine.createCustomProduct();
        } else {
            JOptionPane.showMessageDialog(view, "Please create the chosen vending machine first...");
        }
    }

    @Override
    public void handleMaintenance() {
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
                createVendingMachine.specVendMachine.restockItem(stockSelect, 0, numStock);
                break;

            case 2:
                int slotPrice;
                double stockPrice;
                slotPrice = Integer.parseInt(JOptionPane.showInputDialog(view, "Which slot do you want to set a price for?\n\nSlots 1 - 8: "));
                stockPrice = Double.parseDouble(JOptionPane.showInputDialog(view, "Set new price: "));
                createVendingMachine.specVendMachine.setSlotItemPrice(slotPrice, stockPrice);
                break;

            case 3:
                Money totalEarnings = createVendingMachine.specVendMachine.getTotalEarnings();
                JOptionPane.showMessageDialog(view, "Total earnings: " + totalEarnings.getAmount() + " php");
                break;

            case 4:
                createVendingMachine.specVendMachine.replenishChange();
                JOptionPane.showMessageDialog(view, "Change replenished successfully.");
                break;

            case 5:
                createVendingMachine.specVendMachine.displaySummary(
                        createVendingMachine.specVendMachine.getSlots(),
                        createVendingMachine.specVendMachine.inventory,
                        createVendingMachine.specVendMachine.getSales()
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

