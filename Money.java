/**
 * This class provides methods to create, retrieve, and modify money amounts. It also supports addition and subtraction
 * of money objects, which returns a new Money object with the resulting amount.
 *
 * @author Nas, Joash Monarch Villamera
 * @author Donaire, Lorenzo Lyon Caballero
 * S20B
 */

public class Money {

    private double amount;

    public Money(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Money add(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money subtract(Money other) {
        return new Money(this.amount - other.amount);
    }

    public String toString() {
        return amount + " php";
    }
}
