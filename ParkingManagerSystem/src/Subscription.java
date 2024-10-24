package uvt;

import java.time.LocalDateTime;

/**
 * This objects of this class represent a paid subscription for the parking chain.
 * Each subscription lasts 30 days.
 */
public class Subscription {
    /**
     * Represents the time when the subscription was bought.
     */
    private final LocalDateTime boughtTime;
    /**
     * Represents the price paid for the subscription.
     */
    private final double price;

    /**
     * This is the constructor for class Subscription. It sets the price of a subscription equal to 130.
     * @param boughtTime The date on with the subscription was bought.
     */
    public Subscription(LocalDateTime boughtTime) {
        this.boughtTime = boughtTime;
        this.price = 130;
    }

    /**
     * This is the getter for the boughtTime
     * @return A LocalDateTime variable representing the in which the subscription was bought.
     */
    public LocalDateTime getBoughtTime() {
        return boughtTime;
    }

    /**
     * This is the getter for the price;
     * @return A double representing the price of the subscription.
     */
    public double getPrice() {
        return price;
    }

    /**
     * This method checks if the subscription is valid at a certain time. It will be used when
     * computing the total gain of a parking lot.
     * @param dateTime The date on which is checked if the subscription is still valid.
     * @return A boolean representing if the subscription is valid or not.
     */
    public boolean isValid(LocalDateTime dateTime) {
        if(this.boughtTime != null) {
            return dateTime.compareTo(boughtTime.plusDays(30)) <= 0 && dateTime.compareTo(boughtTime) >= 0;
        }

        return false;
    }

    /**
     * This methods overrides the toString() method.
     * @return The the representation of an object of type Subscription
     */
    @Override
    public String toString() {
        return "Subscription{" +
                "boughtTime=" + boughtTime +
                ", price=" + price +
                '}';
    }
}
