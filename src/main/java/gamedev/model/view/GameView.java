package gamedev.model.view;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import gamedev.controller.GameController;
import gamedev.model.GameObject;
import gamedev.model.Level;
import gamedev.model.SoundPlayer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
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
    private ParallelTransition confirmPulseAnimation;

    private Text scoreText;

    // UI elements
    private ImageView logout;
    private ImageView music;
    private ImageView volume;

    // Панель історії повідомлень з прокруткою
    private VBox messageHistoryBox;
    private ScrollPane messageScrollPane;

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

        Image musicImage = new Image(getClass().getResource("/ui/music_on.png").toExternalForm());
        music = new ImageView(musicImage);

        Image scoreImage = new Image(getClass().getResource("/ui/score.png").toExternalForm());
        ImageView score = new ImageView(scoreImage);

        //########################################/ CONFIRM DIALOG /########################################//
        Image confirmDialogImage = new Image(getClass().getResource("/ui/confirm_dialog.png").toExternalForm());
        confirmDialog = new ImageView(confirmDialogImage);
        confirmDialog.setVisible(false);

        Image confirmYesImage = new Image(getClass().getResource("/ui/confirm_yes.png").toExternalForm());
        confirmYes = new ImageView(confirmYesImage);
        confirmYes.setVisible(false);

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

        //########################################/ CUSTOM CURSOR /########################################//
        try {
            Image cursorImage = new Image(getClass().getResource("/ui/cursor.png").toExternalForm());
            ImageCursor customCursor = new ImageCursor(cursorImage, cursorImage.getWidth() / 2, cursorImage.getHeight() / 2);
            scene.setCursor(customCursor);
        } catch (Exception e) {
            System.err.println("Не удалось загрузить кастомный курсор: " + e.getMessage());
        }

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

        music.setLayoutX(140);
        music.setLayoutY(0);
        root.getChildren().add(music);

        volume.setLayoutX(280);
        volume.setLayoutY(0);
        root.getChildren().add(volume);

        //########################################/ SCORE TEXT /########################################//
        scoreText = new Text("0/" + controller.getTotalThreats());
        scoreText.setFont(Font.font("Poppins", FontWeight.BLACK, 64));
        scoreText.setFill(Color.WHITE);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0, 0, 0, 0.25));
        dropShadow.setOffsetY(4.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setRadius(4.0);
        scoreText.setEffect(dropShadow);

        scoreText.setLayoutX(1155);
        scoreText.setLayoutY(90);
        root.getChildren().add(scoreText);
        root.getChildren().addAll(confirmDialog, confirmYes, confirmNo);

        //########################################/ MESSAGE HISTORY PANEL /########################################//
        messageHistoryBox = new VBox(10);
        messageHistoryBox.setPrefWidth(200);
        messageHistoryBox.setStyle(
                "-fx-background-color: rgba(0,0,0,0.3); -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5;"
        );

        messageScrollPane = new ScrollPane(messageHistoryBox);
        messageScrollPane.setLayoutX(10);
        messageScrollPane.setLayoutY(150);
        messageScrollPane.setPrefHeight(900);
        messageScrollPane.setPrefWidth(400);
        messageScrollPane.setFitToWidth(true);
        messageScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        root.getChildren().add(messageScrollPane);

        setupButtonAnimation(logout);
        setupButtonAnimation(music);
        setupButtonAnimation(volume);
        setupButtonAnimation(confirmYes);
        setupButtonAnimation(confirmNo);

        //########################################/ BUTTON ACTIONS /########################################//
        logout.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            showConfirmDialog();
        });

        confirmYes.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            controller.exitGame(stage);
        });

        confirmNo.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            hideConfirmDialog();
        });

        music.setOnMouseClicked(e -> {
            controller.toggleMute();
            SoundPlayer.play("button_clicked.mp3");
            if (controller.isMuted()) {
                music.setImage(new Image(getClass().getResource("/ui/music_off.png").toExternalForm()));
            } else {
                music.setImage(new Image(getClass().getResource("/ui/music_on.png").toExternalForm()));
            }
        });

        volume.setOnMouseClicked(e -> {
            controller.toggleSoundEffects();
            SoundPlayer.play("button_clicked.mp3");
            boolean muted = controller.areSoundEffectsMuted();
            SoundPlayer.setMuted(muted);
            if (muted) {
                volume.setImage(new Image(getClass().getResource("/ui/mute.png").toExternalForm()));
            } else {
                volume.setImage(new Image(getClass().getResource("/ui/volume_on.png").toExternalForm()));
            }
        });

        //########################################/ COPYRIGHT /########################################//
        Text copyrightText = new Text("Developed for CyberField NeT");
        copyrightText.setFont(Font.font("Poppins", FontWeight.BOLD, 26));
        copyrightText.setFill(Color.SADDLEBROWN);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.color(0, 0, 0, 0.6));
        shadow.setOffsetX(2);
        shadow.setOffsetY(2);
        shadow.setRadius(3);
        copyrightText.setEffect(shadow);

        double paddingRight = 20;
        double paddingBottom = 20;
        copyrightText.setLayoutX(screenBounds.getWidth() - copyrightText.getBoundsInLocal().getWidth() - paddingRight);
        copyrightText.setLayoutY(screenBounds.getHeight() - paddingBottom);

        root.getChildren().add(copyrightText);
    }


    //########################################/ MESSAGE HISTORY /########################################//
    public void showMessage(String message) {
        Text msgText = new Text(message);
        msgText.setFont(Font.font("Poppins", FontWeight.NORMAL, 18));
        msgText.setWrappingWidth(messageHistoryBox.getPrefWidth() - 20);

        // Колір за змістом
        if (message.startsWith("Загрозу знайдено")) {
            msgText.setFill(Color.RED);
        } else if (message.startsWith("Цю загрозу вже було знайдено!")) {
            msgText.setFill(Color.GOLD);
        } else if (message.startsWith("Цей об'єкт є безпечним!")) {
            msgText.setFill(Color.LIME);
        } else {
            msgText.setFill(Color.WHITE);
        }

        messageHistoryBox.getChildren().add(0, msgText);

        // Автопрокрутка вверх
        messageScrollPane.layout();
        messageScrollPane.setVvalue(0);
    }

    //########################################/ CONFIRM DIALOG /########################################//
    private void showConfirmDialog() {
        isConfirmDialogVisible = true;
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double dialogWidth = 650;
        double dialogHeight = 300;
        double x = (screenBounds.getWidth() - dialogWidth) / 2;
        double y = (screenBounds.getHeight() - dialogHeight) / 2;

        confirmDialog.setLayoutX(x);
        confirmDialog.setLayoutY(y);
        confirmYes.setLayoutX(x + 10);
        confirmYes.setLayoutY(y + 180);
        confirmNo.setLayoutX(x + 335);
        confirmNo.setLayoutY(y + 180);

        confirmDialog.setVisible(true);
        confirmYes.setVisible(true);
        confirmNo.setVisible(true);
        setGameInteraction(false);

        confirmDialog.setOpacity(0);
        confirmDialog.setScaleX(0.8);
        confirmDialog.setScaleY(0.8);
        confirmDialog.setTranslateY(40);

        confirmYes.setOpacity(0);
        confirmNo.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), confirmDialog);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), confirmDialog);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        TranslateTransition moveUp = new TranslateTransition(Duration.millis(300), confirmDialog);
        moveUp.setFromY(40);
        moveUp.setToY(0);

        FadeTransition fadeYes = new FadeTransition(Duration.millis(250), confirmYes);
        fadeYes.setFromValue(0);
        fadeYes.setToValue(1);

        FadeTransition fadeNo = new FadeTransition(Duration.millis(250), confirmNo);
        fadeNo.setFromValue(0);
        fadeNo.setToValue(1);

        ParallelTransition dialogAppear = new ParallelTransition(fadeIn, scaleIn, moveUp);
        javafx.animation.SequentialTransition total =
                new javafx.animation.SequentialTransition(dialogAppear, new ParallelTransition(fadeYes, fadeNo));
        total.play();
    }

    private void hideConfirmDialog() {
        isConfirmDialogVisible = false;
        confirmDialog.setVisible(false);
        confirmYes.setVisible(false);
        confirmNo.setVisible(false);
        setGameInteraction(true);
    }

    private void setGameInteraction(boolean enabled) {
        Level level = controller.getCurrentLevel();
        for (GameObject obj : level.getObjects()) {
            obj.getImage().setDisable(!enabled);
        }
        logout.setDisable(!enabled);
        music.setDisable(!enabled);
        volume.setDisable(!enabled);
    }

    private void setupButtonAnimation(ImageView button) {
        button.setCursor(Cursor.HAND); // стандартная рука для кнопок

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        shadow.setRadius(15);
        shadow.setSpread(0.15);
        shadow.setOffsetX(0);
        shadow.setOffsetY(6);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);

        TranslateTransition liftTransition = new TranslateTransition(Duration.millis(100), button);
        liftTransition.setToY(0);

        button.setOnMouseEntered(e -> {
            button.setEffect(shadow);
            scaleTransition.playFromStart();
            liftTransition.playFromStart();
            button.toFront();
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

    public void updateScore(int found, long total) {
        scoreText.setText(found + "/" + total);

        if (found == total) {
            showEndMessage("Ти молодець!\nРада по кібербезпеці: Не відкривай підозрілі посилання та використовуй складні паролі.");
        }
    }


    public void showTipImage(String imageName, double x, double y) {
        try {
            Image tipImage = new Image(getClass().getResource("/tips/" + imageName).toExternalForm());
            ImageView tipView = new ImageView(tipImage);
            tipView.setLayoutX(x);
            tipView.setLayoutY(y - tipImage.getHeight() - 50);
            root.getChildren().add(tipView);

            FadeTransition ft = new FadeTransition(Duration.millis(1000), tipView);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> root.getChildren().remove(tipView));
            ft.setDelay(Duration.millis(500));
            ft.play();
        } catch (Exception e) {
            System.err.println("Ошибка загрузки изображения подсказки: /tips/" + imageName + " " + e.getMessage());
        }
    }
    private void showEndMessage(String message) {
        Text endText = new Text(message);
        endText.setFont(Font.font("Poppins", FontWeight.BOLD, 24));
        endText.setFill(Color.LIME);
        endText.setWrappingWidth(300);

        // Позиция справа
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        endText.setLayoutX(screenBounds.getWidth() - 400); // справа
        endText.setLayoutY(500);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0, 0, 0, 0.7));
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);
        dropShadow.setRadius(3);
        endText.setEffect(dropShadow);

        root.getChildren().add(endText);

        FadeTransition ft = new FadeTransition(Duration.seconds(100), endText);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> root.getChildren().remove(endText));
        ft.play();
    }

}
