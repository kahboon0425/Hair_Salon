package assignmentccp;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Salon extends Thread {

    private boolean closingTime = false;
    BlockingQueue<Customer> waitSittingCustomers = new ArrayBlockingQueue<>(5);
    BlockingQueue<Customer> waitStandingCustomers = new ArrayBlockingQueue<>(5);
    BlockingQueue<Integer> combs = new ArrayBlockingQueue<>(2);
    BlockingQueue<Integer> scissors = new ArrayBlockingQueue<>(2);

    public Salon() {
        for (int i = 1; i <= 2; i++) {
            combs.add(i);
            scissors.add(i);
        }
    }

    public synchronized boolean isClosingTime() {
        return closingTime;
    }

    public synchronized void setClosingTime() {
        this.closingTime = true;
        int waitingCustomer = waitSittingCustomers.size() + waitStandingCustomers.size();
        System.out.println("We are going to close now but still have " + waitingCustomer + " customers left.Next!");
        //notifyAll();
    }

    public synchronized void add(Customer customer) throws InterruptedException {
        System.out.println("[" + getCurrentTime() + "]: Customer " + customer.customerID + " entered the salon.");

        if (waitSittingCustomers.size() < 5) {
            // use 'offer' instead of 'add'
            // if the queue has reached its capacity, it will return false
            // indicates customer give up and leave sthe shop
            // consistent with the idea that customers will leave if there are no seats available
            waitSittingCustomers.offer(customer);
            // wake up any hairdresser who might be waiting for a customer.
            notify();
            System.out.println("[" + getCurrentTime() + "]: Customer " + customer.customerID + " is sitting on waiting-room seat.");
        } else if (waitStandingCustomers.size() < 5) {
            waitStandingCustomers.offer(customer);
            System.out.println("[" + getCurrentTime() + "]: Customer " + customer.customerID + " is standing at waiting area.");

        } else {
            System.out.println("[" + getCurrentTime() + "]: Salon is full. Customer " + customer.customerID + " leaves the salon.");
        }
    }

    public void cutHair(Hairdresser hairdresser) {
        try {
            while (true) {
                Customer customer;
                synchronized (this) {
                    while (waitSittingCustomers.isEmpty()) {
                        if (closingTime) {
                            return;
                        }
                        System.out.println("Hairdresser " + hairdresser.getHairdresserID() + " go to sleep.");
                        wait();
                    }

                    customer = waitSittingCustomers.poll();

                    if (!waitStandingCustomers.isEmpty() && waitSittingCustomers.size() < 5) {
                        // Move the longest waiting customer from standing to sitting area
                        Customer standingCustomer = waitStandingCustomers.poll();
                        waitSittingCustomers.offer(standingCustomer);
                        System.out.println("[" + getCurrentTime() + "]: Customer " + standingCustomer.getCustomerID() + " moves from standing to sitting in waiting room.");
                    }
                }

                int salonChairID = hairdresser.getHairdresserID();
                System.out.println("[" + getCurrentTime() + "]: Customer " + customer.getCustomerID() + " assign to hairdresser " + hairdresser.getHairdresserID() + ".");
                System.out.println("[" + getCurrentTime() + "]: Customer " + customer.getCustomerID() + " sits on salon chair " + salonChairID + ".");

                if (combs.isEmpty() || scissors.isEmpty()) {
                    System.out.println("[" + getCurrentTime() + "]: " + "Hairdresser " + hairdresser.getHairdresserID()
                            + " is waiting for resources, all combs and scissors are in use.");
                }

                int combId = combs.take();
                int scissorId = scissors.take();

                System.out.println("[" + getCurrentTime() + "]: Hairdresser " + hairdresser.getHairdresserID() + " acquire comb " + combId
                        + " and scissor " + scissorId + ".");

                long startTime = System.currentTimeMillis();
                System.out.println("[" + getCurrentTime() + "]: Hairdresser " + hairdresser.getHairdresserID() + " starts cutting hair for customer "
                        + customer.getCustomerID() + " at progress -- 0%");

                // Generate a total random duration between 3 and 6 seconds for the entire haircut
                int totalHaircutTime = ThreadLocalRandom.current().nextInt(3000, 6001);

                int progress = 0;
                while (progress < 100) {
                    progress += 25; // Increment by 25%
                    System.out.println("[" + getCurrentTime() + "]: Hairdresser " + hairdresser.getHairdresserID() + " is cutting customer " + customer.customerID
                            + " hair at progress -- " + progress + "%");
                    Thread.sleep(totalHaircutTime / 4);
                }

                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime) / 1000;

                System.out.println("[" + getCurrentTime() + "]: Hairdresser " + hairdresser.getHairdresserID() + " finished cutting hair for Customer "
                        + customer.getCustomerID() + ".");
                System.out.println("[" + getCurrentTime() + "]: Customer " + customer.customerID + " finished their hair cut.");
                System.out.println("Hair cutting process for customer " + customer.customerID + " took " + duration + " seconds.");
                System.out.println("[" + getCurrentTime() + "]: Customer " + customer.customerID + " pays RM " + duration * 10 + " and leaves the salon.");
                combs.put(combId);
                scissors.put(scissorId);
                System.out.println("[" + getCurrentTime() + "]: Hairdresser " + hairdresser.getHairdresserID() + " returns comb " + combId + " and scissor "
                        + scissorId + ".");

                synchronized (this) {
                    if (!waitSittingCustomers.isEmpty()) {
                        notify();
                        System.out.println("Hairdresser " + hairdresser.getHairdresserID() + " calls the next customer.");

                    }
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    String getCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        // Define a custom time format
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        // Format the current time using the defined format
        String formattedTime = currentTime.format(timeFormatter);

        return formattedTime;
    }

    public synchronized void removeImpatientCustomer(Customer customer) {
        if (waitStandingCustomers.contains(customer)) {
            waitStandingCustomers.remove(customer);
            System.out.println("[" + getCurrentTime() + "]: Customer " + customer.getCustomerID() + 
                    " got tired of waiting and left the salon. Bye!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
}
