
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final Configuration config;
    private final String vendorID;


    public Vendor(TicketPool ticketPool, Configuration config, String vendorID) {
        this.ticketPool = ticketPool;
        this.config = config;
        this.vendorID = vendorID;
    }

    @Override
    public void run(){
        int addedTickets = 0;
        try{
            for (int i =1; i < config.getMaxTicketCapacity(); i++ ){
                Ticket ticket =  new Ticket("T" + (++addedTickets));
                ticketPool.addTicket(ticket);
                System.out.println("Vendor: " + vendorID + " added a ticket (" + ticket.getTicketID() + ") | Total number of tickets: " + addedTickets);
                Thread.sleep(config.getTicketReleaseRate());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor thread interrupted: " + e.getMessage());
        }
    }
}
