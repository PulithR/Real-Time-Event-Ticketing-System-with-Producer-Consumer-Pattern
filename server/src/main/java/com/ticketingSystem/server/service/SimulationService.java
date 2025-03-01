package com.ticketingSystem.server.service;

import com.ticketingSystem.server.model.Configuration;
import com.ticketingSystem.server.system.SimulationResult;
import com.ticketingSystem.server.system.TicketPool;
import com.ticketingSystem.server.system.Customer;
import com.ticketingSystem.server.system.Vendor;
import com.ticketingSystem.server.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SimulationService {

    private TicketPool ticketPool;
    private final List<Thread> vendorThreads;
    private final List<Thread> customerThreads;
    private final AtomicInteger ticketCounter;
    private final AtomicInteger ticketsSold;
    private volatile boolean systemRunning;

    private final List<Vendor> vendorList;
    private final List<Customer> customerList;
    private final List<Thread> threads;

    private volatile boolean shouldStop = false;

    @Autowired
    private WebSocketController webSocketController;


    @Autowired
    public SimulationService() {
        this.vendorThreads = new ArrayList<>();
        this.customerThreads = new ArrayList<>();
        this.vendorList = new ArrayList<>();
        this.customerList = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.ticketCounter = new AtomicInteger(1);
        this.ticketsSold = new AtomicInteger(0);
        this.systemRunning = false;
    }

    public boolean isSystemRunning() {
        return systemRunning;
    }

    // Method to start the system
    public synchronized SimulationResult startSimulation(Configuration configuration) throws InterruptedException {
        if (systemRunning) {
            System.out.println("Simulation is already running.");
            return null;
        }

        ticketCounter.set(1); // Reset ticket counter
        ticketsSold.set(0); // Reset tickets sold counter
        vendorList.clear(); // Clear vendor list
        customerList.clear(); // Clear customer list
        vendorThreads.clear(); // Clear vendor thread list
        customerThreads.clear(); // Clear customer thread list
        threads.clear(); // Clear all threads list

        this.ticketPool = new TicketPool(configuration.getMaxTicketCapacity(), configuration.getTotalTickets());
        systemRunning = true;
        shouldStop = false;

        long startTime = System.currentTimeMillis();

        // Start vendor threads
        for (int i = 0; i < configuration.getVendorNum(); i++) {
            String vendorName = "Vendor_" + i;
            Vendor vendor = new Vendor(ticketPool, configuration.getTotalTickets(), vendorName, configuration.getTicketReleaseRate(), ticketCounter, webSocketController, this);
            vendorList.add(vendor);
            Thread vendorThread = new Thread(vendor);
            vendorThreads.add(vendorThread);
            threads.add(vendorThread);
            vendorThread.start();
        }

        Thread.sleep(1000);

        // Start customer threads
        for (int i = 0; i < configuration.getCustomerNum(); i++) {
            String customerName = "Customer_" + i;
            Customer customer = new Customer(ticketPool, customerName, configuration.getCustomerRetrievalRate(), ticketsSold, webSocketController, this);
            customerList.add(customer);
            Thread customerThread = new Thread(customer);
            customerThreads.add(customerThread);
            threads.add(customerThread);
            customerThread.start();
        }

        while (systemRunning) {
            boolean allVendorsDone = vendorList.stream().allMatch(v -> v.getTicketsAdded() >= configuration.getTotalTickets());
            boolean allTicketsSold = ticketsSold.get() >= configuration.getTotalTickets();
            boolean allCustomersDone = customerList.stream().allMatch(c -> c.getTicketsBought() >= configuration.getTotalTickets());

            // Check stop conditions
            if (allVendorsDone || allTicketsSold || allCustomersDone) {
                stopSimulation();
                break;// Stop simulation if any condition is met
            }

            Thread.sleep(100); // Small delay to prevent busy-waiting
        }


        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;



        systemRunning = false;

        webSocketController.broadcastSimulationUpdate(generateSimulationSummary(configuration, totalTime).toString());
        return generateSimulationSummary(configuration, totalTime);
    }


    private SimulationResult generateSimulationSummary(Configuration configuration, long simulationTime) {
        int zeroVendors = 0;
        int zeroCustomers = 0;

        for (Vendor vendor : vendorList) {
            if (vendor.getTicketsAdded() == 0) zeroVendors++;
        }

        for (Customer customer : customerList) {
            if (customer.getTicketsBought() == 0) zeroCustomers++;
        }

        double avgTicketsSold = !vendorList.isEmpty()
                ? (double) configuration.getTotalTickets() / vendorList.size()
                : 0.0;

        double avgTicketsBought = !customerList.isEmpty()
                ? (double) configuration.getTotalTickets() / customerList.size()
                : 0.0;


        return new SimulationResult(
                configuration.getTotalTickets(),
                zeroVendors,
                zeroCustomers,
                avgTicketsSold,
                avgTicketsBought,
                simulationTime
        );
    }

    // Method to stop the system
    public void stopSimulation()  {
        shouldStop = true;
        for (Thread thread : threads) {
            thread.interrupt(); // Interrupt threads to handle stopping during sleep or blocking states
        }
        System.out.println("Simulation stopped...");
        systemRunning = false;
    }

    public boolean getShouldStop(){
        return shouldStop;
    }

}