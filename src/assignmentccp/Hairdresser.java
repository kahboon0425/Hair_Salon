package assignmentccp;

public class Hairdresser extends Thread {

    Salon salon;
    private int hairdresserID;
    private boolean closingTime = false;


    public Hairdresser(Salon salon, int hairdresserID) {
        this.salon = salon;
        this.hairdresserID = hairdresserID;
    }

    public int getHairdresserID() {
        return hairdresserID;
    }

    public void setHairdresserID(int hairdresserID) {
        this.hairdresserID = hairdresserID;
    }

    public void run() {
        while (true) {
            salon.cutHair(this);

            synchronized (salon) {
                if (salon.isClosingTime()) {
                    System.out.println("Hairdresser " + hairdresserID + " is done for the day.");
                    System.out.println("Hairdresser "+hairdresserID+" go to sleep.");
                    break;
                }
            }
        }
    }

}
