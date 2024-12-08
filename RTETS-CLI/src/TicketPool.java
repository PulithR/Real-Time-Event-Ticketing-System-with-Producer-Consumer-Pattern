import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TicketPool {
    private final List<Ticket> tickets = Collections.synchronizedList(new LinkedList<>());
    private final PriorityQueue<String> vipQueue = new PriorityQueue<>();
    private final int maxCapacity;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition ticketsAvailable = lock.newCondition();
//    private final Condition priorityTurn = lock.newCondition();
//    private boolean priorityAccess = true;
    private final Condition notFull = lock.newCondition();
    private int totalTicketsAdded = 0;
    private int totalTicketsSold = 0;
    private final int totalTicketsToAdd;
    private int unsatisfiedCustomers = 0;


    public TicketPool(int maxCapacity, int totalTicketsToAdd){
        this.maxCapacity = maxCapacity;
        this.totalTicketsToAdd = totalTicketsToAdd;
    }

    public void addTicket(Ticket ticket) throws InterruptedException{
        lock.lock();
        try {
            while (tickets.size() >= maxCapacity) {
                notFull.await();
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
            if (isPriorityCustomer) {
                vipQueue.offer(customerID); // Add VIP customer to the priority queue
            }

            while (tickets.isEmpty() || (isPriorityCustomer && !vipQueue.peek().equals(customerID))) {
                if (totalTicketsSold >= totalTicketsToAdd) {
                    // All tickets sold
                    unsatisfiedCustomers++;
                    return;
                }
                ticketsAvailable.await(); // Wait for tickets to be added or for VIP priority
            }

            // Remove the ticket and update sales
            Ticket ticket = tickets.remove(0);
            totalTicketsSold++;

            if (isPriorityCustomer) {
                vipQueue.poll(); // Remove from VIP queue once served
            }

            System.out.println("Customer: " + customerID + " purchased Ticket: " + ticket.getTicketID());

            notFull.signalAll();
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

    public int getUnsatisfiedCustomers() {
        return unsatisfiedCustomers;
    }

    public int getExcessTickets() {
        return Math.max(0, totalTicketsAdded - totalTicketsSold);
    }

    public int getTotalTicketsSold() {
        return this.totalTicketsSold;
    }
}
