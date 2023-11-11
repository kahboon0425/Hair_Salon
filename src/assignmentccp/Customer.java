package assignmentccp;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Customer extends Thread {

    int customerID;
    Date inTime;
    Salon salon;
    final int MAX_WAIT_TIME_IN_SECONDS = 10;

    public Customer(int customerID, Salon salon) {
        this.customerID = customerID;
        this.salon = salon;
        this.inTime = new Date();
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public void run() {
        try {
            if (!salon.isClosingTime()) {
                salon.add(this);
            }
            // Start a thread to wait for MAX_WAIT_TIME_IN_SECONDS
            new Thread(() -> {
                try {
                    Thread.sleep(MAX_WAIT_TIME_IN_SECONDS * 1000);
                    // After waiting, ask the salon to remove this customer if still standing
                    salon.removeStandingCustomer(this);
                } catch (InterruptedException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }).start();
        } catch (InterruptedException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
