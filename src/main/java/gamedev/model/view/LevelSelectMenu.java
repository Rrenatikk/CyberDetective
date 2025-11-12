package gamedev.model.view;

import gamedev.controller.GameController;
import gamedev.model.SoundPlayer;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LevelSelectMenu {
    private final Stage primaryStage;
    private final double screenWidth;
    private final double screenHeight;
    private final GameController controller;

    private final ImageView menuBackground;
    private final ImageView backButton;
    private final Pane root;

    // --- Предзагрузка общих изображений статуса ---
    private final Image unlockedImage;
    private final Image lockedImage;
    private final Image secretImage;
    private final Image checkMarkImage;

    public LevelSelectMenu(Stage primaryStage, GameController controller) {
        this.primaryStage = primaryStage;
        this.controller = controller;

        Screen screen = Screen.getPrimary();
        screenWidth = screen.getBounds().getWidth();
        screenHeight = screen.getBounds().getHeight();
        root = new Pane();

        // Предзагрузка общих иконок
        unlockedImage = new Image(getClass().getResource("/ui/unlocked.png").toExternalForm());
        lockedImage = new Image(getClass().getResource("/ui/locked.png").toExternalForm());
        secretImage = new Image(getClass().getResource("/ui/level_secret.png").toExternalForm());
        checkMarkImage = new Image(getClass().getResource("/ui/check_mark.png").toExternalForm());

        // Предзагрузка фона
        menuBackground = new ImageView(new Image(getClass().getResourceAsStream("/ui/choose_level_menu.png")));
        menuBackground.setFitWidth(screenWidth);
        menuBackground.setFitHeight(screenHeight);

        // Предзагрузка кнопки "Назад"
        Image backImage = new Image(getClass().getResource("/ui/menu_back.png").toExternalForm());
        backButton = new ImageView(backImage);
    }

    public void show() {
        MainMenu.startMenuMusic();

        root.getChildren().setAll(menuBackground);

        // --- Размещение кнопки "Назад" ---
        double backButtonX = screenWidth / 2 - backButton.getImage().getWidth() / 2;
        double backButtonY = screenHeight - backButton.getImage().getHeight() - 100;

        backButton.setLayoutX(backButtonX);
        backButton.setLayoutY(backButtonY);
        root.getChildren().add(backButton);
        setupButtonAnimation(backButton);

        backButton.setOnMouseClicked(e -> {
            SoundPlayer.play("button_clicked.mp3");
            MainMenu mainMenu = new MainMenu(primaryStage, controller);
            mainMenu.show();
        });


        // --- СОЗДАНИЕ КНОПОК УРОВНЕЙ ---
        int totalLevels = controller.getTotalLevelsCount();

        // Параметры размещения кнопок уровня
        double iconWidth = 200; // Ширина иконки (для центрирования)
        double spacing = 73;    // Отступ между кнопками
        double totalWidth = totalLevels * iconWidth + (totalLevels - 1) * spacing;

        double startX = screenWidth / 2 - totalWidth / 2;
        double iconY = screenHeight / 2 - iconWidth / 2;
        double lockYOffset = 220; // Смещение для замка относительно iconY

        for (int i = 1; i <= totalLevels; i++) {
            final int levelNumber = i;

            // ✅ --- ОБНОВЛЕННАЯ ЛОГИКА СТАТУСА ---
            // 1. Проверяем, ПРОЙДЕН ли САМ ЭТОТ уровень
            boolean isCompleted = controller.isLevelCompleted(levelNumber);

            // 2. Проверяем, ДОСТУПЕН ли уровень (первый доступен, или пройден предыдущий)
            boolean isAvailable = (levelNumber == 1) || controller.isLevelCompleted(levelNumber - 1);
            // ------------------------------------

            // 1. Загрузка ИКОНКИ УРОВНЯ
            Image levelIconImage;
            try {
                if (levelNumber <= 3) {
                    levelIconImage = new Image(getClass().getResource("/ui/level" + levelNumber + ".png").toExternalForm());
                } else {
                    levelIconImage = secretImage;
                }
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки уровня " + levelNumber + ": " + e.getMessage());
                continue;
            }
            ImageView levelButton = new ImageView(levelIconImage);

            // Рассчитываем позицию
            double currentX = startX + (levelNumber - 1) * (iconWidth + spacing);
            levelButton.setLayoutX(currentX);
            levelButton.setLayoutY(iconY);

            root.getChildren().add(levelButton);

            ImageView statusIcon = new ImageView();
            Image statusImageToShow; // Временная переменная для получения высоты

            if (isCompleted) {
                statusImageToShow = checkMarkImage;
            } else if (isAvailable) {
                statusImageToShow = unlockedImage;
            } else {
                statusImageToShow = lockedImage;
            }

            statusIcon.setImage(statusImageToShow);

            // --- Магия выравнивания по низу ---

            // 1. Определяем "пол" (Y-координата низа замка).
            // (Y верха замка + высота замка)
            double lockHeight = lockedImage.getHeight(); // 71 пиксель, как вы сказали
            double targetBottomY = iconY + lockYOffset + lockHeight;

            // 2. Получаем высоту ТЕКУЩЕЙ иконки (галочки или замка)
            double currentIconHeight = statusImageToShow.getHeight();

            // 3. Вычисляем новый Y (Пол - Высота текущей иконки)
            double statusIconTopY = targetBottomY - currentIconHeight;

            // --- Конец магии ---

            // Размещение иконки статуса (Центрирование по X, выравнивание по низу по Y)
            statusIcon.setLayoutX(currentX + (levelIconImage.getWidth() / 2) - (statusIcon.getImage().getWidth() / 2));
            statusIcon.setLayoutY(statusIconTopY); // ✅ Используем рассчитанный Y

            root.getChildren().add(statusIcon);

            // 3. Настройка обработчика и состояния

            // Кнопка кликабельна, ЕСЛИ УРОВЕНЬ ДОСТУПЕН (неважно, пройден или нет)
            if (isAvailable) {
                setupButtonAnimation(levelButton);

                levelButton.setOnMouseClicked(e -> {
                    SoundPlayer.play("button_clicked.mp3");
                    controller.startGameFromLevel(primaryStage, levelNumber);
                });
            } else {
                // Если заблокировано (isAvailable == false)
                levelButton.setOpacity(0.5);
                levelButton.setDisable(true);
                statusIcon.toFront(); // Замок всегда должен быть поверх
            }
        }

        // Настройка сцены
        Scene scene = new Scene(root, screenWidth, screenHeight);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Level Selection");
        primaryStage.setMaximized(true);
        primaryStage.setResizable(false);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                event.consume(); // полностью игнорируем клавишу Esc
            }
        });

        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreenExitKeyCombination(javafx.scene.input.KeyCombination.NO_MATCH);

        primaryStage.setFullScreen(true);

        // Установка курсора
        try {
            Image cursorImage = new Image(getClass().getResource("/ui/arrow.png").toExternalForm());
            ImageCursor customCursor = new ImageCursor(cursorImage, 3, 2);
            scene.setCursor(customCursor);
        } catch (Exception e) {
            System.err.println("Не вдалося завантажити курсор: " + e.getMessage());
        }
    }

    // Метод setupButtonAnimation без изменений
    private void setupButtonAnimation(ImageView button) {
        // ... (код метода setupButtonAnimation)
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
}