import java.io.File;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        RideSharingSystem system = new RideSharingSystem();
        int riders = system.loadRidersFromCSV(dataFile("riders_100.csv"));
        int drivers = system.loadDriversFromCSV(dataFile("drivers_30.csv"));
        int rides = system.loadRidesFromCSV(dataFile("rides_40.csv"));
        System.out.println("Loaded riders: " + riders);
        System.out.println("Loaded drivers: " + drivers);
        System.out.println("Loaded rides: " + rides);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            int option = readInt(scanner, "Choose an option: ");
            if (option == 0) break;
            try {
                switch (option) {
                    case 1:
                        printResults(system.getAllRiders());
                        break;
                    case 2:
                        int riderId = readInt(scanner, "Rider ID: ");
                        IRider rider = system.searchRiderById(riderId);
                        if (rider == null) {
                            System.out.println("No rider found.");
                        } else {
                            System.out.println(rider);
                        }
                        System.out.println("Key comparisons: " + system.nbKeyCompRiderById(riderId));
                        break;
                    case 3:
                        printResults(system.searchRidersByHomeCity(readLine(scanner, "Home city: ")));
                        break;
                    case 4:
                        printResults(system.getAllRidesAlphabetically());
                        break;
                    case 5:
                        String personType = readLine(scanner, "Person type (rider/driver): ").toLowerCase(Locale.ROOT);
                        int id = readInt(scanner, "ID: ");
                        IPerson person = personType.equals("driver")
                                ? system.searchDriverById(id) : system.searchRiderById(id);
                        if (person == null) {
                            System.out.println("No person found.");
                        } else {
                            printResults(person.getSchedule().inOrder());
                        }
                        break;
                    case 6:
                        printIndexStats(system);
                        break;
                    case 7:
                        addRider(scanner, system);
                        break;
                    case 8:
                        IDateTime from = readDateTime(scanner, "From (MM/DD/YYYY HH:MM): ");
                        IDateTime to = readDateTime(scanner, "To (MM/DD/YYYY HH:MM): ");
                        printResults(system.searchRidesByPickupTimeRange(from, to));
                        break;
                    case 9:
                        ITimeSlot slot = readTimeSlot(scanner);
                        VehicleType type = readVehicleType(scanner);
                        printResults(system.findAvailableDrivers(slot, type));
                        break;
                    case 10:
                        addRide(scanner, system);
                        break;
                    case 11:
                        int rideId = readInt(scanner, "Ride ID: ");
                        System.out.println(system.cancelRide(rideId)
                                ? "Ride canceled successfully." : "Failed to cancel ride.");
                        break;
                    case 12:
                        int removedId = readInt(scanner, "Rider ID: ");
                        System.out.println(system.removeRider(removedId)
                                ? "Rider removed successfully." : "Failed to remove rider.");
                        break;
                    default:
                        System.out.println("Invalid option.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Failed: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("1. List all riders.");
        System.out.println("2. Search for a rider by ID.");
        System.out.println("3. Search for riders by home city.");
        System.out.println("4. List all rides alphabetically by pickup location.");
        System.out.println("5. Show the schedule of a rider or driver.");
        System.out.println("6. Show index sizes and heights.");
        System.out.println("7. Add a rider.");
        System.out.println("8. List rides in a pickup-time range.");
        System.out.println("9. Find available drivers.");
        System.out.println("10. Add a ride.");
        System.out.println("11. Cancel a ride.");
        System.out.println("12. Remove a rider.");
        System.out.println("0. Exit the program.");
    }

    private static void printIndexStats(RideSharingSystem system) {
        for (IndexName index : IndexName.values()) {
            int n = system.indexSize(index);
            int minimum = (int) Math.ceil(Math.log(n + 1) / Math.log(2));
            System.out.println(index + ": size=" + n + ", height=" + system.indexHeight(index)
                    + ", minimumHeight=" + minimum);
        }
    }

    private static void addRider(Scanner scanner, RideSharingSystem system) {
        IRider rider = new Rider(
                readInt(scanner, "Rider ID: "),
                readLine(scanner, "Name: "),
                readLine(scanner, "Email: "),
                readLine(scanner, "Phone number (10 digits): "),
                readLine(scanner, "Home city: "));
        System.out.println(system.addRider(rider) ? "Rider added successfully." : "Failed to add rider.");
    }

    private static void addRide(Scanner scanner, RideSharingSystem system) {
        String type = readLine(scanner, "Ride type (private/shared): ").toLowerCase(Locale.ROOT);
        String pickup = readLine(scanner, "Pickup location: ");
        String dropoff = readLine(scanner, "Dropoff location: ");
        ITimeSlot slot = readTimeSlot(scanner);
        int driverId = readInt(scanner, "Driver ID: ");
        int rideId;
        if (type.equals("private")) {
            rideId = system.schedulePrivateRide(pickup, dropoff, slot,
                    readInt(scanner, "Rider ID: "), driverId);
        } else if (type.equals("shared")) {
            rideId = system.scheduleSharedRide(pickup, dropoff, slot,
                    readRiderIds(scanner), driverId);
        } else {
            System.out.println("Failed to add ride.");
            return;
        }
        System.out.println(rideId == -1
                ? "Failed to add ride." : "Ride added successfully. Ride ID: " + rideId);
    }

    private static int[] readRiderIds(Scanner scanner) {
        String[] parts = readLine(scanner, "Rider IDs (comma-separated): ").split("[,\\s]+");
        int[] ids = new int[parts.length];
        for (int i = 0; i < parts.length; i++) ids[i] = Integer.parseInt(parts[i].trim());
        return ids;
    }

    private static ITimeSlot readTimeSlot(Scanner scanner) {
        IDateTime start = readDateTime(scanner, "Pickup date and time (MM/DD/YYYY HH:MM): ");
        IDateTime end = readDateTime(scanner, "Dropoff date and time (MM/DD/YYYY HH:MM): ");
        return new TimeSlot(start, end);
    }

    private static IDateTime readDateTime(Scanner scanner, String prompt) {
        return RideSharingSystem.parseDateTime(readLine(scanner, prompt));
    }

    private static VehicleType readVehicleType(Scanner scanner) {
        while (true) {
            try {
                return VehicleType.valueOf(readLine(scanner,
                        "Vehicle type " + java.util.Arrays.toString(VehicleType.values()) + ": ")
                        .toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                System.out.println("Enter one of the listed vehicle types.");
            }
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            String value = readLine(scanner, prompt);
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid whole number.");
            }
        }
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static <T> void printResults(LinkedList<T> results) {
        if (results == null || results.empty()) {
            System.out.println("No results found.");
            return;
        }
        results.findfirst();
        while (true) {
            System.out.println(results.retrieve().toString());
            if (results.last()) break;
            results.findnext();
        }
    }

    private static String dataFile(String name) {
        File current = new File(name);
        if (current.isFile()) return current.getPath();
        File parent = new File("..", name);
        if (parent.isFile()) return parent.getPath();
        return name;
    }
}
