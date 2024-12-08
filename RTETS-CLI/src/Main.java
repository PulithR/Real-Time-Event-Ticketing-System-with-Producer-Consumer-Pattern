import com.google.gson.*;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        int choice;

        do{
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

                    if(selectedConfig == null){
                        System.out.println("Error while selecting a configuration");
                        break;
                    }
                    System.out.println("\nStarting Stimulation\n");
                    try {
                        runSimulation(selectedConfig);
                    }catch (InterruptedException e){
                        System.out.println("Interrupted while threading");
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        }while (choice != 3);


    }

    public static void newSimulation(){
        int totalTickets;
        int vendorNum;
        int customerNum;
        int ticketReleaseRate;
        int customerRetrievalRate;
        int maxTicketCapacity;
        Configuration configuration;


        maxTicketCapacity = validate("Enter the maximum Ticket capacity: ");
        totalTickets = validate("Enter the Total Number of Tickets(<=Maximum Ticket Capacity): ", maxTicketCapacity);
        vendorNum = validate("Enter the number of Vendors: ");
        customerNum = validate("Enter the number of customers: ");
        ticketReleaseRate = validate("Enter the Ticket Release rate (milli-seconds): ");
        customerRetrievalRate = validate("Enter the Customer Retrieval rate in milli-seconds(Non VIP): ");

        configuration = new Configuration(totalTickets, vendorNum, customerNum, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);

        storeJSON(configuration);
        System.out.println("\nStarting Stimulation\n");
        try {
            runSimulation(configuration);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while threading");
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

    public static int validate(String Statement){
        Scanner scanner = new Scanner(System.in);
        int user_input;

        while(true) {
            try{
                System.out.print(Statement);
                user_input = scanner.nextInt();
            } catch (InputMismatchException e){
                System.out.println("Enter a valid integer value, larger than 0..");
                scanner.nextLine();
                continue;
            }
            if (user_input <= 0){
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

        try (FileReader reader = new FileReader("configuration.json")){
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

        try (FileWriter writer = new FileWriter("configuration.json")){
            gson.toJson(configurationList, writer);
            System.out.println("Configuration successfully recorded.");
        } catch (IOException e) {
            System.err.println("Error recording configuration: " + e.getMessage());
        }

    }

    public static void runStimulation(Configuration configuration) throws InterruptedException {
        TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity(), configuration.getTotalTickets());

        for (int i = 0; i < configuration.getVendorNum(); i++) {
            new Thread(new Vendor(ticketPool, configuration, "Vendor_" + i)).start();
        }
        Thread.sleep(500);

        for (int i = 0; i < (configuration.getPriorityCustomerNum()); i++) {
            new Thread(new Customer("VIP_Customer_" + i, ticketPool, true, configuration.getPriorityCustomerRetrievalRate())).start();
        }

        for(int i = 0; i < (configuration.getCustomerNum()); i++) {
            new Thread(new Customer("Customer_" + i, ticketPool, false, configuration.getCustomerRetrievalRate())).start();
        }

        while (!ticketPool.allTicketsProcessed()) {
            Thread.sleep(100); // Check periodically
        }

        System.out.println("Simulation complete!");
        System.out.println("Total tickets sold: " + ticketPool.getTotalTicketsSold());
        System.out.println("Unsatisfied customers: " + ticketPool.getUnsatisfiedCustomers());
        System.out.println("Excess tickets: " + ticketPool.getExcessTickets());
    }
}