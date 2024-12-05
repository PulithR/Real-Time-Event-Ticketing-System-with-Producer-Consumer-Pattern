public class Customer implements Runnable {
    private final String customerID;
    private final TicketPool ticketPool;
    private final boolean isPriorityCustomer;
    private final int retrievalRate;


    public Customer(String customerID, TicketPool ticketPool, boolean isPriorityCustomer, int retrievalRate){
        this.customerID = customerID;
        this.ticketPool = ticketPool;
        this.isPriorityCustomer = isPriorityCustomer;
        this.retrievalRate = retrievalRate;
    }


    @Override
    public void run(){
        try {
            while (!ticketPool.allTicketsProcessed()) {
                ticketPool.buyTicket(customerID, isPriorityCustomer);
                Thread.sleep(retrievalRate);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(customerID + " interrupted.");
        }
    }
}
