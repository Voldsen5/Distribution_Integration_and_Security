package Client.GUI;

import Client.Controller;
import Client.Player;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LogInScene {
    private static final TextField _port = new TextField("7928");
    private static final TextField _address = new TextField("10.0.0.127");
    private static final TextField _name = new TextField("");
    private static final ColorPicker _color = new ColorPicker();
    private static final Button _button = new Button("Join Server");
    private static final Group group = new Group(_port,_address, _name,_color,_button);
    private static final Scene scene = new Scene(group,500,300);

    public static Scene init() {
        _port.setPrefSize(150,25);
        _address.setPrefSize(150,25);
        _name.setPrefSize(150,25);
        _color.setPrefSize(150,25);
        _button.setPrefSize(120,40);
        _button.setOnAction(event -> {
            new Thread(LogInScene::joinServer).start();
            _button.setDisable(true);
        });
        update();
        return scene;
    }

    public static synchronized void update() {
        _port.setTranslateX(scene.getWidth()/2.-_port.getPrefWidth()/2.+100);
        _port.setTranslateY(scene.getHeight()/2.-_port.getPrefHeight()/2.-40);
        _address.setTranslateX(scene.getWidth()/2.-_address.getPrefWidth()/2.-100);
        _address.setTranslateY(scene.getHeight()/2.-_address.getPrefHeight()/2.-40);
        _name.setTranslateX(scene.getWidth()/2.- _name.getPrefWidth()/2.+100);
        _name.setTranslateY(scene.getHeight()/2.- _name.getPrefHeight()/2.-80);
        _color.setTranslateX(scene.getWidth()/2.- _color.getPrefWidth()/2.-100);
        _color.setTranslateY(scene.getHeight()/2.- _color.getPrefHeight()/2.-80);
        _button.setTranslateX(scene.getWidth()/2.-_button.getPrefWidth()/2.);
        _button.setTranslateY(scene.getHeight()/2.-_button.getPrefHeight()/2.);
    }

    private static void joinServer() {
        if (_name.getText().isBlank()) {
            System.out.println("Username is blank, name your self ANYTHING");
            return;
        }
        Player player;
        try {
            player = Controller.joinServer(Integer.parseInt(_port.getText()), _address.getText(), _name.getText(),_color.getValue());
        } catch (NumberFormatException e) {
            System.out.println(_port.getText());
            System.out.println("Port needs to be a number");
            _button.setDisable(false);
            return;
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Could not locate/join Server");
            _button.setDisable(false);
            return;
        }

        GUI.switchToGame(player);
    }
}
