import javax.swing.JOptionPane;

public class SelectingSlotState implements TransactionState {
    @Override
    public void process(TransactionContext ctx) {
        String input = JOptionPane.showInputDialog(ctx.view, "Enter slot (or -1 to exit):");
        int slot = Integer.parseInt(input);

        if (slot == -1) {
            JOptionPane.showMessageDialog(ctx.view, "Goodbye!");
            ctx.isFinished = true;
        } else if (slot >= 0 && slot < ctx.createVM.regVendMachine.getSlots().length) {
            ctx.selectedSlot = slot;
            ctx.selectedItem = ctx.createVM.regVendMachine.getSlots()[slot][0];
            ctx.setState(new ProcessingPaymentState()); // Transition
        } else {
            JOptionPane.showMessageDialog(ctx.view, "Invalid slot!");
        }
    }
}
