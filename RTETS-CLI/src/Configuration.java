public class Configuration {
    private final int totalTickets;
    private final int vendorNum;
    private final int customerNum;
    private final int ticketReleaseRate;
    private final int customerRetrievalRate;
    private final int maxTicketCapacity;


    public Configuration(int totalTickets, int vendorNum, int customerNum, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity){
        this.totalTickets = totalTickets;
        this.vendorNum = vendorNum;
        this.customerNum = customerNum;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets(){
        return this.totalTickets;
    }

    public int getVendorNum() { return this.vendorNum; }

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

}

