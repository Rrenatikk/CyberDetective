package gamedev.model.view;

import gamedev.controller.GameController;
import gamedev.model.SoundPlayer;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class MainMenu {
    private final Stage primaryStage;
    private final double screenWidth;
    private final double screenHeight;
    private final GameController controller;
    private static MediaPlayer mediaPlayer;

    // Все элементы создаются сразу при создании MainMenu
    private final ImageView rulesBackground;
    private final ImageView rulesList;
    private final ImageView backButton;
    private final ImageView playButton;
    private final ImageView rulesButton;
    private final ImageView exitButton;
    private final ImageView background;
    private final Text copyrightText;

    // Элемент для затемнения - используем Rectangle и делаем его НЕкликабельным
    private final Rectangle darkOverlay;

    public MainMenu(Stage primaryStage, GameController controller) {

        this.primaryStage = primaryStage;
        this.controller = controller;

        Screen screen = Screen.getPrimary();
        screenWidth = screen.getBounds().getWidth();
        screenHeight = screen.getBounds().getHeight();

        // Основной фон
        background = new ImageView(new Image(getClass().getResource("/ui/menu_background.png").toExternalForm()));
        background.setFitWidth(screenWidth);
        background.setFitHeight(screenHeight);

        // Кнопки главного меню
        playButton = new ImageView(new Image(getClass().getResource("/ui/menu_play.png").toExternalForm()));
        rulesButton = new ImageView(new Image(getClass().getResource("/ui/menu_rules.png").toExternalForm()));
        exitButton = new ImageView(new Image(getClass().getResource("/ui/menu_exit.png").toExternalForm()));

        // Элементы экрана правил
        rulesBackground = new ImageView(new Image(getClass().getResource("/ui/menu_rules_back.png").toExternalForm()));
        rulesBackground.setFitWidth(screenWidth);
        rulesBackground.setFitHeight(screenHeight);

        rulesList = new ImageView(new Image(getClass().getResource("/ui/menu_rules_list.png").toExternalForm()));
        backButton = new ImageView(new Image(getClass().getResource("/ui/menu_back.png").toExternalForm()));

        // ✅ ИСПРАВЛЕНО: Используем Rectangle и делаем его НЕкликабельным
        darkOverlay = new Rectangle(screenWidth, screenHeight);
        darkOverlay.setFill(Color.color(0.078, 0.078, 0.078));
        darkOverlay.setOpacity(0); // Изначально невидим
        darkOverlay.setMouseTransparent(true); // ✅ ВАЖНО: пропускает клики сквозь себя

        // Позиционирование кнопок главного меню
        playButton.setLayoutX(650);
        playButton.setLayoutY(560);
        rulesButton.setLayoutX(650);
        rulesButton.setLayoutY(685);
        exitButton.setLayoutX(650);
        exitButton.setLayoutY(810);

        copyrightText = new Text("© 2025 CyberField NET. All rights reserved.");
        copyrightText.setFont(Font.font("Poppins", FontWeight.BLACK, 26));
        copyrightText.setFill(Color.color(0.255, 0.255, 0.255));


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double paddingRight = 30;
        double paddingBottom = -10;
        copyrightText.setLayoutX(screenBounds.getWidth() - copyrightText.getBoundsInLocal().getWidth() - paddingRight);
        copyrightText.setLayoutY(screenBounds.getHeight() - paddingBottom);
    }

    public void show() {
        startMenuMusic();

        Pane root = new Pane();

        // Добавляем предзагруженный фон
        root.getChildren().add(background);

        // Добавляем предзагруженные кнопки
        root.getChildren().addAll(playButton, rulesButton, exitButton, copyrightText);

        // ✅ ВАЖНО: Добавляем оверлей ПОСЛЕДНИМ, но он НЕ блокирует клики
        root.getChildren().add(darkOverlay);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hidden Objects Game");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.setFullScreenExitHint("");

        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                event.consume(); // полностью игнорируем клавишу Esc
            }
        });

        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);


        Platform.runLater(() -> primaryStage.setFullScreen(true));

        // Настройка курсора
        try {
            Image cursorImage = new Image(getClass().getResource("/ui/arrow.png").toExternalForm());
            ImageCursor customCursor = new ImageCursor(cursorImage, 3, 2);
            scene.setCursor(customCursor);
        } catch (Exception e) {
            System.err.println("Не вдалося завантажити курсор: " + e.getMessage());
        }

        // Настройка анимации и обработчиков
        setupButtonAnimation(playButton);
        setupButtonAnimation(rulesButton);
        setupButtonAnimation(exitButton);
        setupButtonAnimation(backButton);

        // Обработчики кликов
        playButton.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            startLevelSelectTransition();
        });

        rulesButton.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            showRules(root);
        });

        exitButton.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            stopMenuMusic();
            primaryStage.close();
        });

        // Обработчик кнопки "Назад"
        backButton.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            showMainMenu(root);
        });
    }

