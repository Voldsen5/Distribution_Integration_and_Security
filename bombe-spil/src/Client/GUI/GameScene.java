package Client.GUI;

import Client.Controller;
import Client.Player;
import Client.Storage;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;

import java.util.ArrayList;

public class GameScene {
    private static boolean w, a, s, d;
    private static final double movementSpeed = 2.5;
    private static Player player;
    private static final ArrayList<Player> players = new ArrayList<>();
    private static final Group group = new Group();
    private static final Scene scene = new Scene(group,500,400);

    public static Scene init(Player player) {
        group.getChildren().add(GameScene.player = player);
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W -> w = true;
                case A -> a = true;
                case S -> s = true;
                case D -> d = true;
            }
        });
        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W -> w = false;
                case A -> a = false;
                case S -> s = false;
                case D -> d = false;
            }
        });
        return scene;
    }


    public static void Updater() {
        while (!Controller.clientIsClosed()) {
            //System.out.println(w + " " + a + " " + s + " " + d);
            userControls();
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void userControls() {
        if (w) Platform.runLater(() -> player.setTranslateY(player.getTranslateY() - movementSpeed));
        if (a) Platform.runLater(() -> player.setTranslateX(player.getTranslateX() - movementSpeed));
        if (s) Platform.runLater(() -> player.setTranslateY(player.getTranslateY() + movementSpeed));
        if (d) Platform.runLater(() -> player.setTranslateX(player.getTranslateX() + movementSpeed));
        if (w||a||s||d) {
            //System.out.println(thisUser.getTranslateX() + " : " + thisUser.getTranslateY());
            Controller.write(Storage.getUserID() + (char)256 + player.getTranslateX() + (char)256 + player.getTranslateY());
        }
    }

    public static void addPlayer(Player player) {
        players.add(player);
        Platform.runLater(() -> group.getChildren().add(player));
    }

    public static void removePlayer(int id) {
        for (Player oldPlayer : players) {
            if (id == oldPlayer.getID()) {
                players.remove(oldPlayer);
                Platform.runLater(() -> group.getChildren().remove(oldPlayer));
                break;
            }
        }
    }

    public static void updatePlayerPos(short id, double x, double y) {
        for (Player player : players) {
            if (player.getID() == id) {
                System.out.println(x+" "+y);
                Platform.runLater(() -> player.setPos(x,y));
            }
        }

    }
}
