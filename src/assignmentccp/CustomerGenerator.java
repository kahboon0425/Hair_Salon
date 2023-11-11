package assignmentccp;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerGenerator extends Thread {

    Customer customer;
    Salon salon;
    public boolean closingTime = false;

    public CustomerGenerator(Salon salon) {
        this.salon = salon;
    }

    public void run() {
        int customerID = 1;

        if (!closingTime) {
            while (customerID <= 20) {
                try {
                    Customer customer = new Customer(customerID, salon);
                    customer.start();
                    customerID++;
                    // Generate a random number 0, 1, or 2 and multiply by 1000 to get milliseconds
                    int sleepTime = ThreadLocalRandom.current().nextInt(0, 3) * 1000;
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
    }
    
    public synchronized void setClosingTime() {
        this.closingTime = true;

    }

}
