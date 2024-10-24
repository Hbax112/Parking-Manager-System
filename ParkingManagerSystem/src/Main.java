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
 * This is the Main class. The operations made on the parking chain are done here.
 */
public class Main {

    /**
     * Represents the pattern used to create a LocalDateTime formatter.
     */
    public static final String formatterPattern = "yyyy-MM-dd HH:mm";

    /**
     * Represents the formatter used to create a LocalDateTime value from a String value.
     */
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatterPattern);

    /**
     * This is the main method. It provides the interaction with the user. The parking chain is configured there and also,
     * the object that overwrites the input file is created here too.
     * @param args Represents the arguments list. There will be the paths to the input file.
     */
    public static void main(String[] args) {
        ParkingChain pc = new ParkingChain();
        try {
            pc.readParkingChainFile(args[0]);
            uvt.KeyboardInputParser keyboardInputParser = new uvt.KeyboardInputParser();
            keyboardInputParser.processInputEvents(pc);

            pc.writeParkingChainFile(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
