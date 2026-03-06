import javax.swing.JOptionPane;

public class ProcessingPaymentState implements TransactionState {
    @Override
    public void process(TransactionContext ctx) {
        double currentChange = ctx.createVM.regVendMachine.currentChange();
        String input = JOptionPane.showInputDialog(ctx.view, "Enter payment:");
        double payment = Double.parseDouble(input);

        if (payment <= currentChange) {
            ctx.createVM.regVendMachine.receivePayment(ctx.selectedSlot, payment);
            JOptionPane.showMessageDialog(ctx.view, "Dispensing " + ctx.selectedItem.getName());
            // Transition back to selection for the next purchase
            ctx.setState(new SelectingSlotState()); 
        } else {
            JOptionPane.showMessageDialog(ctx.view, "Not enough change available!");
            ctx.setState(new SelectingSlotState());
        }
    }
}