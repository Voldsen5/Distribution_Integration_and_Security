import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public class ThreadClient extends Thread {
    private BufferedReader inFromUser;
    private DataOutputStream outToServer;

    public ThreadClient(BufferedReader inFromUser, DataOutputStream outToServer){
        this.inFromUser = inFromUser;
        this.outToServer = outToServer;
    }

    @Override
    public void run() {
        while (true){
            String sentence;
            try {
                sentence = inFromUser.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                outToServer.writeBytes(sentence + '\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
