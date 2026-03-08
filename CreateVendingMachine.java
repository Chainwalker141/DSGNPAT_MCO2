/**
 *  The CreateVendingMachine class represents a model for creating and managing vending machines.
 *  It allows the user to create either a RegularVendingMachine or a SpecialVendingMachine,
 *  both of which can store and display different items.
 *  This class contains constructors to create a RegularVendingMachine or a SpecialVendingMachine,
 *  and methods to prompt the user for item details, stock the vending machine, and display available items.
 *  It also provides utility methods to prompt for string, integer, and double inputs using JOptionPane.
 *
 *  @author Nas, Joash Monarch Villamera
 *  @author Donaire, Lorenzo Lyon Caballero
 *  S20B
 */

import javax.swing.JTable;
import java.util.Scanner;
import javax.swing.JOptionPane;
public class CreateVendingMachine { // MODEL

    VendingMachine newVendingMachine;
	RegularVendingMachine regVendMachine;
    SpecialVendingMachine specVendMachine;
    private VendingMachineFactoryView view;

    private Item[] items;
    String vendingType;

    // table
    private JTable itemTable;

    Scanner sc = new Scanner(System.in);

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

        items = new Item[numItems];
        this.view = view;

        String itemName;
        double itemPrice;
        int itemQuantity, itemCalories;

        for(int i = 0; i < numSlots; i++) {
            itemName = promptStringInput("Enter item name for slot " + (i + 1) + ":");
            itemPrice = promptDoubleInput("Enter item price for " + itemName + ":");
            itemQuantity = promptIntInput("Enter item quantity for " + itemName + ":");
            itemCalories = promptIntInput("Enter item calorie count for " + itemName + ":");

            items[i] = new Item(itemName, itemPrice, itemCalories);
        }

        regVendMachine = new RegularVendingMachine(numSlots, numItems, items);
        regVendMachine.stockItem(items);
        this.vendingType = "Regular";

