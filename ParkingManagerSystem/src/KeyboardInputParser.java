package uvt;

import uvt.Exceptions.InvalidAreaNameException;
import uvt.Exceptions.InvalidParkingLotNameException;
import uvt.Exceptions.InvalidVehicleTypeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This objects of this class parse the input got from the user (from keyboard).
 */
public class KeyboardInputParser {


    /**
     * Represents the Scanner object used to get data from keyboard.
     */
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Represents the options the user will have when running the code.
     */
    private static final String[] options = {
            "1. Add parking lot",
            "2. Add area",
            "3. Add vehicle",
            "4. Print occupancy",
            "5. Print gain",
            "6. Exit"
    };

    /**
     * This is the constructor of the class KeyboardInputParser. It receives no arguments.
     */
    public KeyboardInputParser() { }

    /**
     * This method reads an Integer value from keyboard and returns it.
     * @param lower_bound Represents the smallest value the integer can get.
     * @param upper_bound Represents the biggest value the integer can get.
     * @param optionsMessage Represents the options from which the user can choose what to do.
     * @return The value of the Integer that was read from keyboard.
     */
    private static int readInteger(int lower_bound, int upper_bound, String optionsMessage) {
        String input;
        int integer = -1;
        boolean validInput;

        do {
            validInput = true;
            input = sc.nextLine();

            try {
                integer = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: '" + e.getMessage() + "'.\n" + optionsMessage);
                validInput = false;
            }

            if (integer < lower_bound || integer > upper_bound) {
                System.out.println("Invalid input: '" + input + "'.\n" + optionsMessage);
            }
        } while (!validInput);

        return integer;
    }

    /**
     * This method reads what option the user choose.
     * @return The Integer value representing that option.
     */
    private static int readOption() {
        String optionMessage = String.join("\n", options) + "\nPlease select an option:";
        System.out.println(optionMessage);

        return readInteger(1, options.length, optionMessage);
    }

    /**
     * This method reads from keyboard the parking lot name.
     * @return The name of the parking lot.
     */
    private static String getParkingLotName() {
        System.out.println("Enter parking lot name:");
        return sc.nextLine();
    }

    /**
     * This method reads form keyboard the data needed to create a parking lot and creates it.
     * @return The parking lot created with the data from keyboard.
     */
    private static ParkingLot getParkingLot() {
        String entrancesMessage = "Enter parking lot number of entrances";

        String name = getParkingLotName();
        System.out.println(entrancesMessage + ":");
        int entrances = readInteger(1, 50, entrancesMessage + " (number between 1 and 50): ");

        System.out.println(name + " " + String.valueOf(entrances));

        return new ParkingLot(name, entrances);
    }

    /**
     * This method reads from keyboard the area name.
     * @return The name of the area.
     */
    private static String getAreaName() {
        System.out.println("Enter area name:");
        return sc.nextLine();
    }

    /**
     * This method reads from keyboard the data needed to set the maximum capacity of an area and assigns it to each
     * VehicleType in a map.
     * @return The map containing the maximum capacity for each VehicleType.
     */
    private static Map<VehicleType, Integer> getAreaMaximumCapacity() {
        Map<VehicleType, Integer> maxCapacity = new HashMap<>();

        for (VehicleType vehicleType : VehicleType.values()) {
            String maxParkingPlacesMessage = "Enter maximum number of parking places for " + vehicleType.toString().toLowerCase();
            System.out.println(maxParkingPlacesMessage + ":");
            int value = readInteger(0, 50, maxParkingPlacesMessage + " (number between 1 and 50): ");
            maxCapacity.put(vehicleType, value);
        }

        return maxCapacity;
    }

    /**
     * This method creates an area using the functions getAreaName() and getAreaMaximumCapacity() that process the input
     * got from keyboard.
     * @return The area created.
     */
    private static uvt.Area getArea() {
        String name = getAreaName();
        Map<VehicleType, Integer> maxCapacity = getAreaMaximumCapacity();

        return new uvt.Area(name, maxCapacity);
    }

    /**
     * This method reads the answers Yes or No that the user gives.
     * @param message Represents the options from which the user can choose what to do.
     * @return The string value of the answer got from the user.
     */
    private static String readYN(String message) {
        boolean validInput;
        String input;
        System.out.println(message);

        do {
            validInput = true;
            input = sc.nextLine();

            if (!input.equals("y") && !input.equals("n")) {
                validInput = false;
                System.out.println("Invalid input: '" + input + "'.");
                System.out.println(message);
            }
        } while (!validInput);

        return input;
    }

    /**
     * This method asks the user if he/sha wants to get a subscription and creates it.
     * @return The subscription if it is demanded, null otherwise.
     */
    private static Subscription getSubscription() {
        String message = "Do you want to buy subscription (y/n)?";
        String input = readYN(message);

        if (input.equals("y")) {
            return new Subscription(LocalDateTime.now());
        }

        return null;
    }

    /**
     * This method reads the exit of an interval from keyboard and validates it.
     * @return A LocalDateTime representing the exit time.
     */
    private static LocalDateTime getExitTime() {
        LocalDateTime exitTime = null;
        boolean validInput;
        String input;

        do {
            System.out.println("Enter exit time (" + Main.formatterPattern + "):");
            validInput = true;
            input = sc.nextLine();

            try {
                exitTime = LocalDateTime.parse(input, Main.formatter);
            } catch (DateTimeParseException e) {
                validInput = false;
                System.out.println("Invalid input: '" + input + "'.");
            }

            if (validInput && exitTime.compareTo(LocalDateTime.now()) <= 0) {
                validInput = false;
                System.out.println("Invalid input: '" + input + "'. Exit date should not be in the past!");
            }
        } while (!validInput);

        return exitTime;
    }

