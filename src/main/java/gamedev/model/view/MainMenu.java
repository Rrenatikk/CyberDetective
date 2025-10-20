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
    private MediaPlayer mediaPlayer;

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
        darkOverlay.setFill(Color.BLACK);
        darkOverlay.setOpacity(0); // Изначально невидим
        darkOverlay.setMouseTransparent(true); // ✅ ВАЖНО: пропускает клики сквозь себя

        // Позиционирование кнопок главного меню
        playButton.setLayoutX(650);
        playButton.setLayoutY(560);
        rulesButton.setLayoutX(650);
        rulesButton.setLayoutY(685);
        exitButton.setLayoutX(650);
        exitButton.setLayoutY(810);

        copyrightText = new Text("Developed by CyberField NeT");
        copyrightText.setFont(Font.font("Poppins", FontWeight.BOLD, 26));
        copyrightText.setFill(Color.color(0.255, 0.255, 0.255));


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double paddingRight = 30;
        double paddingBottom = 0;
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

        Platform.runLater(() -> primaryStage.setFullScreen(true));

        // Настройка курсора
        try {
            Image cursorImage = new Image(getClass().getResource("/ui/arrow.png").toExternalForm());
            ImageCursor customCursor = new ImageCursor(cursorImage, cursorImage.getWidth(), cursorImage.getHeight());
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
            startGameWithTransition();
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

    private void startGameWithTransition() {
        // ✅ ВАЖНО: Перед анимацией делаем оверлей кликабельным, чтобы блокировать кнопки
        darkOverlay.setMouseTransparent(false);

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
        transition.setOnFinished(e -> {
            // Когда анимация завершена - запускаем игру
            stopMenuMusic();
            controller.startGame(primaryStage);
        });

        transition.play();
    }

    // Метод для запуска музыки главного меню
    public void startMenuMusic() {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            Media media = new Media(getClass().getResource("/sounds/main_menu.mp3").toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Не удалось загрузить музыку главного меню: " + e.getMessage());
        }
    }

    // Метод для остановки музыки главного меню
    public void stopMenuMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    private void setupButtonAnimation(ImageView button) {
        button.setCursor(Cursor.HAND);

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