//    private void startGameWithTransition() {
//        darkOverlay.setMouseTransparent(false);
//
//        // ✅ Быстрая анимация уменьшения громкости (200ms вместо 800ms)
//        Timeline volumeDecrease = new Timeline();
//        if (mediaPlayer != null) {
//            volumeDecrease = new Timeline(
//                    new KeyFrame(Duration.ZERO, new KeyValue(mediaPlayer.volumeProperty(), mediaPlayer.getVolume())),
//                    new KeyFrame(Duration.millis(200), new KeyValue(mediaPlayer.volumeProperty(), 0.0))
//            );
//        }
//
//        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), darkOverlay); // Укороченная анимация
//        fadeOut.setFromValue(0);
//        fadeOut.setToValue(0.9);
//
//        ParallelTransition transition = new ParallelTransition(fadeOut, volumeDecrease);
//        transition.setOnFinished(e -> {
//            stopMenuMusic(); // Останавливаем после быстрой анимации
//            controller.startGame(primaryStage);
//        });
//
//        transition.play();
//    }

    private void startLevelSelectTransition() {
        darkOverlay.setMouseTransparent(false);

        // Анимация затухания музыки и экрана (скопирована из startGameWithTransition)
//        Timeline volumeDecrease = new Timeline();
//        if (mediaPlayer != null) {
//            volumeDecrease = new Timeline(
//                    new KeyFrame(Duration.ZERO, new KeyValue(mediaPlayer.volumeProperty(), mediaPlayer.getVolume())),
//                    new KeyFrame(Duration.millis(200), new KeyValue(mediaPlayer.volumeProperty(), 0.0))
//            );
//        }

        FadeTransition fadeOut = new FadeTransition(Duration.millis(400), darkOverlay);
        fadeOut.setFromValue(0);
        fadeOut.setToValue(0.9);

        ParallelTransition transition = new ParallelTransition(fadeOut);
        transition.setOnFinished(e -> {
            // stopMenuMusic(); // Музыка должна продолжиться в LevelSelectMenu, если она там есть

            // ✅ Переход к выбору уровня
            LevelSelectMenu levelSelect = new LevelSelectMenu(primaryStage, controller);
            levelSelect.show();
        });

        transition.play();
    }

    // Метод для запуска музыки главного меню
    public static void startMenuMusic() {
        try {
            if (mediaPlayer == null) {
                Media media = new Media(MainMenu.class.getResource("/sounds/main_menu.mp3").toExternalForm());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(0.5);
                mediaPlayer.play();
            } else if (mediaPlayer.getStatus() != MediaPlayer.Status.PLAYING) {
                mediaPlayer.play();
            }
        } catch (Exception e) {
            System.out.println("Не удалось загрузить музыку главного меню: " + e.getMessage());
        }
    }

    // Метод для остановки музыки главного меню
    public static void stopMenuMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    private void setupButtonAnimation(ImageView button) {
        Image handCursorImage = new Image(MainMenu.class.getResource("/ui/hand.png").toExternalForm());
        ImageCursor handCursor = new ImageCursor(handCursorImage, 3, 2);
        button.setCursor(handCursor);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        shadow.setRadius(15);
        shadow.setSpread(0.15);
        shadow.setOffsetY(6);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), button);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.02);
        scaleTransition.setToY(1.02);

        TranslateTransition liftTransition = new TranslateTransition(Duration.millis(100), button);
        liftTransition.setToY(-5);

        button.setOnMouseEntered(e -> {
            button.setEffect(shadow);
            scaleTransition.playFromStart();
            liftTransition.playFromStart();
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

    private void showRules(Pane root) {
        // Позиционирование элементов правил
        double rulesListWidth = rulesList.getImage().getWidth();
        double rulesListHeight = rulesList.getImage().getHeight();

        // Центрирование списка правил
        rulesList.setLayoutX(screenWidth / 2 - rulesListWidth / 2);
        rulesList.setLayoutY(screenHeight / 2 - rulesListHeight / 2);

        // Центрирование кнопки "Назад"
        double buttonWidth = backButton.getImage().getWidth();
        backButton.setLayoutX(screenWidth / 2 - buttonWidth / 2);
        backButton.setLayoutY(screenHeight / 2 + rulesListHeight / 2 - 200);

        // Установка начальных состояний для анимации
        rulesList.setOpacity(0);
        rulesList.setScaleX(0.9);
        rulesList.setScaleY(0.9);
        rulesList.setTranslateY(20);
        backButton.setOpacity(0);

        // Замена содержимого
        root.getChildren().setAll(rulesBackground, rulesList, backButton);
        // ✅ Добавляем оверлей ПОСЛЕДНИМ и делаем его НЕкликабельным
        root.getChildren().add(darkOverlay);
        darkOverlay.setOpacity(0);
        darkOverlay.setMouseTransparent(true); // Разрешаем клики на кнопки правил

        // Создание и запуск анимации
        createAndPlayRulesAnimation();
    }

    private void showMainMenu(Pane root) {
        // Просто меняем содержимое
        root.getChildren().setAll(background, playButton, rulesButton, exitButton, copyrightText);
        // ✅ Добавляем оверлей ПОСЛЕДНИМ и делаем его НЕкликабельным
        root.getChildren().add(darkOverlay);
        darkOverlay.setOpacity(0);
        darkOverlay.setMouseTransparent(true); // Разрешаем клики на кнопки меню
    }

    private void createAndPlayRulesAnimation() {
        // Анимация появляется мгновенно, т.к. изображения уже в памяти

        // Анимация для rulesList
        FadeTransition fadeInRules = new FadeTransition(Duration.millis(250), rulesList);
        fadeInRules.setFromValue(0);
        fadeInRules.setToValue(1);

        ScaleTransition scaleInRules = new ScaleTransition(Duration.millis(300), rulesList);
        scaleInRules.setFromX(0.9);
        scaleInRules.setFromY(0.9);
        scaleInRules.setToX(1.0);
        scaleInRules.setToY(1.0);

        TranslateTransition moveUpRules = new TranslateTransition(Duration.millis(300), rulesList);
        moveUpRules.setFromY(20);
        moveUpRules.setToY(0);

        // Анимация для backButton
        FadeTransition fadeInBack = new FadeTransition(Duration.millis(300), backButton);
        fadeInBack.setFromValue(0);
        fadeInBack.setToValue(1);
        fadeInBack.setDelay(Duration.millis(150));

        // Запускаем все анимации параллельно
        ParallelTransition parallelAnimation = new ParallelTransition(
                fadeInRules,
                scaleInRules,
                moveUpRules,
                fadeInBack
        );

        parallelAnimation.play();
    }
}