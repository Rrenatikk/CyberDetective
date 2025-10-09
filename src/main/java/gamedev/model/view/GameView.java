package gamedev.model.view;

import gamedev.controller.GameController;
import gamedev.model.GameObject;
import gamedev.model.Level;
import javafx.animation.ScaleTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameView {
    private GameController controller;
    private Pane root;

    private ImageView confirmDialog;
    private ImageView confirmYes;
    private ImageView confirmNo;
    private boolean isConfirmDialogVisible = false;

    // UI elements
    private ImageView logout;
    private ImageView reset;
    private ImageView volume;

    public GameView(GameController controller) {
        this.controller = controller;
    }

    public void start(Stage stage) {
        //########################################/ UI /########################################//

        Image ui_sideImage = new Image(getClass().getResource("/ui/ui_side.png").toExternalForm());
        ImageView ui_side_left = new ImageView(ui_sideImage);
        ImageView ui_side_right = new ImageView(ui_sideImage);

        Image logoImage = new Image(getClass().getResource("/ui/logo.png").toExternalForm());
        ImageView logo = new ImageView(logoImage);

        Image logoutImage = new Image(getClass().getResource("/ui/logout.png").toExternalForm());
        logout = new ImageView(logoutImage);

        Image volumeImage = new Image(getClass().getResource("/ui/volume_on.png").toExternalForm());
        volume = new ImageView(volumeImage);

        Image resetImage = new Image(getClass().getResource("/ui/reset.png").toExternalForm());
        reset = new ImageView(resetImage);

        Image scoreImage = new Image(getClass().getResource("/ui/score.png").toExternalForm());
        ImageView score = new ImageView(scoreImage);

        //########################################/ CONFIRM DIALOG /########################################//

        // Main confirm window
        Image confirmDialogImage = new Image(getClass().getResource("/ui/confirm_dialog.png").toExternalForm());
        confirmDialog = new ImageView(confirmDialogImage);
        confirmDialog.setVisible(false);

        // "Yes" button
        Image confirmYesImage = new Image(getClass().getResource("/ui/confirm_yes.png").toExternalForm());
        confirmYes = new ImageView(confirmYesImage);
        confirmYes.setVisible(false);

        // "No" button
        Image confirmNoImage = new Image(getClass().getResource("/ui/confirm_no.png").toExternalForm());
        confirmNo = new ImageView(confirmNoImage);
        confirmNo.setVisible(false);

        //########################################/ Pane /########################################//

        root = new Pane();

        Level level = controller.getCurrentLevel();
        ImageView background = level.getBackground();
        root.getChildren().add(background);

        for (GameObject obj : level.getObjects()) {
            obj.getImage().setOnMouseClicked(e -> controller.onObjectClicked(obj));
            root.getChildren().add(obj.getImage());
        }

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        stage.setTitle("Hidden Objects Game");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();

        //########################################/ UI Placement /########################################//
        background.setLayoutX(0);
        background.setLayoutY(0);

        ui_side_left.setLayoutX(0);
        ui_side_left.setLayoutY(0);
        root.getChildren().add(ui_side_left);

        ui_side_right.setLayoutX(1500);
        ui_side_right.setLayoutY(0);
        root.getChildren().add(ui_side_right);

        score.setLayoutX(635);
        score.setLayoutY(0);
        root.getChildren().add(score);

        logo.setLayoutX(1500);
        logo.setLayoutY(0);
        root.getChildren().add(logo);

        logout.setLayoutX(0);
        logout.setLayoutY(0);
        root.getChildren().add(logout);

        reset.setLayoutX(140);
        reset.setLayoutY(0);
        root.getChildren().add(reset);

        volume.setLayoutX(280);
        volume.setLayoutY(0);
        root.getChildren().add(volume);

        root.getChildren().addAll(confirmDialog, confirmYes, confirmNo);

        setupButtonAnimation(logout);
        setupButtonAnimation(reset);
        setupButtonAnimation(volume);
        setupButtonAnimation(confirmYes);
        setupButtonAnimation(confirmNo);

        //########################################/ LOGOUT BUTTON /########################################//
        logout.setOnMouseClicked(e -> {
            showConfirmDialog();
        });

        //########################################/ CONFIRM DIALOG BUTTONS /########################################//
        confirmYes.setOnMouseClicked(e -> {
            // If "yes" selected
            controller.exitGame(stage);
        });

        confirmNo.setOnMouseClicked(e -> {
            // If "no" selected
            hideConfirmDialog();
        });

        //########################################/ MUTE BUTTON /########################################//
        volume.setOnMouseClicked(e -> {
            controller.toggleMute();
            if (controller.isMuted()) {
                volume.setImage(new Image(getClass().getResource("/ui/mute.png").toExternalForm()));
            } else {
                volume.setImage(new Image(getClass().getResource("/ui/volume_on.png").toExternalForm()));
            }
        });
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    private void showConfirmDialog() {
        isConfirmDialogVisible = true;

        // Dialog menu center position
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double dialogWidth = 650; // Ширина вашего диалога
        double dialogHeight = 300; // Высота вашего диалога

        double x = (screenBounds.getWidth() - dialogWidth) / 2;
        double y = (screenBounds.getHeight() - dialogHeight) / 2;

        confirmDialog.setLayoutX(x);
        confirmDialog.setLayoutY(y);
        confirmDialog.setVisible(true);

        // In dialog menu buttons positions
        confirmYes.setLayoutX(x);
        confirmYes.setLayoutY(y + 180);
        confirmYes.setVisible(true);

        confirmNo.setLayoutX(x + 325);
        confirmNo.setLayoutY(y + 180);
        confirmNo.setVisible(true);

        // Block game interaction
        setGameInteraction(false);
    }

    private void hideConfirmDialog() {
        isConfirmDialogVisible = false;
        confirmDialog.setVisible(false);
        confirmYes.setVisible(false);
        confirmNo.setVisible(false);

        // continue game interaction
        setGameInteraction(true);
    }

    private void setGameInteraction(boolean enabled) {
        // Block/unblock clicks on game objects
        Level level = controller.getCurrentLevel();
        for (GameObject obj : level.getObjects()) {
            obj.getImage().setDisable(!enabled);
        }

        // Block other UI elements
        logout.setDisable(!enabled);
        reset.setDisable(!enabled);
        volume.setDisable(!enabled);
    }

    private void setupButtonAnimation(ImageView button) {

        // Shadow effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(javafx.scene.paint.Color.rgb(0, 0, 0, 0.4));
        shadow.setRadius(15);
        shadow.setSpread(0.15);
        shadow.setOffsetX(0);
        shadow.setOffsetY(6);

        // Scale animation
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);

        // Rise animation
        javafx.animation.TranslateTransition liftTransition =
                new javafx.animation.TranslateTransition(Duration.millis(100), button);
        liftTransition.setToY(0);

        button.setOnMouseEntered(e -> {
            button.setEffect(shadow);
            scaleTransition.playFromStart();
            liftTransition.playFromStart();
            button.toFront(); // Важно: выводим на передний план
        });

        button.setOnMouseExited(e -> {
            scaleTransition.stop();
            liftTransition.stop();
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            button.setTranslateY(0);
            button.setEffect(null);
        });
    }
}