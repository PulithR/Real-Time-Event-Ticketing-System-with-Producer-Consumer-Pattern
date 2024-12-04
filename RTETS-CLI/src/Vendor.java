
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
        int totalTickets = 0;
        try{
            while (totalTickets < config.getMaxTicketCapacity()){
                Ticket ticket =  new Ticket("T" + (++totalTickets));
                ticketPool.addTicket(ticket, vendorID);
                System.out.println(vendorID + " added a ticket | Total number of tickets: " + totalTickets);
                Thread.sleep(config.getTicketReleaseRate());
            }
            if(totalTickets == config.getMaxTicketCapacity()){
                Thread.sleep(config.getTicketReleaseRate());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor thread interrupted: " + e.getMessage());
        }
    }
}
