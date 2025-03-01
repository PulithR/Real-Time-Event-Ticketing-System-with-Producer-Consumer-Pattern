package com.ticketingSystem.server.system;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;


public class TicketPool {

    private final LinkedBlockingQueue<Ticket> tickets;
    private final int maxCapacity;
    private final int totalTickets;
    private final ReentrantLock lock;
    private final Semaphore producerSemaphore;
    private final Semaphore consumerSemaphore;
    private volatile int totalTicketsAdded;

    private volatile boolean shouldStop;

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.tickets = new LinkedBlockingQueue<>(maxCapacity);
        this.lock = new ReentrantLock(true); // Fair lock to prevent thread starvation
        this.producerSemaphore = new Semaphore(maxCapacity);
        this.consumerSemaphore = new Semaphore(0);
        this.totalTicketsAdded = 0;
        this.shouldStop = false;
    }


    public boolean addTicket(Ticket ticket) throws InterruptedException {
        if (!producerSemaphore.tryAcquire(1, TimeUnit.SECONDS)) {
            return false;
        }

        lock.lock();
        try {
            if (totalTicketsAdded >= totalTickets || tickets.size() >= maxCapacity) {
                producerSemaphore.release();
                return false;
            }

            tickets.offer(ticket);
            totalTicketsAdded++;
            consumerSemaphore.release();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Ticket removeTicket() throws InterruptedException {
        if (!consumerSemaphore.tryAcquire(1, TimeUnit.SECONDS)) {
            return null;
        }

        lock.lock();
        try {
            if (totalTicketsAdded >= totalTickets && tickets.isEmpty()) {
                consumerSemaphore.release();
                return null;
            }

            Ticket ticket = tickets.poll();
            producerSemaphore.release();
            return ticket;
        } finally {
            lock.unlock();
        }
    }
}
