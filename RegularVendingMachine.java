/**
 *  This class extends the abstract class VendingMachine and implements the functionality specific to a regular vending machine.
 *  It manages the slots and inventory of items, keeps track of the total earnings and change available in the machine,
 *  and handles the sales and quantity of items sold.
 *
 *  @author Nas, Joash Monarch Villamera
 *  @author Donaire, Lorenzo Lyon Caballero
 *  S20B
 */

public class RegularVendingMachine extends VendingMachine{ // MODEL

    /**
     * RegularVendingMachine represents a regular vending machine that sells items and manages its inventory and earnings.
     * @param numSlots The number of slots in the vending machine to hold items.
     * @param capacity The capacity of each slot to hold items.
     * @param items An array of Item objects representing the items to be stocked in the vending machine.
     */
    public RegularVendingMachine(int numSlots, int capacity, Item[] items) {
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

}
