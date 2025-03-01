package com.ticketingSystem.server.system;



public class SimulationResult {
    private final int totalTicketsSold;
    private final int zeroVendors;
    private final int zeroCustomers;
    private final double avgTicketsSold;
    private final double avgTicketsBought;
    private final long simulationTime;

    public SimulationResult(int totalTicketsSold, int zeroVendors, int zeroCustomers, double avgTicketsSold,
                            double avgTicketsBought, long simulationTime) {
        this.totalTicketsSold = totalTicketsSold;
        this.zeroVendors = zeroVendors;
        this.zeroCustomers = zeroCustomers;
        this.avgTicketsSold = avgTicketsSold;
        this.avgTicketsBought = avgTicketsBought;
        this.simulationTime = simulationTime;
    }

    @Override
    public String toString() {

        return String.format(
                "\nSimulation Summary:\n" +
                        "--------------------\n" +
                        "Total Tickets Sold: %d\n" +
                        "Vendors without Tickets Sold: %d\n" +
                        "Customers without Tickets Bought: %d\n" +
                        "Average Tickets Sold per Vendor: %.2f\n" +
                        "Average Tickets Bought per Customer: %.2f\n" +
                        "Simulation Time: %d milliseconds\n",
                totalTicketsSold, zeroVendors, zeroCustomers, avgTicketsSold, avgTicketsBought, simulationTime
        );
    }
}
