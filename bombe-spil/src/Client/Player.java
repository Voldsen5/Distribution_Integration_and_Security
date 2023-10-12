package Client;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

public class Player extends Group {
    private final Short id;
    private String name;
    private Color color;
    private Shape shape;
    private final Text text;

    public Player(short id, String name, Color color) {
        this.id = id;
        shape = new Circle(10);
        text = new Text(-5,-15,this.name = name);
        shape.setFill(this.color = color);
        shape.setStroke(Color.BLACK);
        Platform.runLater(() -> {
            getChildren().add(shape);
            getChildren().add(text);
        });
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPos(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }

    @Override
    public String toString() {
        return name + "#" + id;
    }

    public String getName() {
        return name;
    }

    public Short getID() {
        return id;
    }
}
