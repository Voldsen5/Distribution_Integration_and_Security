package Game2022.Client;

import java.io.*;
import java.net.*;

public class Client {
    BufferedReader in;
    PrintWriter out;
    Socket socket;

    public Client(String address, int port) throws IOException {
        socket = new Socket(address, port);
        System.out.println("Client - Connected to: " + address + ":" + port);
        // Input and output streams
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String read() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.out.println("Client - Server connection lost");
        }
        return null;
    }

    public void receiver() {
        try {
            while (!socket.isClosed()) {
                String msg = in.readLine();
                if (msg == null) continue;
                String[] data = msg.split(",", 2);
                switch (data[0]) {
                    case "J" -> Controller.playerJoin(data[1]);
                    case "L" -> Controller.playerLeft(data[1]);
                    case "S" -> Controller.startGame(data[1]);
                    case "M" -> Controller.movePlayer(data[1]);
                    case "O" -> Controller.updateTile(data[1]);
                    case "K" -> Controller.playerKilled(data[1]);
                    case "W" -> Controller.win(data[1]);
                    case "P" -> Controller.pointsAdd(data[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Client - Server connection lost");
        }
    }

    public void Send(String msg) {
        if (socket.isClosed()) {
            return;
        }
        out.println(msg);
    }
}
