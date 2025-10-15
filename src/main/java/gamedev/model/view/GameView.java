package gamedev.model.view;

import gamedev.controller.GameController;
import gamedev.model.GameObject;
import gamedev.model.Level;
import gamedev.model.SoundPlayer;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.ParallelTransition;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;



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

        music.setLayoutX(140);
        music.setLayoutY(0);
        root.getChildren().add(music);

        volume.setLayoutX(280);
        volume.setLayoutY(0);
        root.getChildren().add(volume);

        // Инициализация текста счета
        scoreText = new Text("0/" + controller.getTotalThreats());
        scoreText.setFont(Font.font("Poppins", FontWeight.BLACK, 64));
        scoreText.setFill(Color.WHITE);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0, 0, 0, 0.25));
        dropShadow.setOffsetY(4.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setRadius(4.0);
        scoreText.setEffect(dropShadow);

        // Размещаем текст поверх картинки score.png (подберите координаты)
        scoreText.setLayoutX(1155); // Примерная координата X
        scoreText.setLayoutY(90);  // Примерная координата Y
        root.getChildren().add(scoreText);

        root.getChildren().addAll(confirmDialog, confirmYes, confirmNo);

        setupButtonAnimation(logout);
        setupButtonAnimation(music);
        setupButtonAnimation(volume);
        setupButtonAnimation(confirmYes);
        setupButtonAnimation(confirmNo);

        //########################################/ LOGOUT BUTTON /########################################//
        logout.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            showConfirmDialog();
        });

        //########################################/ CONFIRM DIALOG BUTTONS /########################################//
        confirmYes.setOnMouseClicked(e -> {
            // If "yes" selected
            SoundPlayer.play("button_clicked.mp3");
            controller.exitGame(stage);
        });

        confirmNo.setOnMouseClicked(e -> {
            // If "no" selected
            SoundPlayer.play("button_clicked.mp3");
            hideConfirmDialog();
        });

        //########################################/ MUSIC OFF BUTTON /########################################//
        music.setOnMouseClicked(e -> {
            controller.toggleMute();
            SoundPlayer.play("button_clicked.mp3");
            if (controller.isMuted()) {
                music.setImage(new Image(getClass().getResource("/ui/music_off.png").toExternalForm()));
            } else {
                music.setImage(new Image(getClass().getResource("/ui/music_on.png").toExternalForm()));
            }
        });

        //########################################/ SOUNDS OFF BUTTON /########################################//
        volume.setOnMouseClicked(e -> {
            controller.toggleSoundEffects();
            SoundPlayer.play("button_clicked.mp3"); // играет только если звуки ещё включены

            boolean muted = controller.areSoundEffectsMuted();
            SoundPlayer.setMuted(muted); // обновляем состояние в SoundPlayer

            if (muted) {
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

        // Центрирование диалога
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

        // Блокируем остальной интерфейс
        setGameInteraction(false);

        // ----------- ЭФФЕКТ ПОЯВЛЕНИЯ ----------- //
        confirmDialog.setOpacity(0);
        confirmDialog.setScaleX(0.8);
        confirmDialog.setScaleY(0.8);
        confirmDialog.setTranslateY(40);

        confirmYes.setOpacity(0);
        confirmNo.setOpacity(0);

        // Анимация окна
        javafx.animation.FadeTransition fadeIn = new javafx.animation.FadeTransition(Duration.millis(300), confirmDialog);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        javafx.animation.ScaleTransition scaleIn = new javafx.animation.ScaleTransition(Duration.millis(300), confirmDialog);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        javafx.animation.TranslateTransition moveUp = new javafx.animation.TranslateTransition(Duration.millis(300), confirmDialog);
        moveUp.setFromY(40);
        moveUp.setToY(0);

        // Кнопки "Да" и "Нет" появляются чуть позже
        javafx.animation.FadeTransition fadeYes = new javafx.animation.FadeTransition(Duration.millis(250), confirmYes);
        fadeYes.setFromValue(0);
        fadeYes.setToValue(1);

        javafx.animation.FadeTransition fadeNo = new javafx.animation.FadeTransition(Duration.millis(250), confirmNo);
        fadeNo.setFromValue(0);
        fadeNo.setToValue(1);

        // Группируем всё
        javafx.animation.ParallelTransition dialogAppear = new javafx.animation.ParallelTransition(fadeIn, scaleIn, moveUp);
        javafx.animation.SequentialTransition total = new javafx.animation.SequentialTransition(dialogAppear, new javafx.animation.ParallelTransition(fadeYes, fadeNo));

        total.play();
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
        music.setDisable(!enabled);
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

    public void updateScore(int found, long total) {
        scoreText.setText(found + "/" + total);
    }

    public void showTipImage(String imageName, double x, double y) {
        try {
            // 1. Загружаем изображение подсказки
            Image tipImage = new Image(getClass().getResource("/tips/" + imageName).toExternalForm());
            ImageView tipView = new ImageView(tipImage);

            // 2. Устанавливаем положение: над объектом
            // Вы можете настроить смещение (например, -50), чтобы поднять подсказку над объектом.
            tipView.setLayoutX(x);
            tipView.setLayoutY(y - tipImage.getHeight() - 50); // Размещаем над объектом

            // 3. Добавляем в корневой Pane
            root.getChildren().add(tipView);

            // 4. (ОПЦИОНАЛЬНО) Добавляем анимацию исчезновения
            // Создаем анимацию исчезновения (Fade Out)
            FadeTransition ft = new FadeTransition(Duration.millis(1000), tipView);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);

            // По завершении анимации удаляем подсказку с экрана
            ft.setOnFinished(e -> root.getChildren().remove(tipView));

            // Анимация начинается через 500 мс (задержка перед исчезновением)
            ft.setDelay(Duration.millis(500));
            ft.play();

        } catch (Exception e) {
            System.err.println("Ошибка загрузки изображения подсказки: /tips/" + imageName + " " + e.getMessage());
        }
    }
}