package com.ticketingSystem.server.system;

import com.ticketingSystem.server.service.SimulationService;
import com.ticketingSystem.server.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final String vendorName;
    private final int releaseRate;
    private final AtomicInteger ticketCounter;
    private int ticketsAdded = 0;

    @Autowired
    private final WebSocketController webSocketController;
    private  final SimulationService service;

    public Vendor(TicketPool ticketPool, int totalTickets, String vendorName, int releaseRate, AtomicInteger ticketCounter, WebSocketController webSocketController, SimulationService service ) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.vendorName = vendorName;
        this.releaseRate = releaseRate;
        this.ticketCounter = ticketCounter;
        this.webSocketController = webSocketController;
        this.service = service;
    }

    public int getTicketsAdded() {
        return ticketsAdded;
    }


    @Override
    public void run() {
        try {
            while (!service.getShouldStop() && ticketsAdded < totalTickets) {
                Ticket ticket = new Ticket("T" + ticketCounter.getAndIncrement());
                if (ticketPool.addTicket(ticket)) {
                    ticketsAdded++;
                    System.out.println(vendorName + " added Ticket: " + ticket.getTicketID());
                    webSocketController.broadcastSimulationUpdate(vendorName + " added Ticket: " + ticket.getTicketID());
                }
                // Use a shorter sleep duration with a check for interruption
                Thread.sleep(releaseRate); // Adjust timeout as needed
            }
            if (ticketsAdded >= totalTickets) {
                System.out.println(vendorName + " has released all tickets.");
                webSocketController.broadcastSimulationUpdate(vendorName + " has released all tickets.");
            }
        } catch (InterruptedException e) {
            // Log the interruption and clear the interrupt flag
            System.out.println(vendorName + " interrupted. Stopping.");
            Thread.currentThread().interrupt(); // Preserve interrupted status
        } finally {
            // Ensure resources are released even if an exception occurs
            System.out.println(vendorName + " finished ticket addition.");
            webSocketController.broadcastSimulationUpdate(vendorName + " finished ticket addition.");
        }
    }

}