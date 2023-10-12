package Client;

import Client.GUI.GameScene;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Client {
    private Socket tcpSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private DatagramSocket datagramSocket;
    private final InetAddress address;

    public Client(int port, InetAddress address) throws IOException {
         this.address = address;
        tcpSocket = new Socket(address, port);
        datagramSocket = new DatagramSocket();
        in = new DataInputStream(tcpSocket.getInputStream());
        out = new DataOutputStream(tcpSocket.getOutputStream());

    }

    public void stop() {
        try {
            tcpSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection can't be stoped...");
        }
    }

    public int getUDPPort() {
        return datagramSocket.getLocalPort();
    }

    public String TCP_Read() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            System.out.println("Server Connection lost");
            stop();
        }
        return "Error";
    }

    public void TCP_Write(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            System.out.println("x3");
            e.printStackTrace();
        }
    }

    public void TCPReceiver() {
        while (!tcpSocket.isClosed()) {
            String msg = TCP_Read();
            switch (msg.charAt(0)) {
                case '0' -> Controller.addPlayer(msg.substring(1));
                case '1' -> Controller.removePlayer(msg.substring(1));
                default -> System.out.println(msg);
            }
        }
    }

    public void UDPReceiver() {
        while (!tcpSocket.isClosed()) {
            String[] msg = UDP_Read().split(String.valueOf((char) 256));
            //System.out.println(Arrays.toString(msg));
            try {
                GameScene.updatePlayerPos(Short.parseShort(msg[0]), Double.parseDouble(msg[1]), Double.parseDouble(msg[2]));
            } catch (NumberFormatException s) {
                System.out.println(Arrays.toString(msg));
                System.out.println("udp - player id is invalid");
                s.printStackTrace();
            }
        }
    }

    public String UDP_Read() {
        byte[] buffer = new byte[50];
        try {
            DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
            datagramSocket.receive(packet);
            return new String(packet.getData(),0, packet.getLength());
        } catch (IOException e) {
            System.out.println("x2");
            e.printStackTrace();
        }
        return "";
    }

    public void UDP_Write(String msg) {
        byte[] buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer,buffer.length,address,tcpSocket.getPort());
        try {
            datagramSocket.send(packet);
        } catch (IOException e) {
            System.out.println("x1");
            e.printStackTrace();
        }
    }


    public boolean isClosed() {
        return tcpSocket.isClosed();
    }
}
