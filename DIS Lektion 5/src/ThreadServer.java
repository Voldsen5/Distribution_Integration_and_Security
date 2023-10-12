import java.io.BufferedReader;
import java.io.IOException;

public class ThreadServer extends Thread {
    private BufferedReader inFromClient;
    public ThreadServer(BufferedReader inFromClient){
        this.inFromClient = inFromClient;
    }

    @Override
    public void run() {
        while (true) {
            String clientSentence;
            try {
                clientSentence = inFromClient.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(clientSentence);
        }
    }
}
