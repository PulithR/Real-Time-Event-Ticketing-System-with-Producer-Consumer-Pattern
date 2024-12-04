import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class TicketPool {
    private final List<Ticket> tickets;
    private final int maxCapacity;

    public TicketPool(int maxCapacity){
        this.tickets = Collections.synchronizedList(new LinkedList<>());
        this.maxCapacity = maxCapacity;
    }

    public synchronized void addTicket(Ticket ticket){
        while (tickets.size() >= maxCapacity){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }

        this.tickets.add(ticket);
        notifyAll();
        System.out.println("Ticket added by: " + Thread.currentThread().getName() + " | " + "current size is: " + tickets.size());
    }


    public synchronized void buyTicket(String customerID, boolean isPriorityCustomer) {
        while (tickets.isEmpty()){
            if (!isPriorityCustomer) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println(customerID + " was interrupted while waiting for a ticket.");
                    return;
                }
            } else {
                System.out.println(customerID + " was interrupted while waiting for a ticket.");
                return;
            }
        }

        Ticket ticket = tickets.remove(0);
        notifyAll();

        System.out.println(customerID + (isPriorityCustomer ? " (Priority)" : "") + " successfully purchased Ticket: " + ticket.getTicketID());
    }

}
