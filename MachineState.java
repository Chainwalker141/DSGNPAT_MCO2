public interface MachineState {

    /**
     * Handle the logic for testing vending machine features based on the current state.
     */
    void handleTestFeatures();

    /**
     * Handle the logic for maintenance operations based on the current state.
     */
    void handleMaintenance();
}

