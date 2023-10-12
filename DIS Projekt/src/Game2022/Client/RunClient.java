package Game2022.Client;

import Game2022.Extra.Player;
import Game2022.Server.Server;
import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class RunClient {
	private static Client client = null;
	private static Server server = null;
	private static final BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws Exception {
		System.out.println("---------------------------------------");
		startUp:
		while (true) { // startUp loop
			System.out.print("\"host\" / \"join\" / \"stop\": ");
			String string = inFromUser.readLine();
			try {
				switch (string.toLowerCase()) {
					case "host", "h" -> hostServer(); 	 // Starts new Server
					case "join", "j" -> joinServer(); 	 // Joins existing Server
					case "stop", "s" -> {break startUp;} // Stops the Application
					default -> {
						continue;
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				continue;
			}
			//--------------------- set UserName --------------------
			System.out.print("Type Your userName: ");
			String name = "";
			while (name.isBlank()) {
				name = inFromUser.readLine();
				if (name.isBlank()) System.out.println("Your userName is blank, try again.");
			}
			client.Send(name);
			//------------------ get Game2022.Server Information --------------
			System.out.println("Getting Server Information...");
			String[] playerStrings = client.read().split(",");
			for (String playerString : playerStrings) { // Loading Players
				String[] playerInfo = playerString.split(" ");
				Controller.players.add(new Player(Integer.parseInt(playerInfo[0]), playerInfo[1]));
			}
			System.out.println("Starting...");
			System.out.println("---------------------------------------");
			//------------------ Let the Games begin! ----------------
			//Starts receiving game updates from the server
			new Thread(client::receiver).start();
			//Starts the up the javafx Application
			Application.launch(Gui.class);
			//------------------ The games are over ----------------
			// Shuts down connections and ends the Loop.
			client.socket.close();
			if (server != null) server.close();
			break;
		}
	}

	public static void hostServer() throws IOException {
		server = new Server(8450);
		client = new Client(InetAddress.getLoopbackAddress().getHostAddress(), 8450);
	}

	public static void joinServer() throws IOException {
		while (client == null) {
			System.out.println("Insert Server-Address: ");
			String address = inFromUser.readLine();
			try {
				client = new Client(address,8450);
			} catch (IOException e){
				System.out.println("Could not connect to server with address: \"" + address + "\"");
			}
		}
	}

	public static Client getClient() {
		return client;
	}
}
