public class TransactionContext {
    public Item selectedItem;
    public int selectedSlot;
    public boolean isFinished = false;
    private TransactionState currentState;
    
    // Dependencies from your RegularMachineState
    public final VendingMachineFactoryView view;
    public final CreateVendingMachine createVM;

    public TransactionContext(VendingMachineFactoryView view, CreateVendingMachine createVM) {
        this.view = view;
        this.createVM = createVM;
        this.currentState = new SelectingSlotState(); // Initial state
    }

    public void setState(TransactionState state) {
        this.currentState = state;
    }

    public void run() {
        while (!isFinished) {
            currentState.process(this);
        }
    }
}