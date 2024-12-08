import java.util.concurrent.atomic.AtomicInteger;

class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerName;
    private final long retrievalRate;
    private final AtomicInteger ticketsSold;
    private int ticketsBought = 0;

    public Customer(TicketPool ticketPool, String customerName, long retrievalRate, AtomicInteger ticketsSold) {
        this.ticketPool = ticketPool;
        this.customerName = customerName;
        this.retrievalRate = retrievalRate;
        this.ticketsSold = ticketsSold;
    }


    public String getCustomerName() {
        return customerName;
    }

    public int getTicketsBought() {
        return ticketsBought;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Ticket ticket = ticketPool.removeTicket();
                if (ticket == null) {
                    break;
                }
                ticketsBought++;
                ticketsSold.incrementAndGet();
                String ticketID = ticket.getTicketID();
                System.out.println(customerName + " purchased Ticket: " + ticketID);
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}