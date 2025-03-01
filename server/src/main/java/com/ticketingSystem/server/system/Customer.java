package com.ticketingSystem.server.system;

import com.ticketingSystem.server.service.SimulationService;
import com.ticketingSystem.server.web_socket.WebSocketController;

import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerName;
    private final long retrievalRate;
    private final AtomicInteger ticketsBoughtCounter;
    private int ticketsBought = 0;

    private final WebSocketController webSocketController;
    private  final SimulationService service;

    public Customer(TicketPool ticketPool, String customerName, long retrievalRate, AtomicInteger ticketsBoughtCounter, WebSocketController webSocketController, SimulationService service) {
        this.ticketPool = ticketPool;
        this.customerName = customerName;
        this.retrievalRate = retrievalRate;
        this.ticketsBoughtCounter = ticketsBoughtCounter;
        this.webSocketController = webSocketController;
        this.service = service;
    }

    public int getTicketsBought() {
        return ticketsBought;
    }


    @Override
    public void run() {
        try {
            while (!service.getShouldStop()) {
                Ticket ticket = ticketPool.removeTicket();
                if (ticket == null) {
                    System.out.println(customerName + " found no tickets and is stopping.");
                    webSocketController.broadcastSimulationUpdate(customerName + " found no tickets and is stopping.");
                    break;
                }
                ticketsBought++;
                ticketsBoughtCounter.incrementAndGet();
                System.out.println(customerName + " purchased Ticket: " + ticket.getTicketID());
                webSocketController.broadcastSimulationUpdate(customerName + " purchased Ticket: " + ticket.getTicketID());
                Thread.sleep(retrievalRate); // Adjust timeout as needed
            }
        } catch (InterruptedException e) {
            // Log the interruption and clear the interrupt flag
            System.out.println(customerName + " interrupted. Stopping.");
            Thread.currentThread().interrupt(); // Preserve interrupted status
        } finally {
            // Ensure resources are released even if an exception occurs
            System.out.println(customerName + " finished purchasing tickets.");
            webSocketController.broadcastSimulationUpdate(customerName + " finished purchasing tickets.");
        }
    }

}
