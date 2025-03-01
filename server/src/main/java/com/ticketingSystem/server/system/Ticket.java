package com.ticketingSystem.server.system;

public class Ticket {
    private final String ticketID;

    public Ticket(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketID() {
        return ticketID;
    }
}
