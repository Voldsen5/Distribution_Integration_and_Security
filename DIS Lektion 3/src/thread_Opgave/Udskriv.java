package thread_Opgave;

public class Udskriv extends Thread {
    Common common;
    public Udskriv(Common common){
        this.common = common;
        
    }

    public void run() {
        while (true){
            System.out.println(common.getUdskrift());
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

