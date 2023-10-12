import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
public class TCPServer {
	
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		String clientSentence;
		String capitalizedSentence;
		String sentence;
		ServerSocket welcomeSocket = new ServerSocket(6969);
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			outToClient.writeBytes(capitalizedSentence);
			ThreadServer threadServer = new ThreadServer(inFromClient);
			ThreadClient threadClient = new ThreadClient(inFromUser,outToClient);
			threadServer.start();
			threadClient.start();
		}
	}

}
