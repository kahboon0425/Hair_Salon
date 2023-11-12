package assignmentccp;

import java.util.Date;

public class Customer extends Thread {

    int customerID;
    Date inTime;
    Salon salon;
    final int CUSTOMER_MAX_WAIT_DURATION = 4;

    public Customer(int customerID, Salon salon) {
        this.customerID = customerID;
        this.salon = salon;
        this.inTime = new Date();
    }

    public void run() {
        try {
            if (!salon.isClosingTime()) {
                salon.add(this);
            }
           
            new Thread(() -> {
                try {
                    Thread.sleep(CUSTOMER_MAX_WAIT_DURATION * 1000);
                    salon.removeImpatientCustomer(this);
                } catch (InterruptedException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }).start();
        } catch (InterruptedException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
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


}
