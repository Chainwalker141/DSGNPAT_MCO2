import javax.swing.JOptionPane;

public class SpecPaymentState implements SpecialTransactionState {
    @Override
    public void process(SpecialTransactionContext ctx) {
        double currentChange = ctx.createVM.getSpecialCurrentChange(); // Special method 
        String paymentInput = JOptionPane.showInputDialog(ctx.view, "Enter payment:");
        
        if (paymentInput == null) {
            ctx.setState(new SpecSlotSelectionState()); // Go back to selection
            return;
        }

        try {
            double payment = Double.parseDouble(paymentInput);

            if (payment <= currentChange) {
                // Call the special payment method 
                ctx.createVM.receiveSpecialPayment(ctx.selectedSlot, payment); 

                JOptionPane.showMessageDialog(ctx.view, 
                    "Dispensing " + ctx.selectedItem.getName() + "..." + 
                    " It contains " + ctx.selectedItem.getCalories() + " calories!");
                JOptionPane.showMessageDialog(ctx.view, "Transaction Successful!");

                // Refresh the data for the ItemDetailsDialog update 
                ctx.vendingMachineData = ctx.createVM.displaySpecVend();
                
                // Return to selection for next purchase
                ctx.setState(new SpecSlotSelectionState()); 
            } else {
                JOptionPane.showMessageDialog(ctx.view, "There is not enough change available...");
                ctx.setState(new SpecSlotSelectionState());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(ctx.view, "Invalid payment amount.");
        }
    }
}