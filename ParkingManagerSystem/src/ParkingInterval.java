package uvt;

import java.time.LocalDateTime;

/**
 * The objects of this class represent the parking intervals.
 */
public class ParkingInterval {
    /**
     * Represents the time when the car has entered the parking chain.
     */
    private final LocalDateTime entry;
    /**
     * Represents the time when the car exits the parking chain.
     */
    private final LocalDateTime exit;

    /**
     * Represents the fact that the car will get (or not) a discount for this parking inverval.
     */
    private boolean discount;

    /**
     * Represents the constructor of class ParkingIntervals.
     * It sets discount equal to false because it will be modified later.
     * @param entry Represents the time when the car entered the parking lot.
     * @param exit Represents the time when the car exited the parking lot.
     */
    public ParkingInterval(LocalDateTime entry, LocalDateTime exit) {
        this.entry = entry;
        this.exit = exit;
        discount = false;
    }

    /**
     * This is the getter for discount
     * @return A boolean telling if the car will get a discount for that parking
     * interval or not.
     */
    public boolean hasDiscount() {
        return discount;
    }

    /**
     * This is the setter for discount. It will modify its value.
     * @param discount A boolean representing the new value of discount.
     */
    public void setDiscount(boolean discount) {
        this.discount = discount;
    }

    /**
     * This is the getter for entry.
     * @return A LocalDateTime value representing the entry time.
     */
    public LocalDateTime getEntry() {
        return entry;
    }

    /**
     * This is the getter for exit.
     * @return A LocalDateTime value representing the exit time.
     */
    public LocalDateTime getExit() {
        return exit;
    }

    /**
     * This method overrides toString()
     * @return The representation of an object of type ParkingInterval.
     */
    @Override
    public String toString() {
        return "ParkingInterval{" +
                "entry=" + entry +
                ", exit=" + exit +
                ", discount=" + discount +
                '}';
    }
}
