package gamedev.model;

import javafx.scene.image.ImageView;
import java.util.List;

public class Level {
    private int number;
    private List<GameObject> objects;
    private ImageView background;

    public Level(int number, List<GameObject> objects, ImageView background) {
        this.number = number;
        this.objects = objects;
        this.background = background;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    public ImageView getBackground() {
        return background;
    }
}
