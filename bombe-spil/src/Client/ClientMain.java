package Client;

import Client.GUI.GUI;

public class ClientMain {
    public static void main(String[] args) {
        Storage.load();
        GUI.launch(GUI.class);
        Controller.leaveServer();
        //Storage.save();
        System.out.println("Main Thread dead -");
    }
}
