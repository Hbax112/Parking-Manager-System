package uvt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The objects of this class represent the areas in the parking lots.
 */
public class Area {
    /**
     * Represents the name of the area.
     */
    private final String name;
    /**
     * Represents the maximum capacity of the area for each type of vehicle.
     */
    private Map<VehicleType, Integer> maxCapacity = new HashMap<>();
    /**
     * Represents the number of parking places that are used for each type of vehicle.
     */
    private final Map<VehicleType, Integer> currentCapacity = new HashMap<>();
    /**
     * Maps each vehicle in the area to its vehicle plate.
     */
    private final Map<String, Vehicle> vehiclePlatesMap = new HashMap<>();

    /**
     * This is a constructor for the class Area.
     * @param name Represents the name of the area.
     */
    public Area(String name) {
        this.name = name;
        initializeTypeCapacity();
    }

    /**
     * This is a constructor for the class Area.
     * @param name  Represents the name of the area.
     * @param maxCapacity Represents the maximum number of parking spaces for each vehicle type.
     */
    public Area(String name, Map<VehicleType, Integer> maxCapacity) {
        this.name = name;
        this.maxCapacity = maxCapacity;
    }

    /**
     * This is the getter for name.
     * @return A String representing the name of the area.
     */
    public String getName() {
        return name;
    }

    /**
     * This is the getter for currentCapacity.
     * @return A map representing the number of parking places that are used for each type of vehicles.
     */
    public Map<VehicleType, Integer> getCurrentCapacity() {
        return currentCapacity;
    }

    /**
     * This is the getter for vehiclePlates.
     * @return A map mapping each vehicle in the area to its vehicle plate
     */
    public Map<String, Vehicle> getVehiclePlatesMap() {
        return vehiclePlatesMap;
    }

    /**
     * This is the getter fot maxCapacity.
     * @return A map representing the maximum capacity of the area for each type.
     * for each type of vehicles.
     */
    public Map<VehicleType, Integer> getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * This sets the maxCapacity for each type of vehicle.
     * @param values Represents the array of values that are the maximum capacity for
     *               a certain type of vehicle.
     */
    public void setMaxCapacity(int[] values) {
        maxCapacity.put(VehicleType.MOTORCYCLE, values[0]);
        maxCapacity.put(VehicleType.CAR, values[1]);
        maxCapacity.put(VehicleType.VAN, values[2]);
        maxCapacity.put(VehicleType.BUS, values[3]);
        maxCapacity.put(VehicleType.TRUCK, values[4]);
    }

    /**
     * This method initialises the typeCapacity with 0, so that the number of vehicles
     * will be increased easier.
     */
    private void initializeTypeCapacity() {
        for (VehicleType type : VehicleType.values()) {
            currentCapacity.put(type, 0);
        }
    }

    /**
     * This method computes the typeOccupancy based on the current time.
     * For each vehicle in vehiclePlatesMap it checks if it is still in the parking lot
     * at the current time. (Compares the lastExit of a car with the current time).
     * If it is, the capacity for that type of car is incremented.
     */
    private void computeTypeOccupancy() {
        LocalDateTime now = LocalDateTime.now();
        initializeTypeCapacity();

        for (Map.Entry<String, Vehicle> set : vehiclePlatesMap.entrySet()) {
            int capacity = currentCapacity.get(set.getValue().getType());

            if (set.getValue().getLastExit().compareTo(now) > 0) {
                currentCapacity.put(set.getValue().getType(), ++capacity);
            }
        }
    }

    /**
     * This method transforms a String variable intro a LocalDateTime variable.
     * @param str Represents the String that will be transformed into LocalDateTime.
     * @return The LocalDateTime representation of the String passed as argument.
     */
    private LocalDateTime getLocalDateTime(String str) {
        LocalDateTime date = null;

        try {
            date = LocalDateTime.parse(str, Main.formatter);
        } catch (Exception ignored) {
        }

        return date;
    }

    /**
     * This method constructs objects of type Vehicle using the data from a line
     * that was read from a file.
     * The data from the line is assigned to different variables. Based on line[1]
     * (representing the type of the car) we will know what kind of vehicle we should create.
     * We check if the vehicle was parked another time that day in that area.
     * We check if there are free parking spaces in the area. If there are and the vehicle was earlier in that day, we only
     * add the new parking intervals to its parkingIntervalsMap. Otherwise,
     * the vehicle, parking interval and the subscription are created and the lastExit of the car is set. The vehicles are added to
     * vehiclePlatesMap. If there are no free parking spaces in the area, an error will be thrown.
     * To keep the rate occupancy updated, it will be computed again.
     * @param line Represents the line that was read form a file
     * @throws InvalidFieldException It is thrown when a field is not valid. In this context
     * it means that the vehicle type read from the text file is not a valid one.
     * @throws MaximumCapacityReachedException It is thrown when the maximum capacity of the area
     * for a certain type of vehicle is reached.
     * @throws InvalidParkingIntervalException It is thrown when the duration of
     * a parking interval is negative, so the parking interval was not introduced correctly.
     * @throws InvalidVehicleTypeException It is thrown when an invalid type is passed to the method.
     */
    public void addVehicle(String[] line) throws InvalidFieldException, MaximumCapacityReachedException, InvalidParkingIntervalException, InvalidVehicleTypeException {
        String carType = line[1];
        String licensePlate = line[2];
        LocalDateTime boughtTime = getLocalDateTime(line[3]);
        LocalDateTime entry = getLocalDateTime(line[4]);
        LocalDateTime exit = getLocalDateTime(line[5]);

        Subscription subscription = new Subscription(boughtTime);
        if (boughtTime == null) {
            subscription = null;
        }

        Vehicle existingVehicle = vehiclePlatesMap.get(licensePlate);
        Vehicle vehicle;

        if (existingVehicle == null) {
            vehicle = Vehicle.getVehicleObject(carType);
            vehicle.setSubscription(subscription);
        } else {
            vehicle = existingVehicle;
        }

        vehicle.setLastExit(exit);
        VehicleType type = vehicle.getType();
        if (this.maxCapacity.get(type).equals(this.currentCapacity.get(type))) {
            throw new MaximumCapacityReachedException("Maximum capacity for " + type + " is " + this.maxCapacity.get(type));
        }

        int parkingDays = exit.getDayOfYear() - entry.getDayOfYear();
        String date = String.valueOf(entry.toLocalDate());
        ParkingInterval parkingInterval = new ParkingInterval(entry, exit);
        LocalDateTime dateTime = entry;

        vehiclePlatesMap.putIfAbsent(licensePlate, vehicle);
        if (parkingDays >= 0) {
            vehiclePlatesMap.get(licensePlate).getParkingIntervals().putIfAbsent(date, new ArrayList<>());
            vehiclePlatesMap.get(licensePlate).getParkingIntervals().get(date).add(parkingInterval);
        } else {
            throw new InvalidParkingIntervalException("The parking interval is not a valid one.");
        }

        vehicle.addParkingEntrances();

        if (vehicle.getParkingEntrances() % 10 == 0) {
            parkingInterval.setDiscount(true);
        }

        computeTypeOccupancy();
    }

    /**
     * This methods overrides the toString() method.
     * @return The the representation of an object of type ParkingLot.
     */
    @Override
    public String toString() {
        return "Area{" +
                "name='" + name + '\'' +
                ", totalNumberOfParkingSpaces=" + maxCapacity +
                ", typeOccupancy=" + currentCapacity +
                ", vehiclePlatesMap=" + vehiclePlatesMap +
                '}';
    }
}
