/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assignmentccp;

/**
 *
 * @author kahbo
 */
public class Clock extends Thread {

    private CustomerGenerator cg;
    private Salon s;

    public Clock(CustomerGenerator cg, Salon s) {
        this.cg = cg;
        this.s = s;
    }

    public void run() {
        try {
            // Sleep for 50 seconds to simulate the salon being open
            Thread.sleep(15000);
            NotifyClosed();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void NotifyClosed() {
        System.out.println("Clock: It Is closing time !!!!!!!!!!!!!!!!!!!!!!!");
        cg.setClosingTime();
        s.setClosingTime();
    }
}