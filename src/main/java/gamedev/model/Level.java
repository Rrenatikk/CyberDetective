package gamedev.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class Level {
    private int number;
    private List<GameObject> objects;
    private Image backgroundImage;
    private ImageView backgroundView;

    public Level(int number, List<GameObject> objects, ImageView background) {
        this.number = number;
        this.objects = objects;
        this.backgroundImage = background.getImage();
        this.backgroundView = new ImageView(backgroundImage); // создаем копию
        this.backgroundView.setLayoutX(0);
        this.backgroundView.setLayoutY(0);
    }

    public int getLevelId() {
        return this.number;
    }


    public List<GameObject> getObjects() {
        return objects;
    }

    public ImageView getBackground() {
        // Возвращаем новый ImageView каждый раз, если нужно
        ImageView bg = new ImageView(backgroundImage);
        bg.setLayoutX(0);
        bg.setLayoutY(0);
        return bg;
    }
}
