package sock;
import java.net.*;
import java.io.*;
public class ServerThread extends Thread {
	Socket connSocket;
	common c;

	public ServerThread(Socket connSocket, common c) {
		this.connSocket = connSocket;
		this.c = c; // Til Web-server opgaven skal denne ikke anvendes
	}

	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());


			String clientSentence = inFromClient.readLine();
			String[] a = clientSentence.split(" ");
			String docnavn = a[1];

			outToClient.writeBytes("HTTP 200 OK\n" + ContentType(docnavn) + '\n' + "connection: close\n\n");

			outToClient.write(read("/Users/Kris/Documents/myWEB" + docnavn));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// do the work here
	}
	String ContentType(String docuname) {
		//returns the Content-Type headerline for a given document-name
		if (docuname.endsWith(".html")){
			return ("Content-Type: text/html");
		} else if (docuname.endsWith(".gif")) { return ("Content-Type: image/gif");
		} else if (docuname.endsWith(".png")) { return ("Content-Type: image/png");
		} else if (docuname.endsWith(".jpg")) { return ("Content-Type: image/jpg");
		} else if (docuname.endsWith(".js")) { return ("Content-Type: text/javascript");
		} else if (docuname.endsWith(".css")) { return ("Content-Type: text/css");
		} return ("Content-Type: text/plain");
	}
	byte[] read(String aInputFileName) throws FileNotFoundException{
		// returns the content of a file in a binary array
		System.out.println("Reading in binary file named : " +
				aInputFileName);
		File file = new File(aInputFileName); System.out.println("File size: " + file.length());
		byte[] result = new byte[(int)file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file)); while(totalBytesRead < result.length){
					int bytesRemaining = result.length - totalBytesRead;
					int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
					//input.read() returns -1, 0, or more :
					if (bytesRead > 0) {
						totalBytesRead = totalBytesRead + bytesRead;
					}
				}
				System.out.println("Num bytes read: " + totalBytesRead);
			}
			finally {
				System.out.println("Closing input stream.");
				input.close();
			}
		}
		catch (FileNotFoundException ex) {
			throw ex; }
		catch (IOException ex) { ex.printStackTrace();
		}
		return result; }
}



