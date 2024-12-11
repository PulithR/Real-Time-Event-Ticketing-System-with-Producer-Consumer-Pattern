import com.google.gson.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final System.Logger LOGGER = System.getLogger("TicketSimulationLogger");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String LOG_FILE = "ticket_simulation_log.txt";

    public static void main(String[] args) {

        int choice;

        do {
            System.out.println("\nChoose an option:");
            System.out.println("1. Enter a new configuration");
            System.out.println("2. Select a previous configuration");
            System.out.println("3. Exit");
            choice = validate("Enter your choice: ", 3);

            switch (choice) {
                case 1:
                    newSimulation();
                    break;
                case 2:
                    Configuration selectedConfig = selectConfiguration(loadConfigurations());

                    if (selectedConfig == null) {
                        logToFile("Error while selecting a configuration");
                        break;
                    }
                    logToFile("Starting Simulation");
                    try {
                        runSimulation(selectedConfig);
                    } catch (InterruptedException e) {
                        logToFile("Interrupted while threading");
                    }
                    break;
                case 3:
                    logToFile("Exiting...");
                    break;
                default:
                    logToFile("Invalid choice. Please try again.");
            }

        } while (choice != 3);
    }

    public static void newSimulation() {
        int totalTickets;
        int vendorNum;
        int customerNum;
        int ticketReleaseRate;
        int customerRetrievalRate;
        int maxTicketCapacity;
        Configuration configuration;

        // Log the start of manual configuration
        logToFile("\nStarting the manual Configuration of the parameters");

        maxTicketCapacity = validate("Enter the maximum Ticket capacity: ");
        logToFile("Max ticket Capacity is : " + maxTicketCapacity);

        totalTickets = validate("Enter the Total Number of Tickets(<=Maximum Ticket Capacity): ", maxTicketCapacity);
        logToFile("Total tickets is : " + totalTickets);

        vendorNum = validate("Enter the number of Vendors: ");
        logToFile("Number of Vendors : " + vendorNum);

        customerNum = validate("Enter the number of customers: ");
        logToFile("Number of Customers : " + customerNum);

        ticketReleaseRate = validate("Enter the Ticket Release rate (milli-seconds): ");
        logToFile("Ticket Release rate is : " + ticketReleaseRate);

        customerRetrievalRate = validate("Enter the Customer Retrieval rate in milli-seconds(Non VIP): ");
        logToFile("Customer Retrieval Rate is : " + customerRetrievalRate);

        configuration = new Configuration(totalTickets, vendorNum, customerNum, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        storeJSON(configuration);
        logToFile("Parameter configuration has been completed.");
        logToFile("Configuration successfully saved into ticket_system_config.json");
        logToFile("New Configuration has been created and Saved by the user.");

        try {
            runSimulation(configuration);
        } catch (InterruptedException e) {
            logToFile("Interrupted while threading");
        }
    }

    public static List<Configuration> loadConfigurations() {
        List<Configuration> configurations = new ArrayList<>();

        try (FileReader reader = new FileReader("configuration.json")) {
            Gson gson = new Gson();
            Configuration[] configs = gson.fromJson(reader, Configuration[].class);
            configurations.addAll(Arrays.asList(configs));
        } catch (FileNotFoundException e) {
            System.err.println("Configuration file 'configuration.json' not found.");
        } catch (IOException e) {
            System.err.println("Error reading configuration file: " + e.getMessage());
        }

        return configurations;
    }

    public static Configuration selectConfiguration(List<Configuration> configurations) {
        if (configurations.isEmpty()) {
            System.out.println("No previous configurations found.");
            return null;
        }

        System.out.println("Select a previous configuration:");
        for (int i = 0; i < configurations.size(); i++) {
            Configuration config = configurations.get(i);
            System.out.println((i + 1) + ".  Maximum Ticket Capacity: " + config.getMaxTicketCapacity() +
                    ", Total Tickets: " + config.getTotalTickets() + ", Vendors: " + config.getVendorNum() + "," +
                    " Customers: " + config.getCustomerNum() + ", Ticket Release rate: " + config.getTicketReleaseRate() +
                    ", Customer Retrieval rate: " + config.getCustomerRetrievalRate()
            );
            System.out.println("-------------------------------------------------------");
        }

        int choice = validate("Enter the configuration number: ", configurations.size());
        return configurations.get(choice - 1);
    }

    public static int validate(String Statement) {
        Scanner scanner = new Scanner(System.in);
        int user_input;

        while (true) {
            try {
                System.out.print(Statement);
                user_input = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid integer value, larger than 0..");
                scanner.nextLine();
                continue;
            }
            if (user_input <= 0) {
                System.out.println("Enter a valid integer value, larger than 0..");
                scanner.nextLine();
            } else {
                scanner.nextLine();
                break;
            }
        }

        return user_input;
    }

    public static int validate(String Statement, int Capacity) {
        Scanner scanner = new Scanner(System.in);
        int user_input;

        while (true) {
            try {
                System.out.print(Statement);
                user_input = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid integer value, larger than 0..");
                scanner.nextLine();
                continue;
            }
            if (user_input <= 0) {
                System.out.println("Enter a valid integer value, larger than 0..");
                scanner.nextLine();
            } else if (user_input > Capacity) {
                System.out.println("Enter a valid integer value, that respects the requirements");
                scanner.nextLine();
            } else {
                scanner.nextLine();
                break;
            }
        }

        return user_input;
    }

    public static void storeJSON(Configuration configuration) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Configuration> configurationList = new ArrayList<>();

        try (FileReader reader = new FileReader("configuration.json")) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    Configuration existingConfig = gson.fromJson(element, Configuration.class);
                    configurationList.add(existingConfig);
                }
            }
        } catch (IOException e) {
            System.out.println("No existing configuration file found. A new file will be created.");
        }

        configurationList.add(configuration);

        try (FileWriter writer = new FileWriter("configuration.json")) {
            gson.toJson(configurationList, writer);
            System.out.println("Configuration successfully recorded.");
        } catch (IOException e) {
            System.err.println("Error recording configuration: " + e.getMessage());
        }

    }

    public static void runSimulation(Configuration configuration) throws InterruptedException {
        TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity(), configuration.getTotalTickets());
        AtomicInteger ticketCounter = new AtomicInteger(1);
        AtomicInteger ticketsSold = new AtomicInteger(0);
        List<Vendor> vendorList = new ArrayList<>();
        List<Customer> customerList = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        int zeroCustomer = 0;
        int zeroVendor = 0;

        logToFile("Starting Simulation");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < configuration.getVendorNum(); i++) {
            String vendorName = "Vendor_" + i;
            Vendor vendor = new Vendor(ticketPool, configuration.getTotalTickets(), vendorName, configuration.getTicketReleaseRate(), ticketCounter);
            Thread vendorThread = new Thread(vendor);
            vendorList.add(vendor);
            threads.add(vendorThread);
            vendorThread.start();

            logToFile(vendorName + " started");
        }

        Thread.sleep(1000);

        for (int i = 0; i < configuration.getCustomerNum(); i++) {
            String customerName = "Customer_" + i;
            Customer customer = new Customer(ticketPool, customerName, configuration.getCustomerRetrievalRate(), ticketsSold);
            Thread customerThread = new Thread(customer);
            customerList.add(customer);
            threads.add(customerThread);
            customerThread.start();

            logToFile(customerName + " started");
        }

        for (Thread thread : threads) {
            thread.join();
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        logToFile("Simulation started at: " + LocalDateTime.now().format(FORMATTER));
        logToFile("Simulation ended at: " + LocalDateTime.now().format(FORMATTER));

        System.out.println("Vendor Summary:");
        System.out.println("| Vendor Name | Tickets Sold |");
        System.out.println("|-------------|--------------|");
        for (Vendor vendor : vendorList) {
            System.out.printf("| %-12s | %-12d |\n", vendor.getVendorName(), vendor.getTicketsSold());
            if (vendor.getTicketsSold() == 0){
                zeroVendor++;
            }

            logToFile("Vendor " + vendor.getVendorName() + " sold " + vendor.getTicketsSold() + " tickets");
        }

        System.out.println("Customer Summary:");
        System.out.println("| Customer Name | Tickets Bought |");
        System.out.println("|---------------|----------------|");
        for (Customer customer : customerList) {
            System.out.printf("| %-12s | %-12d |\n", customer.getCustomerName(), customer.getTicketsBought());
            if (customer.getTicketsBought() == 0){
                zeroCustomer++;
            }

            logToFile("Customer " + customer.getCustomerName() + " bought " + customer.getTicketsBought() + " tickets");
        }

        double avgTicketsSold = (double) configuration.getTotalTickets() / vendorList.size();
        double avgTicketsBought = (double) configuration.getTotalTickets() / customerList.size();

        System.out.println("Total Tickets Sold: " + configuration.getTotalTickets());
        System.out.println("Vendors without any Ticket Sold: " + zeroVendor);
        System.out.println("Customers without any Tickets: " + zeroCustomer);
        System.out.printf("Average Tickets Sold per Vendor: %.2f\n", avgTicketsSold);
        System.out.printf("Average Tickets Bought per Customer: %.2f\n", avgTicketsBought);
        System.out.println("Simulation Time: " + totalTime + " milliseconds");

        logToFile("Total Tickets Sold: " + configuration.getTotalTickets());
        logToFile("Vendors without any Ticket Sold: " + zeroVendor);
        logToFile("Customers without any Tickets: " + zeroCustomer);
        logToFile(String.format("Average Tickets Sold per Vendor: %.2f", avgTicketsSold));
        logToFile(String.format("Average Tickets Bought per Customer: %.2f", avgTicketsBought));
        logToFile("Simulation Time: " + totalTime + " milliseconds");
        logToFile("----- LOG RECORD ENDED!-----\n\n\n");
    }

    private static void logToFile(String message) {
        try {
            String timestampedMessage = LocalDateTime.now().format(FORMATTER) + " - " + message;
            Files.writeString(Paths.get(LOG_FILE), timestampedMessage + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }
}