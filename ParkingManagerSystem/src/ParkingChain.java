package uvt;

import uvt.Exceptions.*;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;

import static uvt.Main.formatter;

/**
 * The objects of this class represent the Parking Chain.
 */
public class ParkingChain {
    /**
     * Represent the list of parking lots.
     */
    List<ParkingLot> parkingLotList = new ArrayList<>();

    /**
     * This is a constructor fot objects of type ParkingChain.
     */
    public ParkingChain() { }

    /**
     * This method adds a parking lot to the parking chain.
     * @param parkingLot Represents the parking lot that will be added to the parking chain.
     */
    public void addParkingLot(ParkingLot parkingLot) {
        parkingLotList.add(parkingLot);
    }

    /**
     * This method reads the data needed to create the parking chain and its components from a file.
     * @param file Represents the file from which the data is read.
     */
    public void readParkingChainFile(String file) {
        File f = new File(file);

        try {
            Scanner sc = new Scanner(f);

            while (sc.hasNext()) {
                String[] line = sc.nextLine().split(",");
                if (line[0].equals("parkingLot")) {
                    addParkingLotFromLine(line);
                } else if (line[0].equals("area")) {
                    addAreaFromLine(line);
                } else if (line[0].equals("vehicle")) {
                    addVehicleFromLine(line);
                } else {
                    throw new InvalidFieldException("The introduced field is not a valid one");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a parking log using data from a line that was read from a file and adds it to the parking chain.
     * @param line Represents the line that was read from the file. It contains the data needed to construct the parking
     *             lot.
     * @throws InvalidLineLengthException It is thrown when the line contains more data fields that it should.
     */
    private void addParkingLotFromLine(String[] line) throws InvalidLineLengthException {
        final int rowFields = 3;

        if (line.length != rowFields) {
            throw new InvalidLineLengthException("Invalid number of fields (" + line.length + " instead of " + rowFields + ") for type " + line[0] + ".");
        }

        parkingLotList.add(new ParkingLot(line[1], Integer.parseInt(line[2])));
    }

    /**
     * This method creates an area using data from a line read from a file and adds it to the parking lot.
     * @param line Represents the line read form a file that contains the data needed to create an area.
     * @throws InvalidLineLengthException It is thrown when the line contains more data fields that it should.
     */
    private void addAreaFromLine(String[] line) throws InvalidLineLengthException {
        final int rowFields = 7;

        if (line.length != rowFields) {
            throw new InvalidLineLengthException("Invalid number of fields (" + line.length + " instead of " + rowFields + ") for type " + line[0] + ".");
        }
        parkingLotList.get(parkingLotList.size() - 1).addArea(line);
    }

    /**
     * This method creates a vehicle using data from a line read from a file and adds it to the are.a
     * @param line Represents the line read form a file that contains the data needed to create an area.
     * @throws InvalidLineLengthException      It is thrown when the line contains more data fields that it should.
     * @throws MaximumCapacityReachedException It is thrown when the maximum capacity of the area for a vehicle type is reached.
     * @throws InvalidParkingIntervalException It is thrown when the duration of an interval in negative, so the interval in not
     *                                         correctly formed.
     * @throws InvalidFieldException           It is thrown if an invalid type for a field will is passed to a method.
     * @throws InvalidVehicleTypeException     It is thrown if an invalid vehicle type will is passed to a method.
     */
    private void addVehicleFromLine(String[] line) throws InvalidLineLengthException, MaximumCapacityReachedException, InvalidParkingIntervalException, InvalidFieldException, InvalidVehicleTypeException {
        final int rowFields = 6;

        if (line.length != rowFields) {
            throw new InvalidLineLengthException("Invalid number of fields (" + line.length + " instead of " + rowFields + ") for type " + line[0] + ".");
        }

        List<uvt.Area> areaList = parkingLotList.get(parkingLotList.size() - 1).getAreaList();
        int lastAreaListIdx = areaList.size() - 1;
        areaList.get(lastAreaListIdx).addVehicle(line);
    }

    /**
     * This method is used when adding an area from keyboard. It adds it to the
     * areaList of the parking lot passed as parameter.
     * @param parkingLotName Represents the name of the parking lot in which the new area will be added.
     * @param area Represents the area that will be added to a parking lot.
     * @throws InvalidParkingLotNameException It is thrown when there is no parking lot with the name passed as parameter.
     */
    public void addArea(String parkingLotName, uvt.Area area) throws InvalidParkingLotNameException {
        int parkingLotIdx = getParkingLotIndex(parkingLotName);

        if (parkingLotIdx == -1) {
            throw new InvalidParkingLotNameException("Parking lot '" + parkingLotName + "' does not exist!");
        }

        parkingLotList.get(parkingLotIdx).addArea(area);
    }


    /**
     * This method is used when adding a vehicle from keyboard. It creates the parking interval and adds it to the parkingIntervals
     * map and adds the car to the area in the parking lot passed as parameter.
     * @param parkingLotName Represents the parking lot name where is the area in which we want to add the car.
     * @param areaName Represents the name of the area in which the car will be added.
     * @param licensePlate Represents the licencePlate of the car that will be added.
     * @param vehicle Represents the car that will pe added.
     * @param entryTime Represents the time when the car entries the parking lot.
     * @param exitTime Represents the time when the car exits the parking lot.
     * @throws InvalidParkingLotNameException It is thrown when there is no parking lot with the name passed as parameter.
     * @throws InvalidAreaNameException It is thrown when there is no are with the name passed as parameter.
     */
    public void addVehicle(String parkingLotName, String areaName, String licensePlate, Vehicle vehicle, LocalDateTime entryTime, LocalDateTime exitTime) throws InvalidParkingLotNameException, InvalidAreaNameException {
        int parkingLotIdx = getParkingLotIndex(parkingLotName);

        if (parkingLotIdx == -1) {
            throw new InvalidParkingLotNameException("Parking lot '" + parkingLotName + "' does not exist!");
        }

        ParkingLot parkingLot = parkingLotList.get(parkingLotIdx);
        int areaIdx = getAreaIndex(parkingLot, areaName);

        if (areaIdx == -1) {
            throw new InvalidAreaNameException("Area '" + areaName + "' does not exist!");
        }

        uvt.Area area = parkingLot.getAreaList().get(areaIdx);

        if (area.getVehiclePlatesMap().containsKey(licensePlate)) {
            Vehicle existingVehicle = area.getVehiclePlatesMap().get(licensePlate);
            existingVehicle.addParkingInterval(entryTime, exitTime);
        } else {
            area.getVehiclePlatesMap().put(licensePlate, vehicle);
            vehicle.addParkingInterval(entryTime, exitTime);
        }
    }

    /**
     * This method returns the index of a parking lot that has a specific name in the parking lot list of a parking chain.
     * @param name Represents the name of the parking lot that is searched in the parking chain.
     * @return An integer representing the index at which the parking lot is found in the parking lot list of a parking chain.
     */
    private int getParkingLotIndex(String name) {
        return IntStream.range(0, parkingLotList.size())
                .filter(i -> parkingLotList.get(i).getName().equals(name))
                .findFirst().orElse(-1);
    }

    /**
     * his method returns the index of a area that has a specific name in the area list of a parking lot.
     * @param parkingLot Represents the name of the parking lot in which the area is searched.
     * @param name Represents the name of area that is searched.
     * @return An integer representing the index at which the area is found in the area list of a parking lot.
     */
    private int getAreaIndex(ParkingLot parkingLot, String name) {
        return IntStream.range(0, parkingLot.getAreaList().size())
                .filter(i -> parkingLot.getAreaList().get(i).getName().equals(name))
                .findFirst().orElse(-1);
    }

    /**
     * This method overwrites the new data of the parking chain in the initial file.
     * @param file Represents the name of the file in which the new data of the parking chain will be written.
     * @throws IOException It is thrown when the file is not found.
     */
    public void writeParkingChainFile(String file) throws IOException {
        FileWriter output = new FileWriter(file);

        for (ParkingLot parkingLot : parkingLotList) {
            output.write("parkingLot," + parkingLot.getName() + "," + parkingLot.getNoOfEntries() + '\n');
            writeAreas(output, parkingLot.getAreaList());
        }
        output.close();
    }

    /**
     * This method writes the areas of the parking lots in the initial file.
     * @param output Represents the name of the file in which the new data will be written in the file.
     * @param areas Represents the list of areas of the parking lot that will be written in the file.
     * @throws IOException It is thrown when the file is not found.
     */
    private void writeAreas(FileWriter output, List<uvt.Area> areas) throws IOException {
        for (uvt.Area area : areas) {
            StringBuilder maximumParkingSpaces = new StringBuilder();

            for (int value : area.getMaxCapacity().values()) {
                maximumParkingSpaces.append(value).append(",");
            }

            output.write("area," + area.getName() + "," + maximumParkingSpaces.substring(0, maximumParkingSpaces.length() - 1) + '\n');
            writeVehicles(output, area.getVehiclePlatesMap());
        }
    }

    /**
     * This method writes the vehicles of an area in the initial file.
     * @param output Represents the name of the file in which the new data will be written in the file.
     * @param licensePlates Represents the map of licence plates an vehicle of an area that will be written in the file.
     * @throws IOException It is thrown when the file is not found.
     */
    private void writeVehicles(FileWriter output, Map<String, Vehicle> licensePlates) throws IOException {
        for (Map.Entry<String, Vehicle> map : licensePlates.entrySet()) {
            Vehicle vehicle = map.getValue();
            Subscription subscription = vehicle.getSubscription();
            StringBuilder vehicleInfo = new StringBuilder("vehicle,");

            vehicleInfo.append(vehicle.getType().toString().toLowerCase()).append(",").append(map.getKey()).append(",");

            if (subscription == null) {
                vehicleInfo.append("null");
            } else {
                vehicleInfo.append(subscription.getBoughtTime().format(formatter));
            }

            writeParkingIntervals(output, vehicle.getParkingIntervals(), vehicleInfo);
        }
    }

    /**
     * This method writes the parking intervals of a vehicle in the initial file.
     * @param output Represents the name of the file in which the new data will be written in the file.
     * @param parkingIntervalMap Represents the parkingIntervals map of a car.
     * @param vehicleInfo Represents the data about the car, except from the parking interval.
     * @throws IOException It is thrown when the file is not found.
     */
    private void writeParkingIntervals(FileWriter output, Map<String, List<ParkingInterval>> parkingIntervalMap, StringBuilder vehicleInfo) throws IOException {
        for (List<ParkingInterval> parkingIntervals : parkingIntervalMap.values()) {
            for (ParkingInterval parkingInterval : parkingIntervals) {
                output.write(vehicleInfo + "," + parkingInterval.getEntry().format(formatter) + "," + parkingInterval.getExit().format(formatter) + "\n");
            }
        }
    }

    /**
     * This is the method that will be called in the Main class to get the occupancy rate.
     * @param parkingLotName Represents the name of the parking lot fo which the occupancy rate is printed.
     */
    public void printOccupancy(String parkingLotName) {
        int parkingLotIdx = getParkingLotIndex(parkingLotName);

        if (parkingLotIdx == -1) {
            System.out.println("Parking lot '" + parkingLotName + "' does not exist!");
        } else {
            printParkingLotOccupancy(parkingLotList.get(parkingLotIdx));
        }
    }

    /**
     * This method is used to print the occupancy rate of all the parking lots in the parking chain.
     */
    public void printOccupancy() {
        printParkingLotOccupancy(null);
    }

    /**
     * This private method computes the occupancy rate for a certain parking lot.(If it is null it means that it will
     * compute the gain for each parking lot in the parking chain.)
     *
     * @param parkingLot Represents the parking lot for which the occupancy rate is computed.
     */
    private void printParkingLotOccupancy(ParkingLot parkingLot) {
        if (parkingLot != null) {
            parkingLot.printOccupancyRate();
        } else {
            for (ParkingLot pl : parkingLotList) {
                pl.printOccupancyRate();
            }
        }
    }

    /**
     * This method wil be called in the Main class in order to compute the gain of all parking lots in the parking chain.
     *
     * @param date Represents the date on which the gain is computed.
     */
    public void printGain(String date) {
        printGainForParkingLot(null, date);
    }

    /**
     * This method wil be called in the Main class in order to compute the gain of only one parking lot form the parking chain.
     * @param parkingLotName The name of the parking lot for which we compute the gain.
     * @param date Represents the date on which the gain is computed.
     */
    public void printGain(String parkingLotName, String date) {
        int parkingLotIdx = getParkingLotIndex(parkingLotName);

        if (parkingLotIdx == -1) {
            System.out.println("Parking lot '" + parkingLotName + "' does not exist!");
        } else {
            printGainForParkingLot(parkingLotList.get(parkingLotIdx), date);
        }
    }

    /**
     * This private method computes the gain of one day of a parking lot in the parking chain.
     * @param parkingLot The parking lot for which we compute the gain. (If it is null it means that it will
     *             compute the gain for each parking lot in the parking chain.)
     * @param date Represents the date on which the gain is computed.
     */
    private void printGainForParkingLot(ParkingLot parkingLot, String date) {
        if (parkingLot != null) {
            parkingLot.printParkingLotGain(date);
        } else {
            for (ParkingLot pl : parkingLotList) {
                pl.printParkingLotGain(date);
            }
        }
    }

    @Override
    public String toString() {
        return "ParkingChain{" +
                "parkingLotList=" + parkingLotList +
                '}';
    }
}
