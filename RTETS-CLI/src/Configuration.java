public class Configuration {
    private int totalTickets;
    private int customerNum;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int PriorityCustomerNum;
    private int PriorityCustomerRetrievalRate;

    public Configuration(int totalTickets, int customerNum, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity, int PriorityCustomerNum, int PriorityCustomerRetrievalRate){
        this.totalTickets = totalTickets;
        this.customerNum = customerNum;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.PriorityCustomerNum = PriorityCustomerNum;
        this.PriorityCustomerRetrievalRate = PriorityCustomerRetrievalRate;
    }


    public Configuration(int totalTickets, int customerNum, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity){
        this.totalTickets = totalTickets;
        this.customerNum = customerNum;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets(){
        return this.totalTickets;
    }

    public int getCustomerNum(){
        return this.customerNum;
    }

    public int getTicketReleaseRate(){
        return this.ticketReleaseRate;
    }

    public int getCustomerRetrievalRate(){
        return this.customerRetrievalRate;
    }

    public int getMaxTicketCapacity(){
        return this.maxTicketCapacity;
    }

    public int getPriorityCustomerNum(){
        return this.PriorityCustomerNum;
    }

    public int getPriorityCustomerRetrievalRate(){
        return this.PriorityCustomerRetrievalRate;
    }
}
