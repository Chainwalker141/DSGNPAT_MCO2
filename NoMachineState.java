import javax.swing.JOptionPane;

public class NoMachineState implements MachineState {

    private final VendingMachineFactoryView view;

    public NoMachineState(VendingMachineFactoryView view) {
        this.view = view;
    }

    @Override
    public void handleTestFeatures() {
        JOptionPane.showMessageDialog(view, "Please create a vending machine first...");
    }

    @Override
    public void handleMaintenance() {
        JOptionPane.showMessageDialog(view, "Please create a vending machine first...");
    }
}

