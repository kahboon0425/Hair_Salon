package assignmentccp;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerGenerator extends Thread {

    Customer customer;
    Salon salon;
//    public boolean closingTime=false;

    public CustomerGenerator(Salon salon) {
        this.salon = salon;
    }

    public void run() {
        int customerID = 1;

        while (customerID <= 20) {
            try {
                Customer customer = new Customer(customerID, salon);
                customer.start();
                customerID++;
                Thread.sleep(ThreadLocalRandom.current().nextInt(0, 2001));
            } catch (InterruptedException ex) {
                Logger.getLogger(CustomerGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
