public class SpecialTransactionContext {
    public Item selectedItem;
    public int selectedSlot;
    public boolean isFinished = false;
    public String vendingMachineData;
    private SpecialTransactionState currentState;

    public final VendingMachineFactoryView view;
    public final CreateVendingMachine createVM;

    public SpecialTransactionContext(VendingMachineFactoryView view, CreateVendingMachine createVM) {
        this.view = view;
        this.createVM = createVM;
        this.vendingMachineData = createVM.displaySpecVend();
        // Initial state: Choosing between Default vs Burger
        this.currentState = new FeatureChoiceState(); 
    }

    public void setState(SpecialTransactionState state) {
        this.currentState = state;
    }

    public void run() {
        while (!isFinished) {
            currentState.process(this);
        }
    }
}