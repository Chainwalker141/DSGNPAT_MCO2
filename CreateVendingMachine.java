/**
 * The CreateVendingMachine class represents a model for creating and managing vending machines.
 * It implements the Virtual Proxy pattern for Lazy Loading.
 *
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

import javax.swing.JOptionPane;

public class CreateVendingMachine { // MODEL

    private VendingMachine newVendingMachine;
    private RegularVendingMachine regVendMachine;
    private SpecialVendingMachine specVendMachine;
    private VendingMachineFactoryView view;

    private Item[] items;
    private String vendingType;
    private int numSlots;
    private int numItems;

    /**
     *  Constructs a new RegularVendingMachine instance to create and manage a RegularVendingMachine.
     *  This constructor initializes the RegularVendingMachine instance and creates instances of Item
     *  representing different items.
     *
     *  @param numSlots The number of slots in the vending machine to store items.
     *  @param numItems The total number of different items to be stocked in the vending machine.
     *  @param view     The VendingMachineFactoryView instance associated with this vending machine.
     */
    
    public CreateVendingMachine(int numSlots, int numItems, VendingMachineFactoryView view) {
        this.numSlots = numSlots;
        this.numItems = numItems;
        this.view = view;
        this.items = new Item[numItems];
        this.vendingType = "Regular";
    }

    public CreateVendingMachine(int numSlots, int numItems, String vendingType) {
        this.numSlots = numSlots;
        this.numItems = numItems;
        this.items = new Item[numItems];
        this.vendingType = vendingType;
    }

    private void ensureRegularInitialized() {
        if (regVendMachine == null) {
            initializeItems(); 
            regVendMachine = new RegularVendingMachine(numSlots, numItems, items);
            regVendMachine.stockItem(items);
            this.newVendingMachine = regVendMachine;
        }
    }

    private void ensureSpecialInitialized() {
        if (specVendMachine == null) {
            initializeItems();
            specVendMachine = new SpecialVendingMachine(numSlots, numItems, items);
            specVendMachine.stockItem(items);
            this.newVendingMachine = specVendMachine;
        }
    }

    private void initializeItems() {
        for(int i = 0; i < numSlots; i++) {
            String itemName = promptStringInput("Enter item name for slot " + (i + 1) + ":");
            if (itemName == null || itemName.trim().isEmpty()) itemName = "Default Item " + (i + 1);
            
            double itemPrice = promptDoubleInput("Enter item price for " + itemName + ":");
            int itemQuantity = promptIntInput("Enter item quantity for " + itemName + ":");
            int itemCalories = promptIntInput("Enter item calorie count for " + itemName + ":");

            items[i] = new Item(itemName, itemPrice, itemCalories);
        }
    }

    public String promptStringInput(String message) {
        return JOptionPane.showInputDialog(null, message);
    }

    public int promptIntInput(String message) {
        String input = JOptionPane.showInputDialog(null, message);
        if (input == null || input.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public double promptDoubleInput(String message) {
        String input = JOptionPane.showInputDialog(null, message);
        if (input == null || input.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public String getVendingType() {
        return vendingType;
    }

    // --- REGULAR MACHINE WRAPPERS ---

    public String displayRegVend() {
        ensureRegularInitialized();
        StringBuilder sb = new StringBuilder();
        Item[][] slots = regVendMachine.getSlots();
        for (int i = 0; i < slots.length; i++) {
            Item item = slots[i][0];
            if (item != null) {
                sb.append("Slot ").append(i + 1).append("\n")
                  .append("Item Name: ").append(item.getName()).append("\n")
                  .append("Price: ").append(item.getPrice().getAmount()).append(" php\n")
                  .append("Calories: ").append(item.getCalories()).append("\n")
                  .append("Stock: ").append(regVendMachine.getStockQuantity(item)).append("\n\n");
            }
        }
        return sb.toString();
    }

    public void receiveRegularPayment(int slotNumber, double payment) {
        ensureRegularInitialized();
        regVendMachine.receivePayment(slotNumber, payment);
    }

    public Item getRegularItem(int slotNumber) {
        ensureRegularInitialized();
        return regVendMachine.getSlots()[slotNumber][0];
    }

    public double getRegularCurrentChange() {
        ensureRegularInitialized();
        return regVendMachine.currentChange();
    }

    public int getRegularSlotCount() {
        ensureRegularInitialized();
        return regVendMachine.getSlots().length;
    } 

    public void restockRegularItem(int slot, int itemIndex, int quantity) {
        ensureRegularInitialized();
        regVendMachine.restockItem(slot, itemIndex, quantity);
    }

    public void setRegularItemPrice(int slot, double price) {
        ensureRegularInitialized();
        regVendMachine.setSlotItemPrice(slot, price);
    }

    public Money getRegularTotalEarnings() {
        ensureRegularInitialized();
        return regVendMachine.getTotalEarnings();
    }

    public void replenishRegularChange() {
        ensureRegularInitialized();
        regVendMachine.replenishChange();
    }

    public void displayRegularSummary() {
        ensureRegularInitialized();
        regVendMachine.displaySummary(regVendMachine.getSlots(), regVendMachine.inventory, regVendMachine.getSales());
    }

    // --- SPECIAL MACHINE WRAPPERS ---

    public String displaySpecVend() {
        ensureSpecialInitialized();
        StringBuilder sb = new StringBuilder();
        Item[][] slots = specVendMachine.getSlots();
        for (int i = 0; i < slots.length; i++) {
            Item item = slots[i][0];
            if (item != null) {
                sb.append("Slot ").append(i + 1).append("\n")
                  .append("Item Name: ").append(item.getName()).append("\n")
                  .append("Price: ").append(item.getPrice().getAmount()).append(" php\n")
                  .append("Calories: ").append(item.getCalories()).append("\n")
                  .append("Stock: ").append(specVendMachine.getStockQuantity(item)).append("\n\n");
            }
        }
        return sb.toString();
    }

    /**
     * Wrapper for the custom product creation logic in Special Vending Machine.
     */
    public void createSpecialCustomProduct() {
        ensureSpecialInitialized();
        specVendMachine.createCustomProduct();
    }

    public void receiveSpecialPayment(int slotNumber, double payment) {
        ensureSpecialInitialized();
        specVendMachine.receivePayment(slotNumber, payment);
    }

    public Item getSpecialItem(int slotNumber) {
        ensureSpecialInitialized();
        return specVendMachine.getSlots()[slotNumber][0];
    }

    public double getSpecialCurrentChange() {
        ensureSpecialInitialized();
        return specVendMachine.currentChange();
    }

    public int getSpecialSlotCount() {
        ensureSpecialInitialized();
        return specVendMachine.getSlots().length;
    }

    public void restockSpecialItem(int slot, int itemIndex, int quantity) {
        ensureSpecialInitialized();
        specVendMachine.restockItem(slot, itemIndex, quantity);
    }

    public void setSpecialItemPrice(int slot, double price) {
        ensureSpecialInitialized();
        specVendMachine.setSlotItemPrice(slot, price);
    }

    public Money getSpecialTotalEarnings() {
        ensureSpecialInitialized();
        return specVendMachine.getTotalEarnings();
    }

    public void replenishSpecialChange() {
        ensureSpecialInitialized();
        specVendMachine.replenishChange();
    }

    public void displaySpecialSummary() {
        ensureSpecialInitialized();
        specVendMachine.displaySummary(specVendMachine.getSlots(), specVendMachine.inventory, specVendMachine.getSales());
    }
}