/**
 * SpecialVendingMachine represents a special type of vending machine that allows users to create custom products
 * by selecting items from the available inventory and making a payment for the selected items.
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class SpecialVendingMachine extends VendingMachine { // MODEL

    /**
     * This constructor initializes the slots, inventory, change, totalEarnings, totalChange, changeGiven, and sales arrays,
     * and sets the initial stock quantity for each item slot.
     * @param numSlots The number of slots in the vending machine to hold items.
     * @param capacity The capacity of each slot to hold items.
     * @param items An array of Item objects representing the items to be stocked in the vending machine.
     */
    public SpecialVendingMachine(int numSlots, int capacity, Item[] items) {
        super(numSlots, capacity, items);
        slots = new Item[numSlots][capacity];
        inventory = new double[numSlots][2]; // 2 columns for starting and ending inventory
        change = new double[numSlots][3]; // 3 columns for different change denominations
        totalEarnings = new Money(0.0);
        totalChange = new Money(1000.0);
        changeGiven = new Money(0.0);
        sales = new int[slots.length][2]; // 2 represents the columns quantity sold and total sales
        stockQuantity = 10;

        stockItem(items);
    }

    /**
     * Allows the user to create a custom product by selecting items from the vending machine
     * and making a payment. The method displays the available items in the vending machine
     * along with their prices and calories.
     */
    public void createCustomProduct() {

        // Display the items in the slots for the user to choose from
        StringBuilder itemsInfoBuilder = new StringBuilder("Select items to include in the custom product (only 1 item per addition to prevent malfunction)\n");
        for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
            for (int itemIndex = 0; itemIndex < slots[slotIndex].length; itemIndex++) {
                Item item = slots[slotIndex][itemIndex];
                if (item != null) {
                    String itemInfo = item.getName() +
                            " - Price: " + item.getPrice().getAmount() + " - Calories: " + item.getCalories() + "\n";
                    itemsInfoBuilder.append(itemInfo);
                }
            }
        }

        // Show the list of items in JOptionPane message
        JOptionPane.showMessageDialog(null, itemsInfoBuilder.toString());

        ArrayList<Item> selectedItems = showCustomProductDialog(slots);

        if (selectedItems == null || selectedItems.isEmpty()) {
            return; // No items were selected, so exits the method
        }

        for (Item item : selectedItems) {
            int[] slotAndItemIndices = getSlotAndItemIndices(selectedItems.indexOf(item), slots);
            int slotIndex = slotAndItemIndices[0];
            int itemIndex = slotAndItemIndices[1];
            System.out.println("Selected Item: " + item.getName() + ", Slot Index: " + slotIndex + ", Item Index: " + itemIndex);
        }

        if (selectedItems != null && !selectedItems.isEmpty()) {
            // Calculates the total price and total calories
            Money totalPrice = new Money(0.0);
            int totalCalories = 0;
            for (Item item : selectedItems) {
                totalPrice.setAmount(totalPrice.getAmount() + item.getPrice().getAmount());
                totalCalories += item.getCalories();
            }

            // Creates a new Item object representing the custom product with the calculated total price and calories.
            Item customProduct = new Item("Custom Product", totalPrice.getAmount(), totalCalories);

            // Displays product info and ask for payment
            JOptionPane.showMessageDialog(null, customProduct.getName() + " - Price: " + customProduct.getPrice().getAmount() + " - Calories: " + customProduct.getCalories());

            double amountPaid = Double.parseDouble(JOptionPane.showInputDialog(null, "Enter payment: "));
            double changeAmount;

            if (amountPaid >= totalPrice.getAmount()) {
                changeAmount = amountPaid - totalPrice.getAmount();
                double currentTotalEarnings = totalEarnings.getAmount();
                totalEarnings.setAmount(currentTotalEarnings + totalPrice.getAmount());

                if (hasEnoughChange(changeAmount)) {
                    // Dispenses the custom product to the user
                    JOptionPane.showMessageDialog(null, "Preparing custom product...");

                    for (Item item : selectedItems) {
                        JOptionPane.showMessageDialog(null, "Preparing and adding " + item.getName() + "...");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    JOptionPane.showMessageDialog(null, "\nCustom product created!");
                    JOptionPane.showMessageDialog(null, "\nHere is your change: " + (amountPaid - customProduct.getPriceForControl()));

                    int[] slotAndItemIndices = getSlotAndItemIndices(selectedItems.size() - 1, slots);
                    int slotIndex = slotAndItemIndices[0];

                    receivePayment(slotIndex, amountPaid);

                } else {
                    JOptionPane.showMessageDialog(null, "Sorry, the machine does not have enough change to conduct the transaction...");
                }
            } else {
                JOptionPane.showMessageDialog(null, "\nInsufficient payment. More money required for a successful transaction.");
            }
        }
    }

    /**
     * Displays a custom product dialog to the user, allowing them to select items from the vending machine
     * to include in the custom product. The method creates and formats the user interface components, including
     * a JList displaying the available items with their prices and calories.
     * @param slots 2D array representing the slots in the vending machine and their items.
     * @return An ArrayList containing the selected items for the custom product.
     */
    private ArrayList<Item> showCustomProductDialog(Object[][] slots) {
        ArrayList<Item> selectedItems = new ArrayList<>(); // Initializes the list
        Money totalPrice = new Money(0.0);

        // Create and format the UI components
        JList<String> itemList = new JList<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
            for (int itemIndex = 0; itemIndex < slots[slotIndex].length; itemIndex++) {
                Item item = (Item) slots[slotIndex][itemIndex];
                if (item != null) {
                    String itemInfo =  item.getName() +
                            " - Price: " + item.getPrice().getAmount() + " - Calories: " + item.getCalories();
                    listModel.addElement(itemInfo);
                }
            }
        }
        itemList.setModel(listModel);
        itemList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        itemList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // Creates a label to display the current total price
        JLabel totalPriceLabel = new JLabel("Total Price: " + totalPrice.getAmount());

        // Creates an "Add" button to add the selected items to the custom product
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedIndices = itemList.getSelectedIndices();
                for (int index : selectedIndices) {
                    int[] slotAndItemIndices = getSlotAndItemIndices(index, slots);
                    if (slotAndItemIndices != null) {
                        int slotIndex = slotAndItemIndices[0];
                        int itemIndex = slotAndItemIndices[1];
                        Item selectedItem = (Item) slots[slotIndex][itemIndex];
                        if (selectedItem != null) {
                            if (!selectedItems.contains(selectedItem)) {
                                selectedItems.add(selectedItem); // Adds the item to the list
                                totalPrice.setAmount(totalPrice.getAmount() + selectedItem.getPrice().getAmount());
                            }
                        }
                    }
                }
                totalPriceLabel.setText("Total Price: " + totalPrice.getAmount());
            }
        });

        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = SwingUtilities.getWindowAncestor(finishButton);
                if (window instanceof JDialog) {
                    window.dispose();
                }
            }
        });

        // Creates a panel to hold the item list and buttons
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(itemList), BorderLayout.CENTER);
        panel.add(totalPriceLabel, BorderLayout.NORTH);

        // Adds the "Add" and "Finish" buttons to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(finishButton);
        panel.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel to the main panel

        // Creates the custom product dialog
        JDialog customProductDialog = createDialog(panel, "Custom Product Selection");

        customProductDialog.pack();
        customProductDialog.setLocationRelativeTo(null);
        customProductDialog.setModal(true);
        customProductDialog.setVisible(true);

        return selectedItems;
    }

    /**
     * Retrieves the slot index and item index in the 2D array representing the vending machine slots
     * based on the given selected index. The method iterates through the slots and counts the total number
     * of items displayed in the vending machine.
     * @param selectedIndex The selected index representing the total number of items displayed.
     * @param slots The 2D array representing the slots in the vending machine and their items.
     * @return An integer array containing the slot index and item index for the selected index.
     */
    private int[] getSlotAndItemIndices(int selectedIndex, Object[][] slots) {
        int totalItemsDisplayed = 0;
        for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
            for (int itemIndex = 0; itemIndex < slots[slotIndex].length; itemIndex++) {
                Item item = (Item) slots[slotIndex][itemIndex];
                if (item != null) {
                    if (totalItemsDisplayed == selectedIndex) {
                        return new int[]{slotIndex, itemIndex};
                    }
                    totalItemsDisplayed++;
                }
            }
        }
        return new int[]{-1, -1}; // Return -1 if the index is out of bounds
    }

    /**
     * Creates a custom JDialog with the specified content and title.
     * @param content The JComponent content to be displayed in the dialog.
     * @param title The title of the dialog window.
     * @return A JDialog instance with the specified content and title, set as modal.
     */
    private JDialog createDialog(JComponent content, String title) {
        JDialog dialog = new JDialog((Frame) null, title, true);
        dialog.add(content);
        return dialog;
    }

}

