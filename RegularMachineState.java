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
    // This call will now trigger ensureRegularInitialized() inside the model
    String vendingMachineData = createVendingMachine.displayRegVend(); 
    
    if (vendingMachineData.isEmpty()) {
        JOptionPane.showMessageDialog(view, "Regular vending machine data is empty.");
        return;
    }

    TransactionContext context = new TransactionContext(view, createVendingMachine);
    context.run();
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
                createVendingMachine.restockRegularItem(stockSelect, 0, numStock);
                stockSelect = Integer.parseInt(JOptionPane.showInputDialog(view, "Restocked " + numStock + " to slot " + numStock));
                break;

            case 2:
                int slotPrice;
                double stockPrice;
                slotPrice = Integer.parseInt(JOptionPane.showInputDialog(view, "Which slot do you want to set a price for?\n\nSlots 1 - 8: "));
                stockPrice = Double.parseDouble(JOptionPane.showInputDialog(view, "Set new price: "));
                createVendingMachine.setRegularItemPrice(slotPrice, stockPrice);
                break;

            case 3:
                Money totalEarnings = createVendingMachine.getRegularTotalEarnings();
                JOptionPane.showMessageDialog(view, "Total earnings: " + totalEarnings.getAmount() + " php");
                break;

            case 4:
                createVendingMachine.replenishRegularChange();
                JOptionPane.showMessageDialog(view, "Change replenished successfully.");
                break;

            case 5:
                createVendingMachine.displayRegularSummary();
                break;

            case 6:
                view.switchToFactoryPanel();
                break;

            default:
                JOptionPane.showMessageDialog(view, "Invalid choice!");
        }
    }
}

