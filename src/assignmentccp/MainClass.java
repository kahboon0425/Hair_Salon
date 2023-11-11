package assignmentccp;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass {

    public static void main(String[] args) throws InterruptedException {

        Salon salon = new Salon();

        // Create 3 hairdresser threads
        Thread hairdresser1 = new Thread(new Hairdresser(salon, 1));
        Thread hairdresser3 = new Thread(new Hairdresser(salon, 3));
        Thread hairdresser2 = new Thread(new Hairdresser(salon, 2));

        hairdresser1.start();
        hairdresser2.start();
        hairdresser3.start();

        CustomerGenerator customerGenerator = new CustomerGenerator(salon);
        customerGenerator.start();
         Clock clock = new Clock(customerGenerator, salon);
        clock.start();

        try {
            customerGenerator.join();
            Thread.sleep(5000);  // Allow time for the last customer to get their hair cut
        } catch (InterruptedException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

//        salon.setClosingTime();
        hairdresser1.join();
        hairdresser2.join();
        hairdresser3.join();

        System.out.println("Salon is closed for the day!");
    }
}