        this.newVendingMachine = regVendMachine;
    }

    /**
     * This constructor initializes the SpecialVendingMachine instance and creates instances of Item
     * representing different items. The provided vendingType parameter specifies the type of vending
     * machine being created (e.g., "Regular", "Special", etc.).
     *
     * @param numSlots The number of slots in the vending machine to store items.
     * @param numItems The total number of different items to be stocked in the vending machine.
     * @param vendingType The type of the vending machine being created.
     */
    public CreateVendingMachine(int numSlots, int numItems, String vendingType) {

        items = new Item[numItems];

        String itemName;
        double itemPrice;
        int itemQuantity, itemCalories;

        for(int i = 0; i < numSlots; i++) {
            itemName = promptStringInput("Enter item name for slot " + (i + 1) + ":");
            itemPrice = promptDoubleInput("Enter item price for " + itemName + ":");
            itemQuantity = promptIntInput("Enter item quantity for " + itemName + ":");
            itemCalories = promptIntInput("Enter item calorie count for " + itemName + ":");

            items[i] = new Item(itemName, itemPrice, itemCalories);
        }

        specVendMachine = new SpecialVendingMachine(numSlots, numItems, items);
        specVendMachine.stockItem(items);
        this.vendingType = vendingType;

        this.newVendingMachine = specVendMachine;
    }

    /**
     * This method uses the JOptionPane class to create a dialog box that shows the provided message
     * and allows the user to enter a string input. The method returns the string entered by the user.
     *
     * @param message The message to display in the dialog box, prompting the user for input.
     * @return The string input provided by the user. If the user cancels the dialog, null is returned.
     */
    public String promptStringInput(String message) {
        return JOptionPane.showInputDialog(null, message);
    }

    /**
     * Displays a message as a dialog box and prompts the user to enter an integer input.
     *
     * @param message The message to display in the dialog box, prompting the user for an integer input.
     * @return The integer value provided by the user. If the user cancels the dialog or provides invalid input,
     *         a NumberFormatException might occur.
     */
    public int promptIntInput(String message) {
        String input = JOptionPane.showInputDialog(null, message);
        return Integer.parseInt(input);
    }

    /**
     * Displays a message as a dialog box and prompts the user to enter a double-precision floating-point input.
     *
     * @param message The message to display in the dialog box, prompting the user for a double input.
     * @return The double value provided by the user. If the user cancels the dialog or provides invalid input,
     *         a NumberFormatException might occur.
     */
    public double promptDoubleInput(String message) {
        String input = JOptionPane.showInputDialog(null, message);
        return Double.parseDouble(input);
    }

    /**
     * This method returns the type of the vending machine, which can be "Regular" or "Special"
     * @return The type of the vending machine as a string
     */
    public String getVendingType() {
        return vendingType;
    }

    /**
     * Stocks the vending machine with items.
     */
    public void stockVendingMachine() {
        regVendMachine.setItems(items);
    }

    /**
     *  Displays the available items in the RegularVendingMachine.
     * @return A formatted string displaying the available items in the RegularVendingMachine.
     */
    public String displayRegVend() {
        StringBuilder sb = new StringBuilder();
        Item[][] slots = regVendMachine.getSlots();

        for (int i = 0; i < slots.length; i++) {
            Item item = slots[i][0];
            if (item != null) {
                sb.append("Slot ").append(i + 1).append("\n");
                sb.append("Item Name: ").append(item.getName()).append("\n");
                sb.append("Price: ").append(item.getPrice().getAmount()).append(" php\n");
                sb.append("Calories: ").append(item.getCalories()).append("\n");
                sb.append("Stock: ").append(regVendMachine.getStockQuantity(item)).append("\n\n");
            }
        }

        return sb.toString();
    }

    /**
     * Displays the available items in the SpecialVendingMachine.
     * @return A formatted string displaying the available items in the SpecialVendingMachine.
     */
    public String displaySpecVend() {
        StringBuilder sb = new StringBuilder();
        Item[][] slots = specVendMachine.getSlots();

        for (int i = 0; i < slots.length; i++) {
            Item item = slots[i][0];
            if (item != null) {
                sb.append("Slot ").append(i + 1).append("\n");
                sb.append("Item Name: ").append(item.getName()).append("\n");
                sb.append("Price: ").append(item.getPrice().getAmount()).append(" php\n");
                sb.append("Calories: ").append(item.getCalories()).append("\n");
                sb.append("Stock: ").append(specVendMachine.getStockQuantity(item)).append("\n\n");
            }
        }

        return sb.toString();
    }

    /**
     * Wrapper methods so that other classes do not touch regVendMachine anymore
     */

    public Item getRegularItem(int slotNumber) {
        return regVendMachine.getSlots()[slotNumber][0];
    }

    public double getRegularCurrentChange() {
        return regVendMachine.currentChange();
    }

    public double getRegularStock(Item item) {
        return regVendMachine.getStockQuantity(item);
    }

    public void receiveRegularPayment(int slotNumber, double payment) {
        regVendMachine.receivePayment(slotNumber, payment);
    }

    public int getRegularSlotCount() {
        return regVendMachine.getSlots().length;
    } 

    public void restockRegularItem(int slot, int itemIndex, int quantity) {
        regVendMachine.restockItem(slot, itemIndex, quantity);
    }

    public void setRegularItemPrice(int slot, double price) {
        regVendMachine.setSlotItemPrice(slot, price);
    }

    public Money getRegularTotalEarnings() {
        return regVendMachine.getTotalEarnings();
    }

    public void replenishRegularChange() {
        regVendMachine.replenishChange();
    }

    public void displayRegularSummary() {
        regVendMachine.displaySummary(
            regVendMachine.getSlots(),
            regVendMachine.inventory,
            regVendMachine.getSales()
        );
    }
    /**
     * Additional wrapper methods so other classes do not touch specVendMachine
     */

    public Item getSpecialItem(int slotNumber) {
        return specVendMachine.getSlots()[slotNumber][0];
    }

    public double getSpecialCurrentChange() {
        return specVendMachine.currentChange();
    }

    public double getSpecialStock(Item item) {
        return specVendMachine.getStockQuantity(item);
    }

    public int getSpecialSlotCount() {
        return specVendMachine.getSlots().length;
    }

    public void receiveSpecialPayment(int slotNumber, double payment) {
        specVendMachine.receivePayment(slotNumber, payment);
    }

    public void restockSpecialItem(int slot, int itemIndex, int quantity) {
        specVendMachine.restockItem(slot, itemIndex, quantity);
    }

    public void setSpecialItemPrice(int slot, double price) {
        specVendMachine.setSlotItemPrice(slot, price);
    }

    public Money getSpecialTotalEarnings() {
        return specVendMachine.getTotalEarnings();
    }

    public void replenishSpecialChange() {
        specVendMachine.replenishChange();
    }

    public void displaySpecialSummary() {
        specVendMachine.displaySummary(
            specVendMachine.getSlots(),
            specVendMachine.inventory,
            specVendMachine.getSales()
        );
    }
}
