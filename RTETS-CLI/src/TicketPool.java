import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class TicketPool {
    private final LinkedBlockingQueue<Ticket> tickets;
    private final int maxCapacity;
    private final int totalTickets;
    private final ReentrantLock lock;
    private final Semaphore producerSemaphore;
    private final Semaphore consumerSemaphore;
    private volatile int totalTicketsAdded;

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
        this.tickets = new LinkedBlockingQueue<>(maxCapacity);
        this.lock = new ReentrantLock(true); // Fair lock to prevent starvation
        this.producerSemaphore = new Semaphore(maxCapacity);
        this.consumerSemaphore = new Semaphore(0);
        this.totalTicketsAdded = 0;
    }

    public boolean addTickets(Ticket ticket) throws InterruptedException {
        // Attempt to acquire producer semaphore with a timeout
        if (!producerSemaphore.tryAcquire(1, TimeUnit.SECONDS)) {
            return false;
        }

        lock.lock();
        try {
            // Double-check ticket addition conditions
            if (totalTicketsAdded >= totalTickets || tickets.size() >= maxCapacity) {
                producerSemaphore.release();
                return false;
            }

            tickets.offer(ticket);
            totalTicketsAdded++;
            consumerSemaphore.release(); // Signal a consumer
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Ticket removeTicket() throws InterruptedException {
        // Attempt to acquire consumer semaphore with a timeout
        if (!consumerSemaphore.tryAcquire(1, TimeUnit.SECONDS)) {
            return null;
        }

        lock.lock();
        try {
            // If no more tickets will be added and queue is empty
            if (totalTicketsAdded >= totalTickets && tickets.isEmpty()) {
                consumerSemaphore.release(); // Release for other potential consumers
                return null;
            }

            Ticket ticket = tickets.poll();
            producerSemaphore.release(); // Signal a producer
            return ticket;
        } finally {
            lock.unlock();
        }
    }
}