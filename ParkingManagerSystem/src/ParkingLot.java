package uvt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The objects of this class represent a parking lot.
 */
public class ParkingLot {

    /**
     * Represents the name of the parking lot.
     */
    private final String name;

    /**
     * Represents the number of entries of a parking lot.
     */
    private final int noOfEntries;

    /**
     * Represents the list of areas of the parking lot.
     */
    private final List<uvt.Area> areaList = new ArrayList<>();

    /**
     * This is the constructor for objects of class ParkingLot.
     * @param name Represents the name of the parking lot.
     * @param noOfEntries Represents the number of entries the parking lot has.
     */
    public ParkingLot(String name, int noOfEntries) {
        this.name = name;
        this.noOfEntries = noOfEntries;
    }

    /**
     * This is the getter fot the areaList
     * @return The list of areas.
     */
    public List<uvt.Area> getAreaList() {
        return areaList;
    }

    /**
     * This method adds an area to the parking lot.
     * @param area Represents the area that will be added to the parking lot.
     */
    public void addArea(uvt.Area area) {
        areaList.add(area);
    }

    /**
     * This is the getter for name.
     * @return A string representing the name of the parking lot.
     */
    public String getName() {
        return name;
    }

    /**
     * This is the getter for noOfEntries.
     * @return An integer representing the number of entries of the parking lot.
     */
    public int getNoOfEntries() {
        return noOfEntries;
    }

    /**
     * This method reads from a file a String[], constructs the object of type
     * Area and then adds the area to the parking lot.
     * @param line Represents the line that is read from a file. It contains the data needed to create the area.
     */
    public void addArea(String[] line) {
        uvt.Area a = new uvt.Area(line[1]);
        int[] arrayOfValues = new int[5];
        for (int i = 2; i < line.length; i++) {
            arrayOfValues[i - 2] = Integer.parseInt(line[i]);
        }

        a.setMaxCapacity(arrayOfValues);
        areaList.add(a);
    }

    /**
     * This method computes the occupancy rate of the parking lot for each type of
     * vehicles. It loops through values of the enum VehicleTypes and for each of
     * them, it computes the total maximum number of parking places and the total
     * number of occupied places at the moment and computes the rate of occupancy
     * for that type of vehicles. Then it prints the values.
     */
    public void printOccupancyRate() {
        System.out.println("Occupancy rate for " + this.name + " is:");

        for (VehicleType type : VehicleType.values()) {
            double total = 0;
            double occupied = 0;
            for (uvt.Area area : areaList) {
                total += area.getMaxCapacity().get(type);
                if (area.getCurrentCapacity().get(type) != null) {
                    occupied += area.getCurrentCapacity().get(type);
                }
            }

            double percent = (occupied * 100) / total;
            System.out.println(" - " + type.toString().toLowerCase() + ": " + percent + "%");
        }
    }

    /** This method computes the total gain of the parking lot in a day. It loops
     * through the ares in the areaList and for each area it computes the
     * gain and adds it to the total gain of the parking lot.
     * @param date Represents the date in which the total gain is computed.
     */
    public void printParkingLotGain(String date) {
        double total = 0;

        for (uvt.Area area : areaList) {
            for (Map.Entry<String, Vehicle> vehicle : area.getVehiclePlatesMap().entrySet()) {
                total += vehicle.getValue().parkingCost(date);
            }
        }

        System.out.println(name + " gained " + total + " on " + date + '.');
    }

    /**
     * This methods overrides the toString() method.
     * @return The the representation of an object of type ParkingLot.
     */
    @Override
    public String toString() {
        return "ParkingLot{" +
                "name='" + name + '\'' +
                ", noOfEntries=" + noOfEntries +
                ", areaList=" + areaList +
                '}';
    }
}
