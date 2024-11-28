public class Configuration {
    private int totalTickets;
    private int TicketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private int PriorityCustomerNum;
    private int PriorityCustomerRetrievalRate;

    public Configuration(int totalTickets,int TicketReleaseRate,int customerRetrievalRate,int maxTicketCapacity,int PriorityCustomerNum,int PriorityCustomerRetrievalRate){
        totalTickets = this.totalTickets;
        TicketReleaseRate = this.TicketReleaseRate;
        customerRetrievalRate = this.customerRetrievalRate;
        maxTicketCapacity = this.maxTicketCapacity;
        PriorityCustomerNum = this.PriorityCustomerNum;
        PriorityCustomerRetrievalRate = this.PriorityCustomerRetrievalRate;
    }

    public Configuration(int totalTickets, int TicketReleaseRate, int customerRetrievalRate, int maxTicketCapacity){
        totalTickets = this.totalTickets;
        TicketReleaseRate = this.TicketReleaseRate;
        customerRetrievalRate = this.customerRetrievalRate;
        maxTicketCapacity = this.maxTicketCapacity;
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
