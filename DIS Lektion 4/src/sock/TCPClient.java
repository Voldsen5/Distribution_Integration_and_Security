package sock;
import java.io.*;
import java.net.*;

public class TCPClient {

	public static void main(String argv[]) throws Exception{
		String sentence;
		String modifiedSentence;
		String startbesked;

		boolean bananen = true;
		while (bananen) {
			BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
			Socket clientSocket = new Socket("10.10.138.191", 6969);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			sentence = inFromUser.readLine();
			startbesked = "Start " + sentence;
			outToServer.writeBytes(startbesked + '\n');
			modifiedSentence = inFromServer.readLine();
			System.out.println("FROM SERVER: " + modifiedSentence);
			if (!sentence.equals("nej")) {
				while(!sentence.contains(".disconnect")){
					sentence = inFromUser.readLine();
					outToServer.writeBytes(sentence + '\n');
					modifiedSentence = inFromServer.readLine();
					System.out.println("FROM SERVER: " + modifiedSentence);

				}
			}
		}
	}
}


