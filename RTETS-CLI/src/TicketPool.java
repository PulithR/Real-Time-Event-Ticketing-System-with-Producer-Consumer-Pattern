import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final List<Ticket> tickets = new LinkedList<>();
    private final int maxCapacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition ticketsAvailable = lock.newCondition();
    private final Condition priorityTurn = lock.newCondition();
    private boolean priorityAccess = true;
    private int totalTicketsAdded = 0;
    private int totalTicketsSold = 0;
    private final int totalTicketsToAdd;

    public TicketPool(int maxCapacity, int totalTicketsToAdd){
        this.maxCapacity = maxCapacity;
        this.totalTicketsToAdd = totalTicketsToAdd;
    }

    public void addTicket(Ticket ticket) throws InterruptedException{
        lock.lock();
        try {
            while (tickets.size() >= maxCapacity) {
                ticketsAvailable.await();
            }

            tickets.add(ticket);
            totalTicketsAdded++;
//            System.out.println("Ticket added: " + ticket.getTicketID() + " | Total Tickets in pool: " + tickets.size());

            ticketsAvailable.signalAll();

        } finally {
            lock.unlock();
        }
    }


    public void buyTicket(String customerID, boolean isPriorityCustomer) throws InterruptedException {
        lock.lock();
        try {
            while (tickets.isEmpty() || (priorityAccess && !isPriorityCustomer)) {
                if (tickets.isEmpty() && totalTicketsSold >= totalTicketsToAdd) {
                    // All tickets have been sold
                    return;
                }
                if (priorityAccess && !isPriorityCustomer) {
                    priorityTurn.await(); // Wait until priority customers are served
                } else {
                    ticketsAvailable.await(); // Wait for tickets to become available
                }
            }

            Ticket ticket = tickets.remove(0);
            totalTicketsSold++;
            System.out.println("Customer: " + customerID + " purchased Ticket: " + ticket.getTicketID());

            if (tickets.isEmpty() || !isPriorityCustomer) {
                priorityAccess = false;
                priorityTurn.signalAll();
            }

            ticketsAvailable.signalAll();

        } finally {
            lock.unlock();
        }
    }

    public boolean allTicketsProcessed() {
        lock.lock();
        try {
            return totalTicketsSold >= totalTicketsToAdd;
        } finally {
            lock.unlock();
        }
    }

    public int getTotalTicketsSold(){
        return this.totalTicketsSold;
    }
}
