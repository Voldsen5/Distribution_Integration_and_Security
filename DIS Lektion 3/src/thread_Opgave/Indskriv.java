package thread_Opgave;

import java.util.Scanner;

public class Indskriv extends Thread {
    Common common;

    public Indskriv(Common common){
        this.common = common;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            common.setUdskrift(scanner.nextLine());
        }
    }

}