    /**
     * This method reads the vehicle type from keyboard and validates it.
     * @return The type of a vehicle.
     */
    private static VehicleType getVehicleType() {
        VehicleType vehicleType = null;
        boolean validInput;
        String input;
        String[] options = new String[VehicleType.values().length];
        int idx = 0;

        for (VehicleType v : VehicleType.values()) {
            options[idx] = v.toString().toLowerCase();
            idx++;
        }

        do {
            System.out.println("Enter vehicle type (" + String.join("/", options) + "):");
            validInput = true;
            input = sc.nextLine();

            try {
                vehicleType = Vehicle.getVehicleType(input);
            } catch (InvalidVehicleTypeException e) {
                validInput = false;
                System.out.println(e.getMessage());
            }
        } while (!validInput);

        return vehicleType;
    }

    /**
     * This method creates a vehicle (using methods that process data given from keyboard) and assigns it the subscription demanded.
     * @return The vehicle created with data from keyboard.
     */
    private static Vehicle getVehicle() {
        VehicleType vehicleType = getVehicleType();
        Subscription subscription = getSubscription();

        Vehicle vehicle = null;
        try {
            vehicle = Vehicle.getVehicleObject(vehicleType.toString().toLowerCase());
            vehicle.setSubscription(subscription);
        } catch (Exception ignored) {
        }
        return vehicle;
    }

    /**
     * This method prints the gain of a parking lot (or all parking lots, depending on what the users asks for) in
     * a specific day (given by the user from keyboard).
     * @param parkingChain Represents the parking chain containing the parking lots for which the gain is computed.
     */
    private static void printPrintGain(ParkingChain parkingChain) {
        String dateFormat = "yyyy-MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        String message = "Please enter the date for which you want the gain (" + dateFormat + "):";
        LocalDate date = null;
        boolean validInput;
        String input;

        do {
            System.out.println(message);
            validInput = true;
            input = sc.nextLine();

            try {
                date = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                validInput = false;
                System.out.println("Invalid input: '" + input + "'.");
            }
        } while (!validInput);

        message = "Do you want to print the gain for all parking lots (y/n)?";
        input = readYN(message);

        if (input.equals("y")) {
            parkingChain.printGain(date.toString());
        } else {
            System.out.println("Enter parking lot name:");
            String parkingLotName = sc.nextLine();

            parkingChain.printOccupancy(parkingLotName);
        }
    }

    /**
     * This method prints the occupancy rate of a parking lot (or all parking lots, depending on what the users asks for) in
     * a specific day (given by the user from keyboard).
     * @param parkingChain Represents the parking chain containing the parking lots for which the occupancy rate is computed.
     */
    private static void readPrintOccupancy(ParkingChain parkingChain) {
        String message = "Do you want to print the occupancy for all parking lots (y/n)?";
        String input = readYN(message);

        if (input.equals("y")) {
            parkingChain.printOccupancy();
        } else {
            System.out.println("Enter parking lot name:");
            String parkingLotName = sc.nextLine();

            parkingChain.printOccupancy(parkingLotName);
        }
    }

    /**
     * Thi method takes input from keyboard and processes it. The data is added to the parking chain passed as parameter.
     * @param pc Represents the parking chain in which the objects built from the data given by the user are added.
     */
    public void processInputEvents(ParkingChain pc) {
        int option;

        do {
            option = readOption();
            switch (option) {
                case 1:
                    ParkingLot parkingLot = getParkingLot();
                    pc.addParkingLot(parkingLot);
                    System.out.println("Parking lot '" + parkingLot.getName() + "' was added!");
                    break;
                case 2:
                    try {
                        String parkingLotName = getParkingLotName();
                        uvt.Area area = getArea();
                        pc.addArea(parkingLotName, area);
                        System.out.println("Area '" + area.getName() + "' was added in parking lot '" + parkingLotName + "'!");
                    } catch (InvalidParkingLotNameException e) {
                        System.out.println(e.getMessage());
                        option = readOption();
                    }
                    break;
                case 3:
                    String parkingLotName = getParkingLotName();
                    String areaName = getAreaName();

                    System.out.println("Enter license plate:");
                    String licensePlate = sc.nextLine();

                    Vehicle vehicle = getVehicle();
                    LocalDateTime entryTime = LocalDateTime.now();
                    LocalDateTime exitTime = getExitTime();

                    try {
                        pc.addVehicle(parkingLotName, areaName, licensePlate, vehicle, entryTime, exitTime);
                        System.out.println("Vehicle with license plate '" + licensePlate + "' was added in area '" + areaName + "' from parking lot '" + parkingLotName + "'!");
                    } catch (InvalidAreaNameException | InvalidParkingLotNameException e) {
                        System.out.println(e.getMessage());
                        option = readOption();
                    }
                    break;
                case 4:
                    readPrintOccupancy(pc);
                    break;
                case 5:
                    printPrintGain(pc);
                    break;
            }
        } while (option != options.length);

    }
}
