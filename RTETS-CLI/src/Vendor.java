import java.util.concurrent.atomic.AtomicInteger;

class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final String vendorName;
    private final int releaseRate;
    private final AtomicInteger ticketCounter;
    private int ticketsSold = 0;

    public Vendor(TicketPool ticketPool, int totalTickets, String vendorName, int releaseRate, AtomicInteger ticketCounter) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.vendorName = vendorName;
        this.releaseRate = releaseRate;
        this.ticketCounter = ticketCounter;
    }

    public String getVendorName() {
        return vendorName;
    }

    public int getTicketsSold() {
        return ticketsSold;
    }


    @Override
    public void run() {
        for (int i = 1; i <= totalTickets; i++) {
            try {
                Ticket ticket = new Ticket("T" + ticketCounter.getAndIncrement());
                if (ticketPool.addTickets(ticket)) {
                    ticketsSold++;
                    System.out.println(vendorName + " added a ticket (" + ticket.getTicketID() + ")");
                } else {
                    break; // Stop if no more tickets can be added
                }
                Thread.sleep(releaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}