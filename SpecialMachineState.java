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
    // Simply launch the state-driven context
    new SpecialTransactionContext(view, createVendingMachine).run();
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

