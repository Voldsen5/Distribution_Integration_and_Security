package Client;

import Client.GUI.GameScene;
import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

public class Controller {
    private static Client client = null;

    public static Player joinServer(int port, String address, String name, Color color) throws IOException {
        int x = 250, y = 250;
        InetAddress inetAddress = InetAddress.getByName(address);
        client = new Client(port,inetAddress);
        client.TCP_Write(Storage.getUserID() + (char)256 + client.getUDPPort() + (char)256 + name + (char)256 + color.toString() + (char)256 + "" + x + "" + (char)256 + "" + y);
        System.out.println("Attempting to join " + inetAddress + " : " + port + "...");
        String[] serverData = client.TCP_Read().split(String.valueOf((char)257));
        System.out.println(Arrays.toString(serverData));
        Storage.setUserID(serverData[0]);
        for (int i = 2; i < serverData.length; i++) {
            if (serverData[i].isBlank()) continue;
            addPlayer(serverData[i]);
        }
        System.out.println("You son of a b*tch. I'm in.");
        System.out.println("----------------------------------------------------------------------------");
        new Thread(() -> client.UDPReceiver()).start();
        new Thread(() -> client.TCPReceiver()).start();
        try {
            Player player = new Player(Short.parseShort(serverData[1]),name,color);
            Platform.runLater(() -> player.setPos(x,y));
            return player;
        } catch (NumberFormatException n) {
            n.printStackTrace();
            return null;
        }
    }

    public static void leaveServer() {
        if (client != null) {
            client.stop();
        }
        //client.TCP_Write("X");
    }

    public static void write(String s) {
        if (client != null) {
            client.UDP_Write(s);
        }
    }

    public static boolean clientIsClosed() {
        return client.isClosed();
    }

    public static void addPlayer(String msg) {
        String[] data = msg.split(String.valueOf((char)256));
        System.out.println(Arrays.toString(data));
        try {
            Player player = new Player(Short.parseShort(data[0]), data[1], Color.valueOf(data[2]));
            Platform.runLater(() -> player.setPos(Double.parseDouble(data[3]), Double.parseDouble(data[4])));
            System.out.println("Say hi to " + player);
            GameScene.addPlayer(player);
        } catch (NumberFormatException s) {
            System.out.println("player id is invalid");
            s.printStackTrace();
        }
    }

    public static void removePlayer(String msg) {
        String[] data = msg.split(String.valueOf((char)256));
        System.out.println(Arrays.toString(data));
        try {
            System.out.println("Say goodbye to " + data[1]);
            GameScene.removePlayer(Integer.parseInt(data[0]));
        } catch (NumberFormatException s) {
            System.out.println("player id is invalid");
            s.printStackTrace();
        }
    }
}
