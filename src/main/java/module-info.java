module gamedev.Main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.base;

    opens gamedev to javafx.fxml;
    exports gamedev;
}
