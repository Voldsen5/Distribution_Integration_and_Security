import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {

	public static void main(String argv[]) throws Exception {
		String sentence;
		String modifiedSentence;
		String startbesked;

		while (true) {
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			Socket clientSocket = new Socket("10.10.139.79", 8008);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			sentence = inFromUser.readLine();
			startbesked = "Start " + sentence;
			outToServer.writeBytes(startbesked + '\n');
//			outToServer.writeBytes("HaHa");
//			outToServer.writeBytes("Hehe");
//			outToServer.writeBytes("HiHi");
//			outToServer.writeBytes("HoHo" + '\n');
			for (int i = 0; i < 100000; i++) {
				outToServer.writeBytes("8==D\n");
			}
			outToServer.writeBytes("Hej");
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			if (!sentence.equals("nej")) {
				ThreadServer threadServer = new ThreadServer(inFromServer);
				ThreadClient threadClient = new ThreadClient(inFromUser, outToServer);
				threadClient.start();
				threadServer.start();
			}
		}
	}
}


