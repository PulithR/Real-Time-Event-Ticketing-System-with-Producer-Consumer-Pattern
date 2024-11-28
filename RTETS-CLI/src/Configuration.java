public class Configuration {
    private int totalTickets;
    private int TicketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int PriorityCustomerNum;
    private int PriorityCustomerRetrievalRate;

    public Configuration(int totalTickets,int TicketReleaseRate,int customerRetrievalRate,int maxTicketCapacity,int PriorityCustomerNum,int PriorityCustomerRetrievalRate){
        this.totalTickets = totalTickets;
        this.TicketReleaseRate = TicketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
        this.PriorityCustomerNum = PriorityCustomerNum;
        this.PriorityCustomerRetrievalRate = PriorityCustomerRetrievalRate;
    }


    public Configuration(int totalTickets, int TicketReleaseRate, int customerRetrievalRate, int maxTicketCapacity){
        this.totalTickets = totalTickets;
        this.TicketReleaseRate = TicketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets(){
        return this.totalTickets;
    }

    public int getTicketReleaseRate(){
        return this.TicketReleaseRate;
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
