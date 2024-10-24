package uvt;

import uvt.Exceptions.InvalidVehicleTypeException;
import uvt.Vehicles.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is the abstract class that is inherited by all the classes Motorcycle, Car, Van, Bus and Truck.
 */
public abstract class Vehicle {
    /**
     * Represents the subscription a vehicle has (set to null if the vehicle does not have
     * a subscription).
     */
    private uvt.Subscription subscription;

    /**
     * Represents the exit of the last parking. It will be used for obtaining the occupancy rate.
     */
    private LocalDateTime lastExit;

    /**
     * Represents the number of times the vehicle parked in the parking chain. It is used
     * to give a discount for the vehicle at the 10th parking.
     */
    private int parkingEntrances;

    /**
     * Represents the discount the vehicle will get at the 10th parking. It will be different
     * for each type of vehicle.
     */
    protected int discount;

    /**
     * Represents the price paid per hour by a vehicle. It will be different
     * for each type of vehicle.
     */
    protected double parkingPriceHour;

    /**
     * Represents the type of the vehicle.
     */
    protected VehicleType type;

    /**
     * Represents the parking intervals for the car in each day.
     */
    private final Map<String, List<uvt.ParkingInterval>> parkingIntervals = new HashMap<>();

    /**
     * This is an empty constructor for class Vehicle.
     * It sets the number of parking entrances equal to 0 and the subscription equal to null.
     */
    public Vehicle() {
        this.subscription = null;
        this.parkingEntrances = 0;
    }
    /**
     * Represents the constructor of the Vehicle class.
     * It sets the number of parking entrances equal to 0.
     * @param subscription Represents the subscription the car has or null if it foes not have any subscription.
     */
    public Vehicle(uvt.Subscription subscription) {
        this.subscription = subscription;
        this.parkingEntrances = 0;
    }

    /**
     * This is the getter for lastExit.
     * @return A LocalDateTime value representing the exit of the last parking.
     */
    public LocalDateTime getLastExit() {
        return lastExit;
    }

    /**
     * This is the setter for lastExit.
     * @param lastExit Represents the new exit of the last parking.
     */
    public void setLastExit(LocalDateTime lastExit) {
        this.lastExit = lastExit;
    }

    /**
     * This is the getter for type.
     * @return The type of the vehicle.
     */
    public VehicleType getType() {
        return type;
    }

    /**
     * This is the setter for subscription.
     * @param subscription Represents the new Subscription of the vehicle.
     */
    public void setSubscription(uvt.Subscription subscription) {
        this.subscription = subscription;
    }

    /**
     * This is the getter for subscription.
     * @return The subscription of the car
     */
    public uvt.Subscription getSubscription() {
        return subscription;
    }

    /**
     * This is the getter for parkingEntrances.
     *
     * @return An integer representing the number of times tge vehicle parked
     * in the parking chain.
     */
    public int getParkingEntrances() {
        return parkingEntrances;
    }

    /**
     * This is the getter for parkingIntervals.
     *
     * @return A map representing the parkingIntervalsMap.
     * in which the car was parked each day.
     */
    public Map<String, List<uvt.ParkingInterval>> getParkingIntervals() {
        return parkingIntervals;
    }

    /**
     * This method adds a parking interval to the list of parking intervals of a car.
     * @param entry Represents the time the vehicle entries the parking lot.
     * @param exit Represents the time the vehicle exits the parking lot.
     */
    public void addParkingInterval(LocalDateTime entry, LocalDateTime exit) {
        String date = entry.toLocalDate().toString();

        if (!parkingIntervals.containsKey(date)) {
            parkingIntervals.put(date, new ArrayList<>());
        }

        parkingIntervals.get(date).add(new uvt.ParkingInterval(entry, exit));
    }

    /**
     * This increments the number of parkingEntrances a vehicle has.
     */
    public void addParkingEntrances() {
        this.parkingEntrances++;
    }

    /**
     * This computes the total amount of money that must be paid for the parking intervals in a day.
     * It checks if the vehicle has subscription and if it has, it checks if the
     * bought date is equal to the date in which the parking cost will be computed.
     * For each interval it computes its duration and how many hours the vehicle
     * was parked. It checks if the vehicle does not have a subscription or the subscription is not valid
     * for the entryTime of the current interval. If it is so, the parking cost will
     * be computed taking into consideration the discount (if it should be given),
     * otherwise nothing will be added to the total amount of money.
     *
     * @param data Represents the date in which the parking cost will be computed.
     * @return A double representing the amount of money that was paid.
     */
    public double parkingCost(String data) {
        double amount = 0;

        if (subscription != null) {
            if (String.valueOf(subscription.getBoughtTime().toLocalDate()).equals(data)) {
                amount += subscription.getPrice();
            }
        }

        if (parkingIntervals.containsKey(data)) {
            for (uvt.ParkingInterval parkingInterval : parkingIntervals.get(data)) {
                Duration duration = Duration.between(parkingInterval.getEntry(), parkingInterval.getExit());
                long hours = duration.toHours();

                if (duration.toMinutesPart() > 0) {
                    hours++;
                }
                if (this.subscription == null || !subscription.isValid(parkingInterval.getEntry())) {
                    if (parkingInterval.hasDiscount()) {
                        amount += (parkingPriceHour - discount) * hours;
                    } else {
                        amount += parkingPriceHour * hours;
                    }
                }
            }
        }
        return amount;
    }

    /**
     * This method returns the type of a vehicle.
     * @param carType Represents the string representation of the type.
     * @return A VehicleType representing the type of the vehicle.
     * @throws InvalidVehicleTypeException It is thrown when an invalid type is passed to the method.
     */
    public static VehicleType getVehicleType(String carType) throws InvalidVehicleTypeException {
        return switch (carType) {
            case "motorcycle" -> VehicleType.MOTORCYCLE;
            case "car" -> VehicleType.CAR;
            case "van" -> VehicleType.VAN;
            case "bus" -> VehicleType.BUS;
            case "truck" -> VehicleType.TRUCK;
            default -> throw new InvalidVehicleTypeException("Unexpected value: '" + carType + "'.");
        };
    }

    /**
     * This method creates an object of a class that extends class Vehicle based on its type.
     * @param carType Represents the string representation of the type.
     * @return An object of one of the classes that extend class Vehicle.
     * @throws InvalidVehicleTypeException It is thrown when an invalid type is passed to the method.
     */
    public static Vehicle getVehicleObject(String carType) throws InvalidVehicleTypeException {
        Vehicle vehicle;

        switch (carType) {
            case "motorcycle" -> vehicle = new Motorcycle();
            case "car" -> vehicle = new Car();
            case "van" -> vehicle = new Van();
            case "bus" -> vehicle = new Bus();
            case "truck" -> vehicle = new Truck();
            default -> throw new InvalidVehicleTypeException("Unexpected value: " + carType);
        }

        return vehicle;
    }

    /**
     * This methods overrides the toString() method.
     *
     * @return The the representation of an object of type Vehicle.
     */
    @Override
    public String toString() {
        return "Vehicle{" +
                "subscription=" + subscription +
                ", parkingEntrances=" + parkingEntrances +
                ", discount=" + discount +
                ", parkingPriceHour=" + parkingPriceHour +
                ", type=" + type +
                ", parkingIntervals=" + parkingIntervals +
                '}';
    }
}
