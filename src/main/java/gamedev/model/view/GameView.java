package gamedev.model.view;

import gamedev.model.Game;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import gamedev.controller.GameController;
import gamedev.model.GameObject;
import gamedev.model.Level;
import gamedev.model.SoundPlayer;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameView {
    private GameController controller;
    private Pane root;

    private ImageView confirmDialog;
    private ImageView confirmYes;
    private ImageView confirmNo;
    private boolean isConfirmDialogVisible = false;
    private ParallelTransition confirmPulseAnimation;

    private ImageView endDialog;
    private ImageView endPlayAgain;
    private ImageView endExit;
    private boolean isEndDialogVisible = false;

    private Rectangle darkOverlay;


    private Text scoreText;

    // UI elements
    private ImageView logout;
    private ImageView music;
    private ImageView volume;

    // Панель історії повідомлень з прокруткою
    private VBox messageHistoryBox;
    private ScrollPane messageScrollPane;
    private MediaPlayer mediaPlayer;

//    public GameView(GameController controller) {
//        this.controller = controller;
//        this.stage = stage;
//        this.root = new Pane();
//    }

    public void start(Stage stage) {
        root = new Pane();
        //########################################/ UI /########################################//
        Image ui_side_rightImage = new Image(getClass().getResource("/ui/ui_side_right.png").toExternalForm());
        Image ui_side_leftImage = new Image(getClass().getResource("/ui/ui_side_left.png").toExternalForm());
        ImageView ui_side_left = new ImageView(ui_side_leftImage);
        ImageView ui_side_right = new ImageView(ui_side_rightImage);

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

        //########################################/ END DIALOG /########################################//
        Image endDialogImage = new Image(getClass().getResource("/ui/end_dialog.png").toExternalForm());
        endDialog = new ImageView(endDialogImage);
        endDialog.setVisible(false);

        Image endPlayAgainImage = new Image(getClass().getResource("/ui/end_play_again.png").toExternalForm());
        endPlayAgain = new ImageView(endPlayAgainImage);
        endPlayAgain.setVisible(false);

        Image endExitImage = new Image(getClass().getResource("/ui/end_exit.png").toExternalForm());
        endExit = new ImageView(endExitImage);
        endExit.setVisible(false);


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

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                event.consume();
            }
        });
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);


        scene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());
        stage.setTitle("Hidden Objects Game");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.show();

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();
        darkOverlay = new Rectangle(screenWidth, screenHeight);
        darkOverlay.setFill(Color.color(0.078, 0.078, 0.078));
        darkOverlay.setOpacity(0); // Изначально невидим
        darkOverlay.setMouseTransparent(true); // ✅ ВАЖНО: пропускает клики сквозь себя

        // ✅ После того как окно показано — включаем fullscreen
        Platform.runLater(() -> stage.setFullScreen(true));

        //########################################/ CUSTOM CURSOR /########################################//
        try {
            Image magnifyingCursorImage = new Image(getClass().getResource("/ui/magnifying-glass-cursor.png").toExternalForm());
            Image arrowCursorImage = new Image(getClass().getResource("/ui/arrow.png").toExternalForm());

            ImageCursor magnifyingCursor = new ImageCursor(magnifyingCursorImage, magnifyingCursorImage.getWidth() / 2, magnifyingCursorImage.getHeight() / 2);
            ImageCursor arrowCursor = new ImageCursor(arrowCursorImage, 3, 2);

            // Устанавливаем начальный курсор
            scene.setCursor(magnifyingCursor);

            // ✅ ОБРАБОТЧИК ДВИЖЕНИЯ МЫШИ
            scene.setOnMouseMoved(event -> {
                double mouseX = event.getX();

                // Игровое поле: от x >= 420 до x <= 1500
                if (mouseX >= 415 && mouseX <= 1495) {
                    // Курсор-лупа на игровом поле
                    scene.setCursor(magnifyingCursor);
                } else {
                    // Обычная стрелка за пределами игрового поля
                    scene.setCursor(arrowCursor);
                }
            });

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

        score.setLayoutX(screenBounds.getWidth() / 2 - 235);
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
        scoreText.setFont(Font.font("Poppins", FontWeight.BLACK, 48));
        scoreText.setFill(Color.WHITE);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0, 0, 0, 0.25));
        dropShadow.setOffsetY(4.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setRadius(4.0);
        scoreText.setEffect(dropShadow);

        scoreText.setLayoutX(1083);
        scoreText.setLayoutY(67);
        root.getChildren().add(scoreText);
        root.getChildren().addAll(confirmDialog, confirmYes, confirmNo, endDialog, endExit, endPlayAgain);

        setupButtonAnimation(endPlayAgain);
        setupButtonAnimation(endExit);

        //########################################/ MESSAGE HISTORY PANEL /########################################//
        // Загружаем CSS
        scene.getStylesheets().add(getClass().getResource("/styles/game.css").toExternalForm());

        messageHistoryBox = new VBox(10);
        messageHistoryBox.setPrefWidth(350);
        messageHistoryBox.getStyleClass().add("message-history-panel"); // Применяем CSS класс

        messageScrollPane = new ScrollPane(messageHistoryBox);
        messageScrollPane.setLayoutX(20);
        messageScrollPane.setLayoutY(210);
        messageScrollPane.setPrefHeight(800);
        messageScrollPane.setPrefWidth(380);
        messageScrollPane.setFitToWidth(true);
        messageScrollPane.getStyleClass().add("message-scroll-pane");

        root.getChildren().add(messageScrollPane);

        // Заголовок панели
        Text panelTitle = new Text("ІСТОРІЯ ПОВІДОМЛЕНЬ");
        panelTitle.getStyleClass().add("panel-title");

        // Фон для заголовка
        Rectangle titleBackground = new Rectangle(380, 60);
        titleBackground.getStyleClass().add("title-background");
        titleBackground.setLayoutX(20);
        titleBackground.setLayoutY(150);

        panelTitle.setLayoutX(45);
        panelTitle.setLayoutY(190);

        root.getChildren().addAll(titleBackground, panelTitle);

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
    }


    //########################################/ MESSAGE HISTORY /########################################//
    public void showMessage(String message, boolean isFinalThreat) {
        Text msgText = new Text(message);
        msgText.setWrappingWidth(messageHistoryBox.getPrefWidth() - 25);

        boolean isThreatFound = message.startsWith("Загрозу знайдено");
        msgText.getStyleClass().add("message-text");
        if (isThreatFound) {
            msgText.getStyleClass().add("message-threat-found");
        } else if (message.startsWith("Цю загрозу вже було знайдено!")) {
            msgText.getStyleClass().add("message-already-found");
        } else if (message.startsWith("Цей об'єкт є безпечним!")) {
            msgText.getStyleClass().add("message-safe-object");
        }

        msgText.setOpacity(0);
        messageHistoryBox.getChildren().add(0, msgText);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), msgText);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        fadeIn.setOnFinished(e -> {
            Platform.runLater(() -> {
                messageScrollPane.setVvalue(0);

                limitMessageHistory();

                // ✅ Проверяем победу ТОЛЬКО если это финальная угроза
                if (isFinalThreat) {
                    PauseTransition victoryPause = new PauseTransition(Duration.millis(100));
                    victoryPause.setOnFinished(finalEv -> {
                        showEndDialog();
                    });
                    victoryPause.play();
                }
            });
        });

        fadeIn.play();
    }

    private void limitMessageHistory() {
        // ✅ Используем количество угроз как максимальный размер
        final int MAX_TOTAL_MESSAGES = (int) controller.getTotalThreats();

        // Если угроз меньше 5, устанавливаем минимальный лимит для удобства
        final int MIN_LIMIT = 5;
        final int actualLimit = Math.max(MAX_TOTAL_MESSAGES, MIN_LIMIT);

        while (messageHistoryBox.getChildren().size() > actualLimit) {
            int oldestNonThreatIndex = -1;

            // 1. Ищем самую старую не-угрозу
            for (int i = messageHistoryBox.getChildren().size() - 1; i >= 0; i--) {
                javafx.scene.Node child = messageHistoryBox.getChildren().get(i);
                if (child instanceof Text) {
                    Text historyText = (Text) child;
                    if (!historyText.getText().startsWith("Загрозу знайдено")) {
                        oldestNonThreatIndex = i;
                        break;
                    }
                }
            }

            if (oldestNonThreatIndex != -1) {
                // 2. Если найдена, удаляем ее
                messageHistoryBox.getChildren().remove(oldestNonThreatIndex);
            } else {
                // 3. Если остались только угрозы, удаляем самую старую угрозу
                messageHistoryBox.getChildren().remove(messageHistoryBox.getChildren().size() - 1);
            }
        }
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
        Image handCursorImage = new Image(MainMenu.class.getResource("/ui/hand.png").toExternalForm());
        ImageCursor handCursor = new ImageCursor(handCursorImage, 3, 2);
        button.setCursor(handCursor);

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

    }


    public void showTipImage(String imageName, double x, double y) {
        try {
            Image tipImage = new Image(getClass().getResource("/tips/" + imageName).toExternalForm());
            ImageView tipView = new ImageView(tipImage);
            tipView.setLayoutX(x);
            tipView.setLayoutY(y - tipImage.getHeight() - 50);
            root.getChildren().add(tipView);

            FadeTransition ft = new FadeTransition(Duration.millis(100), tipView);
            ft.setFromValue(1.0);
            ft.setToValue(0.0);
            ft.setOnFinished(e -> root.getChildren().remove(tipView));
            ft.setDelay(Duration.millis(300));
            ft.play();
        } catch (Exception e) {
            System.err.println("Ошибка загрузки изображения подсказки: /tips/" + imageName + " " + e.getMessage());
        }
    }

    public void showEndDialog() {
        if (isEndDialogVisible) return;
        isEndDialogVisible = true;

        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double dialogWidth = 700;
        double dialogHeight = 400;
        double x = screenBounds.getWidth() / 2;
        double y = screenBounds.getHeight() / 2;

        endDialog.setLayoutX(x - 400);
        endDialog.setLayoutY(y - 130);

        endPlayAgain.setLayoutX(x - 390);   // отступы подгонишь под свой дизайн
        endPlayAgain.setLayoutY(y + 21);
        endExit.setLayoutX(x);
        endExit.setLayoutY(y + 21);

        endDialog.setVisible(true);
        endPlayAgain.setVisible(true);
        endExit.setVisible(true);
        //setGameInteraction(false);

        // Анимация появления
        endDialog.setOpacity(0);
        endDialog.setScaleX(0.8);
        endDialog.setScaleY(0.8);
        endDialog.setTranslateY(40);

        endPlayAgain.setOpacity(0);
        endExit.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), endDialog);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), endDialog);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        TranslateTransition moveUp = new TranslateTransition(Duration.millis(300), endDialog);
        moveUp.setFromY(40);
        moveUp.setToY(0);

        FadeTransition fadeYes = new FadeTransition(Duration.millis(250), endPlayAgain);
        fadeYes.setFromValue(0);
        fadeYes.setToValue(1);

        FadeTransition fadeNo = new FadeTransition(Duration.millis(250), endExit);
        fadeNo.setFromValue(0);
        fadeNo.setToValue(1);

        ParallelTransition endDialogAppear = new ParallelTransition(fadeIn, scaleIn, moveUp);
        javafx.animation.SequentialTransition total =
                new javafx.animation.SequentialTransition(endDialogAppear, new ParallelTransition(fadeYes, fadeNo));
        total.setOnFinished(e -> {
            // Отключаем взаимодействие только после полного появления диалога
            setGameInteraction(false);
        });
        total.play();

        // Кнопки
        endPlayAgain.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            hideEndDialog();
            messageHistoryBox.getChildren().clear();

            SoundPlayer.stopAll();
            controller.stopGameMusic();
            controller.resetGame();
        });

        endExit.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            hideEndDialog();

            javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(Duration.millis(300));
            pause.setOnFinished(ev -> {
                try {
                    // ✅ ВАЖНО: Делаем оверлей кликабельным ПЕРЕД анимацией
                    darkOverlay.setMouseTransparent(false);

                    // Останавливаем все звуки из игры
                    SoundPlayer.stopAll();
                    controller.stopGameMusic();

                    boolean muted = controller.areSoundEffectsMuted();
                    SoundPlayer.setMuted(muted);

                    // 1. Сначала плавно затемняем экран
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(800), darkOverlay);
                    fadeOut.setFromValue(0);
                    fadeOut.setToValue(0.9); // 90% непрозрачности

                    // 2. Параллельно уменьшаем громкость музыки
                    Timeline volumeDecrease = new Timeline();
                    if (mediaPlayer != null) {
                        volumeDecrease = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(mediaPlayer.volumeProperty(), 0.5)),
                                new KeyFrame(Duration.millis(800), new KeyValue(mediaPlayer.volumeProperty(), 0.0))
                        );
                    }

                    // Запускаем все анимации параллельно
                    ParallelTransition transition = new ParallelTransition(fadeOut, volumeDecrease);
                    transition.setOnFinished(event -> {
                        // ✅ Открываем главное меню после завершения анимации
                        controller.exitGame(stage);
                        MainMenu mainMenu = new MainMenu(stage, controller);
                        mainMenu.show();
                    });

                    transition.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            pause.play();
        });
    }

    private void hideEndDialog() {
        isEndDialogVisible = false;
        endDialog.setVisible(false);
        endPlayAgain.setVisible(false);
        endExit.setVisible(false);
        setGameInteraction(true);
    }

    private Stage stage;

    public GameView(GameController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }
    public Pane getRootPane() {
        return root;
    }

    public Scene getScene() {
        return root.getScene();
    }

}
