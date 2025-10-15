package gamedev.model;

import javafx.scene.image.ImageView;

public class GameObject {
    private String name;
    private boolean isThreat;
    private ThreatType threatType;
    private ImageView image;
    private double x, y;
    private boolean found = false;

    public GameObject(String name, boolean isThreat, ThreatType threatType, ImageView image, double x, double y) {
        this.name = name;
        this.isThreat = isThreat;
        this.threatType = threatType;
        this.image = image;
        this.x = x;
        this.y = y;

        this.image.setLayoutX(x);
        this.image.setLayoutY(y);
    }

    public boolean isThreat() { return isThreat; }
    public ThreatType getThreatType() { return threatType; }
    public ImageView getImage() { return image; }
    public String getName() { return name; }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }
}
