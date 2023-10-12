package Game2022.Client;

import Game2022.Extra.Direction;
import Game2022.Extra.Pair;
import Game2022.Extra.Player;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class Gui extends Application {
    // ------------------------- java ----------------------------
    public static final int size = 30;
    public static final int scene_height = size * 20 + 50;
    public static final int scene_width = size * 20 + 200;
    public static char[][] map = null;

    public static void setMap(char[][] map) {
        Gui.map = map;
    }

    // ------------------------ javaFX ---------------------------
    public static Stage stage;
    private static final GridPane gameGrid = new GridPane();
    public static final GridPane lobbyGrid = new GridPane();
    public static final Scene gameScene = new Scene(gameGrid, scene_width, scene_height);
    public static final Scene lobbyScene = new Scene(lobbyGrid, 600, 400);


    //------------------------------------------------------------

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        initLobby();
        primaryStage.setScene(lobbyScene);
        primaryStage.show();
    }

    public static void switchScene() {
        Platform.runLater(() -> {
            initGame();
            stage.setScene(gameScene);
        });
    }

    // ------------------------- HudScene -------------------------

    static ListView<Player> playerListView = new ListView<>();

    private void initLobby() {
        ToggleButton button = new ToggleButton("Not Ready");
        button.setPrefSize(120, 50);
        button.setOnAction(actionEvent -> {
            button.setText(button.isSelected() ? "Ready" : "Not Ready");
            RunClient.getClient().Send("r");
        });

        playerListView.getItems().addAll(Controller.players);
        lobbyGrid.add(playerListView, 0, 0);
        lobbyGrid.add(button, 1, 0);
    }

    // ------------------------- GameScene -------------------------
    private static Label[][] fields;
    private static TextArea scoreList;
    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------
    public static Image image_floor;
    public static Image image_wall4, image_wall2;
    public static Image bomb, blast, blastUp, blastDown, blastLeft, blastRight, blastH, blastV;
    public static Image hero_right, hero_left, hero_up, hero_down;

    private static void initGame() {
        try {
            gameGrid.getChildren().clear();
            int w = map.length;
            int h = map[0].length;
            gameGrid.setHgap(10);
            gameGrid.setVgap(10);
            gameGrid.setPadding(new Insets(0, 10, 0, 10));

            Text mazeLabel = new Text("Maze:");
            mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text scoreLabel = new Text("Score:");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            scoreList = new TextArea();

            GridPane boardGrid = new GridPane();

            image_wall4 = new Image(Gui.class.getResourceAsStream("Image/wall4.png"), size, size, false, false);
            image_wall2 = new Image(Gui.class.getResourceAsStream("Image/wall2.png"), size, size, false, false);
            image_floor = new Image(Gui.class.getResourceAsStream("Image/floor1.png"), size, size, false, false);

            bomb = new Image(Gui.class.getResourceAsStream("Image/bomb.png"), size, size, false, false);
            blast = new Image(Gui.class.getResourceAsStream("Image/blast.png"), size, size, false, false);
            blastH = new Image(Gui.class.getResourceAsStream("Image/blastH.png"), size, size, false, false);
            blastV = new Image(Gui.class.getResourceAsStream("Image/blastV.png"), size, size, false, false);
            blastUp = new Image(Gui.class.getResourceAsStream("Image/blastUp.png"), size, size, false, false);
            blastDown = new Image(Gui.class.getResourceAsStream("Image/blastDown.png"), size, size, false, false);
            blastLeft = new Image(Gui.class.getResourceAsStream("Image/blastLeft.png"), size, size, false, false);
            blastRight = new Image(Gui.class.getResourceAsStream("Image/blastRight.png"), size, size, false, false);

            hero_right = new Image(Gui.class.getResourceAsStream("Image/heroRight.png"), size, size, false, false);
            hero_left = new Image(Gui.class.getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
            hero_up = new Image(Gui.class.getResourceAsStream("Image/heroUp.png"), size, size, false, false);
            hero_down = new Image(Gui.class.getResourceAsStream("Image/heroDown.png"), size, size, false, false);

            fields = new Label[w][h];
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    fields[x][y] = switch (map[x][y]) {
                        case 'w' -> new Label("", new ImageView(image_wall2));
                        case 'r' -> new Label("", new ImageView(image_wall4));
                        case ' ' -> new Label("", new ImageView(image_floor));
                        default -> new Label();
                    };
                    boardGrid.add(fields[x][y], x, y);
                }
            }
            scoreList.setEditable(false);

            gameGrid.add(mazeLabel, 0, 0);
            gameGrid.add(scoreLabel, 1, 0);
            gameGrid.add(boardGrid, 0, 1);
            gameGrid.add(scoreList, 1, 1);

            gameScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                switch (event.getCode()) {
                    case W -> RunClient.getClient().Send("W");
                    case S -> RunClient.getClient().Send("S");
                    case A -> RunClient.getClient().Send("A");
                    case D -> RunClient.getClient().Send("D");
                    case SPACE -> RunClient.getClient().Send("B");
                    case ESCAPE -> System.exit(0);
                }
            });

            // Putting default players on screen
            for (Player player : Controller.players) {
                fields[player.getXPos()][player.getYPos()].setGraphic(new ImageView(hero_up));
            }
            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //------------------------- Other Methods ----------------------------

    public static void updateField(Pair pos) {
        Platform.runLater(() -> fields[pos.getX()][pos.getY()].setGraphic(switch (map[pos.getX()][pos.getY()]) {
            case 'w' -> new ImageView(image_wall2);
            case 'r' -> new ImageView(image_wall4);
            case ' ' -> new ImageView(image_floor);
            case '0' -> new ImageView(bomb);
            case '1' -> new ImageView(blast);
            case '2' -> new ImageView(blastH);
            case '3' -> new ImageView(blastV);
            case '4' -> new ImageView(blastUp);
            case '5' -> new ImageView(blastDown);
            case '6' -> new ImageView(blastLeft);
            case '7' -> new ImageView(blastRight);
            default -> null;
        }));
    }

    public static void addPlayer(Player player) {
        Platform.runLater(() -> playerListView.getItems().add(player));
    }

    public static void removePlayer(Player player) {
        Platform.runLater(() -> playerListView.getItems().remove(player));
    }

    public static void win(String s) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Tillykke du vandt! " + s);
            alert.show();
        });
    }


    public static void lost(String s) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Du tabte desværre, " + s + " prøv igen!");
            alert.show();
        });
    }


    public static void placePlayerOnScreen(Pair newPos, Direction direction) {
        Platform.runLater(() -> fields[newPos.getX()][newPos.getY()].setGraphic(
                switch (direction) {
                    case Up -> new ImageView(hero_up);
                    case Down -> new ImageView(hero_down);
                    case Left -> new ImageView(hero_left);
                    case Right -> new ImageView(hero_right);
                })
        );
    }

    public static void movePlayerOnScreen(Pair oldPos, Pair newPos, Direction direction) {
        updateField(oldPos);
        placePlayerOnScreen(newPos, direction);
    }

    public static void updateScoreTable() {
        Platform.runLater(() -> scoreList.setText(getScoreList()));
    }

    public static String getScoreList() {
        StringBuilder b = new StringBuilder();
        for (Player p : Controller.players) {
            b.append(String.format("%s %d wins\r\n",p.getName(),p.getPoints()));
        }
        return b.toString();
    }
}

