public class Customer implements Runnable {
    private String customerID;
    private TicketPool ticketPool;
    private int customerNum;
    private int customerRetrievalRate;
    private int PriorityCustomerNum;
    private int PriorityCustomerRetrievalRate;


    public Customer(String customerID, TicketPool ticketPool, int customerNum, int customerRetrievalRate, int PriorityCustomerNum, int PriorityCustomerRetrievalRate){
        this.customerID = customerID;
        this.ticketPool = ticketPool;
        this.customerNum = customerNum;
        this.customerRetrievalRate = customerRetrievalRate;
        this.PriorityCustomerNum = PriorityCustomerNum;
        this.PriorityCustomerRetrievalRate = PriorityCustomerRetrievalRate;
    }

    public Customer(String customerID, TicketPool ticketPool, int customerNum, int customerRetrievalRate){
        this.customerID = customerID;
        this.ticketPool = ticketPool;
        this.customerNum = customerNum;
        this.customerRetrievalRate = customerRetrievalRate;
    }

    @Override
    public void run(){
        try {
            Ticket ticket = TicketPool.buyTicket(isPriorityCustomer);

            if (ticket != null) {
                System.out.println(customerID + (isPriorityCustomer ? " (Priority)" : "") + " successfully purchased Ticket: " + ticket.getTicketID());
            } else {
                System.out.println(customerID + (isPriorityCustomer ? " (Priority)" : "") + " could not purchase a ticket. No tickets available.");
            }
        } catch (Exception e) {
            System.err.println(customerID + " encountered an error during ticket purchase: " + e.getMessage());
        }
    }
}
