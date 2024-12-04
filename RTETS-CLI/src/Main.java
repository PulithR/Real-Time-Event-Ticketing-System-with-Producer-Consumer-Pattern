import com.google.gson.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        int totalTickets;
        int customerNum;
        int ticketReleaseRate;
        int customerRetrievalRate;
        int maxTicketCapacity;
        int PriorityCustomerNum;
        int PriorityCustomerRetrievalRate;
        Configuration configuration;
        Scanner scanner = new Scanner(System.in);

        totalTickets = validate("Enter the Total Number of Tickets: ");
        customerNum = validate("Enter the number of customers: ");
        ticketReleaseRate = validate("Enter the Ticket Release rate (milli-seconds): ");
        customerRetrievalRate = validate("Enter the Customer Retrieval rate in milli-seconds(Non VIP): ");
        maxTicketCapacity = validate("Enter the maximum Ticket capacity(>=Total Number of Tickets): ", totalTickets);

        System.out.println("Add VIP customers?");
        System.out.println("1. Yes");
        System.out.println("2. No");


        while(true) {
            int choice = 0;
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid choice");
            }
            if (choice == 1) {
                PriorityCustomerNum = validate("Enter the number of VIP customers? ");
                PriorityCustomerRetrievalRate = validate("Enter the Customer Retrieval rate in milli-seconds(VIP): ");
                configuration = new Configuration(totalTickets, customerNum, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity, PriorityCustomerNum, PriorityCustomerRetrievalRate);
                break;
            } else if (choice == 2) {
                configuration = new Configuration(totalTickets, customerNum, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                break;
            } else {
                System.out.println("Enter a valid choice");
                scanner.nextLine();
            }
        }

        storeJSON(configuration);
        System.out.println("\nStarting Stimulation\n");
//        runStimulation();





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


    public static int validate(String Statement, int TicketNum){
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
            } else if (user_input <= TicketNum) {
                System.out.println("Enter a valid integer value, larger than total number of tickets");
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

//    public static void runStimulation() throws InterruptedException {
//
//    }
}