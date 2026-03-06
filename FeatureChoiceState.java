import javax.swing.JOptionPane;

public class FeatureChoiceState implements SpecialTransactionState {
    @Override
    public void process(SpecialTransactionContext ctx) {
        int choice = JOptionPane.showOptionDialog(
                ctx.view, "Choose the type of feature", "Special Features",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{"Default Features", "Burger Feature"}, "Default Features"
        );

        if (choice == JOptionPane.YES_OPTION) {
            if (ctx.vendingMachineData.isEmpty()) {
                JOptionPane.showMessageDialog(ctx.view, "Data is empty.");
                ctx.isFinished = true;
            } else {
                ctx.setState(new SpecSlotSelectionState()); // Go to transaction loop
            }
        } else if (choice == JOptionPane.NO_OPTION) {
            ctx.setState(new CustomProductState()); // Go to Burger feature
        } else {
            ctx.isFinished = true;
        }
    }
}