package Client.GUI;

import Client.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
    private static Stage stage;

    private static Scene logIn;
    private static Scene game;

    @Override
    public void start(Stage stage) {
        GUI.stage = stage;
        logIn = LogInScene.init();

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            stage.getScene().getRoot().prefWidth(newVal.doubleValue() - oldVal.doubleValue());
            //System.out.println("X: " + stage.getScene().getWidth());
            if (stage.getScene() == logIn) {
                Platform.runLater(LogInScene::update);
            }
        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            //Platform.runLater(() -> stage.getScene().getRoot().prefHeight(newVal.doubleValue() - oldVal.doubleValue()));
            stage.getScene().getRoot().prefHeight(newVal.doubleValue() - oldVal.doubleValue());
            //System.out.println("Y: " + stage.getScene().getHeight());
            if (stage.getScene() == logIn) {
                Platform.runLater(LogInScene::update);
            }
        });

        stage.setTitle(":)");
        stage.setScene(logIn);
        stage.show();
    }


    public static void switchToGame(Player player) {
        Platform.runLater(() -> {
            game = GameScene.init(player);
            stage.setScene(game);
            GUI.getStage().centerOnScreen();
        });
        new Thread(GameScene::Updater).start();
    }

    public static Stage getStage() {
        return stage;
    }

    public GUI getGUI() {
        return this;
    }
}
