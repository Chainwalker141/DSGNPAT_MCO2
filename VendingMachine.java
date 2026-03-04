/**
 * The VendingMachine class represents a model of a vending machine that allows users to purchase items,
 * receive change, and view transaction summaries. It extends the JFrame class to display transaction summaries.
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class VendingMachine extends JFrame{ // MODEL

    protected Money totalEarnings;
    protected Money totalChange;
    protected Money changeGiven;
    protected Item[][] slots; // 2D array to hold items in each slot
    protected Item[] items;
    protected double[][] inventory; // 2D array to track starting and ending inventory of items
    protected int[][] sales; // 2D array to track item sales and total sales
    protected double[][] change; // 2D array to track change denominations
    protected int stockQuantity;

    private JTextArea messageTextArea;

    /**
     * Constructs a VendingMachine object with the specified number of slots, capacity, and items.
     * @param numSlots The number of slots in the vending machine.
     * @param capacity The maximum capacity of each slot to hold items.
     * @param items An array of Item objects representing the items to be stocked in the vending machine.
     */
    VendingMachine(int numSlots, int capacity, Item[] items) {
        slots = new Item[numSlots][capacity];
        inventory = new double[numSlots][2]; // 2 columns for starting and ending inventory
        change = new double[numSlots][3]; // 3 columns for different change denominations
        totalEarnings = new Money(0.0);
        totalChange = new Money(1000.0);
        changeGiven = new Money(0.0);
        sales = new int[slots.length][2]; // 2 represents the columns quantity sold and total sales
        stockQuantity = 10;

        stockItem(items);
        this.messageTextArea = new JTextArea();

    }

    //*****************************************************************************************************************************************

    // VENDING MACHINE FEATURES

    /**
     * This method displays the available items in the vending machine.
     */
    public Object[][] displayItems() {
        // Generate data for the table
        Object[][] data = new Object[slots.length * slots[0].length][5];
        int dataIndex = 0;
        for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
            for (int itemIndex = 0; itemIndex < slots[slotIndex].length; itemIndex++) {
                Item item = slots[slotIndex][itemIndex];
                if (item != null) {
                    data[dataIndex][0] = "Slot " + (slotIndex + 1);
                    data[dataIndex][1] = item.getName();
                    data[dataIndex][2] = item.getPrice().getAmount();
                    data[dataIndex][3] = item.getCalories();
                    data[dataIndex][4] = getStockQuantity(item);
                    dataIndex++;
                }
            }
        }
        return data;
    }

    /**
     * Retrieves the current stock quantity of the specified item in the vending machine.
     * @param item The Item object for which the stock quantity needs to be retrieved.
     * @return The current stock quantity of the specified item.
     */
    public double getStockQuantity(Item item) {
        int slotIndex = getSlotIndex(item);
        if (slotIndex >= 0) {
            return 10 - (inventory[slotIndex][0] - inventory[slotIndex][1]);
        }
        return 0;
    }


    public int getSlotIndex(Item item) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i][0] == item) {
                return i;
            }
        }
        return -1;
    }

    public int[][] getSales() {
        return sales;
    }

    public Item[][] getSlots() {
        return slots;
    }
    public Item[] getItems() {
        return items;
    }
    public void setItems(Item[] items) {
        this.items = items;
    }


    public void stockItem(Item[] items) {
        this.items = items;
        for (int i = 0; i < items.length; i++) {
            int slotIndex = i % slots.length;
            int itemIndex = i / slots.length;
            slots[slotIndex][itemIndex] = items[i];
        }
    }

    /**
     * This method processes a payment for an item in the vending machine and completes the transaction.
     *
     * @param slotIndex The index of the slot containing the item to be purchased.
     * @param amountPaid The amount of money paid by the user for the item.
     */
    public void receivePayment(int slotIndex, double amountPaid) {
        Item item = slots[slotIndex][0];
        Money itemPrice = item.getPrice();
        double itemPriceValue = itemPrice.getAmount();

        if (amountPaid >= itemPrice.getAmount()) {
            double changeAmount = amountPaid - itemPriceValue;
            double currentTotalEarnings = totalEarnings.getAmount();
            totalEarnings.setAmount(currentTotalEarnings + itemPriceValue);

            if (hasEnoughChange(changeAmount)) {
                // Dispenses item and provides change
                dispenseItem(item);
                giveChange(changeAmount);
                sales[slotIndex][0]++; // Increases quantity sold
                sales[slotIndex][1] += itemPriceValue; // Increases total sales

                messageTextArea.append("\nTransaction successful!\n");
                messageTextArea.append("You purchased: " + item.getName() + "\n");
                messageTextArea.append("Change given: " + new Money(changeAmount) + "\n");
            } else {
                messageTextArea.append("Sorry, the machine does not contain enough change for the transaction.\n");
            }
        } else {
            messageTextArea.append("\nInsufficient payment. More money required.\n");
        }
    }

    /**
     * This method dispenses an item from the vending machine and updates the inventory accordingly.
     *
     * @param item The item to be dispensed.
     */
    private void dispenseItem(Item item) {
        messageTextArea.append("\nDispensing " + item.getName() + "...\n\n");
        messageTextArea.append(item.getName() + " is dispensed. It contains " + item.getCalories() + " calories. Enjoy your food!\n\n");

        // Decreases the quantity of the item in the inventory
        for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
            Item slotItem = slots[slotIndex][0]; // One item per slot
            if (slotItem != null && slotItem.equals(item)) {
                double currentStock = inventory[slotIndex][1]; // Gets the current ending inventory
                inventory[slotIndex][1] = currentStock - 1; // Decreases ending inventory by 1
                break;
            }
        }
    }

    /**
     * This method checks if the vending machine has enough change to provide for a given amount.
     *
     * @param amount The amount for which change availability needs to be checked.
     * @return true if the machine has enough change, false otherwise
     */
    protected boolean hasEnoughChange(double amount) {
        // Calculates the total change available in the machine
        double totalChange = 1000.0;
        for (int slotIndex = 0; slotIndex < change.length; slotIndex++) {
            for (int denomIndex = 0; denomIndex < change[slotIndex].length; denomIndex++) {
                totalChange += change[slotIndex][denomIndex];
            }
        }
        // Checks if the machine has enough change for the given amount
        return totalChange >= amount;
    }


    /**
     * This method provides change to the customer for a given amount.
     *
     * @param amount The amount of change to be provided.
     */
    protected void giveChange(double amount) {
        messageTextArea.append("Providing change...");

        messageTextArea.append("\n\nHere is your change: " + new Money(amount) + "\n");

        // Initializes an array to track the number of money for each denomination
        int[] coinCount = new int[7]; // (5, 10, 20, 50, 100, 200, 500)

        // Calculate the number of coins for each denomination
        double remainingChange = amount;
        for (int denomIndex = 0; denomIndex < change[0].length; denomIndex++) {
            double denomValue = getDenominationValue(denomIndex);
            int coins = (int) (remainingChange / denomValue);
            coinCount[denomIndex] = coins;
            remainingChange -= coins * denomValue;
        }

        // Updates the change inventory in the machine
        for (int slotIndex = 0; slotIndex < change.length; slotIndex++) {
            for (int denomIndex = 0; denomIndex < change[slotIndex].length; denomIndex++) {
                change[slotIndex][denomIndex] -= coinCount[denomIndex];
            }
        }

        // Prints the number of coins for each denomination
        for (int denomIndex = 0; denomIndex < coinCount.length; denomIndex++) {
            System.out.println("Denomination " + (denomIndex + 1) + ": " + coinCount[denomIndex]);
        }
        changeGiven = new Money(changeGiven.getAmount() + amount);
    }

    /**
     * This method retrieves the value of a denomination based on its index.
     *
     * @param denomIndex The index of the denomination.
     * @return The value of the denomination.
     * @throws IllegalArgumentException If the denomIndex is invalid
     */
    private double getDenominationValue(int denomIndex) {
        switch (denomIndex) {
            case 0:
                return 5;
            case 1:
                return 10;
            case 2:
                return 20;
            case 3:
                return 50;
            case 4:
                return 100;
            case 5:
                return 200;
            case 6:
                return 500;
            default:
                throw new IllegalArgumentException("Invalid denomination index: " + denomIndex);
        }
    }


    //*****************************************************************************************************************************************
    // MAINTENANCE

    /**
     * This method restocks the specified quantity of an item in the vending machine slot.
     *
     * @param slotIndex The index of the slot where the item is located.
     * @param itemIndex The index of the item within the slot.
     * @param quantity The quantity of items to be restocked.
     */
    public void restockItem(int slotIndex, int itemIndex, int quantity) {
        if (slotIndex >= 0 && slotIndex < slots.length && itemIndex >= 0 && itemIndex < slots[slotIndex].length) {
            Item item = slots[slotIndex][itemIndex];
            inventory[slotIndex][1] += quantity; // Increase the ending inventory
            System.out.println("Restocked " + quantity + " items of " + item.getName() + " in slot " + (slotIndex + 1));
            displayItems(); // Display the updated items after restocking
        } else {
            System.out.println("Invalid slot index. Please try again.");
        }
    }

    /**
     * This method sets the price for all items in the specified vending machine slot.
     *
     * @param slotIndex The index of the slot where the items are located.
     * @param price The new price to be set for the items.
     */
    public void setSlotItemPrice(int slotIndex, double price) {
        if (slotIndex >= 0 && slotIndex < slots.length) {
            for (int itemIndex = 0; itemIndex < slots[slotIndex].length; itemIndex++) { //iterates through the slots
                slots[slotIndex][0].setPrice(price); // sets the new price
            }
            System.out.println("Price set for all items in slot " + (slotIndex + 1) + ": " + price);
        } else {
            System.out.println("Invalid slot index. Please provide a valid slot index.");
        }
    }

    /**
     * This method retrieves the total earnings of the vending machine, including the earnings after changes given.
     *
     * @return The total earnings of the vending machine, including the earnings after changes given.
     */
    public Money getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * This method replenishes the change inventory of the vending machine to its maximum capacity.
     * The current change amount is displayed, and then the change given is reset to zero.
     */
    public void replenishChange() {
        double currentTotalChange = totalChange.getAmount();
        double currentChangeGiven = changeGiven.getAmount();

        System.out.println("Current change: " + (currentTotalChange - currentChangeGiven));
        double replenishAmount = currentTotalChange ;
        changeGiven = new Money(0.0);
        System.out.println("Change replenished: " + replenishAmount);
    }

    public Money getChangeGiven() {
        return changeGiven;
    }

    public Money getTotalChange() {
        return totalChange;
    }

    public double currentChange() {
        return (totalChange.getAmount() - changeGiven.getAmount());
    }


    /**
     * Displays a transaction summary for the vending machine.
     * @param slots     A 2D array representing the slots and their respective items in the vending machine.
     * @param inventory A 2D array representing the inventory of items in the vending machine, including starting and ending quantities.
     * @param sales     A 2D array representing the sales data for each slot in the vending machine, including quantity sold and total sales.
     */
    public void displaySummary(Object[][] slots, double[][] inventory, int[][] sales) {

        setTitle("Vending Machine Transaction Summary");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Creates a panel for the transaction summary
        JPanel summaryPanel = createTransactionSummaryPanel();

        // Creates a panel for the inventory display
        JPanel inventoryPanel = createInventoryPanel();

        // Adds the panels to the frame
        setLayout(new BorderLayout());
        add(summaryPanel, BorderLayout.NORTH);
        add(inventoryPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates and returns a JPanel that displays the transaction summary for the vending machine.
     * @return A JPanel representing the transaction summary.
     */
    public JPanel createTransactionSummaryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Displays starting inventory
        panel.add(new JLabel("Starting Inventory:"));
        for (int i = 0; i < slots.length; i++) {
            Item item = (Item) slots[i][0];
            panel.add(new JLabel("Slot " + (i + 1) + ": " + item.getName() + " - Stock: 10"));
        }

        // Displays quantity sold and total sales for each slot
        panel.add(new JLabel("Sales:"));
        for (int i = 0; i < slots.length; i++) {
            Item item = (Item) slots[i][0];
            int quantitySold = (int) sales[i][0];
            double totalSales = sales[i][1];
            DecimalFormat df = new DecimalFormat("#.##");
            panel.add(new JLabel("Slot " + (i + 1) + ": " + item.getName() + " - Quantity Sold: " + quantitySold + " - Total Sales: " + df.format(totalSales) + " php"));
        }

        // Displays ending inventory (current stock)
        panel.add(new JLabel("Ending Inventory:"));

        return panel;
    }

    /**
     * Creates and returns a JPanel that displays the current inventory of items in the vending machine.
     * @return A JPanel representing the current inventory.
     */
    public JPanel createInventoryPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (int i = 0; i < slots.length; i++) {
            Item item = (Item) slots[i][0];
            int stockQuantity = 10 - (int) (inventory[i][0] - inventory[i][1]);
            panel.add(new JLabel("Slot " + (i + 1) + ": " + item.getName() + " - Stock: " + stockQuantity));
        }

        return panel;
    }


}
