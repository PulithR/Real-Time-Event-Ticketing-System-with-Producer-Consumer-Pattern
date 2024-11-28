import com.google.gson.Gson;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        int totalTickets;
        int TicketReleaseRate;
        int customerRetrievalRate;
        int maxTicketCapacity;
        int PriorityCustomerNum;
        int PriorityCustomerRetrievalRate;
        Scanner scanner = new Scanner(System.in);

        totalTickets = validate("Enter the Total Number of Tickets: ");
        TicketReleaseRate = validate("Enter the Ticket Release rate (milli-seconds): ");
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
                Configuration configuration = new Configuration(totalTickets, TicketReleaseRate, customerRetrievalRate, maxTicketCapacity, PriorityCustomerNum, PriorityCustomerRetrievalRate);
                break;
            } else if (choice == 2) {
                Configuration configuration = new Configuration(totalTickets, TicketReleaseRate, customerRetrievalRate, maxTicketCapacity);
                break;
            } else {
                System.out.println("Enter a valid choice");
                scanner.nextLine();
            }
        }

        System.out.println("\nStarting Stimulation\n");
        storeJSON(configuration);




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
        Gson gson = new Gson().setPre
    }
}