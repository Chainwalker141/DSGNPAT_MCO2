/**
 *  The Item class represents an item that can be stored and sold in a vending machine.
 *  Each item has a name, price, price for control purposes, and calorie content.
 *
 *  @author Nas, Joash Monarch Villamera
 *  @author Donaire, Lorenzo Lyon Caballero
 *  S20B
 */


public class Item {

    private String name;
    private Money price;
    private double priceForControl;
    private int calories;

    /**
     * This constructor initializes an Item object with the specified name, price, and calories.
     *
     *
     * @param name - the name of the time
     * @param price - the price of the item
     * @param calories - the calories of the item
     */
    public Item(String name, double price, int calories) {
        this.name = name;
        this.price = new Money(price);
        this.calories = calories;
        this.priceForControl = price;
    }

    // Getters for item properties
    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public double getPriceForControl() {
        return priceForControl;
    }

    public void setPrice(double price) {
        this.price.setAmount(price);
    }

    public int getCalories() {
        return calories;
    }
}