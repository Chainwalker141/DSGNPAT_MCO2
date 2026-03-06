import javax.swing.JOptionPane;

public class SpecSlotSelectionState implements SpecialTransactionState {
    @Override
    public void process(SpecialTransactionContext ctx) {
        String input = JOptionPane.showInputDialog(ctx.view, "Enter slot number or -1 to exit:");
        int slot = Integer.parseInt(input);

        if (slot == -1) {
            ctx.isFinished = true;
        } else if (slot >= 0 && slot < ctx.createVM.getSpecialSlotCount()) {
            ctx.selectedSlot = slot;
            ctx.selectedItem = ctx.createVM.getSpecialItem(slot);
            
            ItemDetailsDialog dialog = new ItemDetailsDialog(ctx.selectedItem, ctx.createVM.getSpecialCurrentChange(), ctx.vendingMachineData);
            dialog.setVisible(true);
            
            ctx.setState(new SpecPaymentState()); // Move to payment
        } else {
            JOptionPane.showMessageDialog(ctx.view, "Invalid slot number.");
        }
    }
}