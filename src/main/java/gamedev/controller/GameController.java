package gamedev.controller;

import gamedev.model.*;
import gamedev.model.view.GameView;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.*;

public class GameController {
    private Game game;
    private GameView view;
    private MediaPlayer mediaPlayer; // Перенесено из GameView
    private boolean soundEffectsMuted = false;
    private Stage stage;

    private final Random random = new Random();

    public void startGame(Stage stage) {
        //########################################/ AUDIO /########################################//
        try {
            Media media = new Media(getClass().getResource("/sounds/background.mp3").toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Не удалось загрузить музыку: " + e.getMessage());
        }


        Image backgroundImage = new Image(getClass().getResource("/background.png").toExternalForm());
        ImageView background = new ImageView(backgroundImage);

        //########################################/ GAME OBJECTS /########################################//
        Image usbImage = new Image(getClass().getResource("/objects/usb.png").toExternalForm());
        ImageView usb = new ImageView(usbImage);

        Image calculatorImage = new Image(getClass().getResource("/objects/calculator.png").toExternalForm());
        ImageView calculator = new ImageView(calculatorImage);

        Image cdImage = new Image(getClass().getResource("/objects/cd.png").toExternalForm());
        ImageView cd = new ImageView(cdImage);

        Image cupImage = new Image(getClass().getResource("/objects/cup.png").toExternalForm());
        ImageView cup = new ImageView(cupImage);

        Image phishing_mailImage = new Image(getClass().getResource("/objects/phishing_mail.png").toExternalForm());
        ImageView phishing_mail = new ImageView(phishing_mailImage);

        Image login_stickerImage = new Image(getClass().getResource("/objects/login_sticker.png").toExternalForm());
        ImageView login_sticker = new ImageView(login_stickerImage);

        Image mouseImage = new Image(getClass().getResource("/objects/mouse.png").toExternalForm());
        ImageView mouse = new ImageView(mouseImage);

        Image penImage = new Image(getClass().getResource("/objects/pen.png").toExternalForm());
        ImageView pen = new ImageView(penImage);

        Image pencilsImage = new Image(getClass().getResource("/objects/pencils.png").toExternalForm());
        ImageView pencils = new ImageView(pencilsImage);

        Image phishing_notificationImage = new Image(getClass().getResource("/objects/phishing_notification.png").toExternalForm());
        ImageView phishing_notification = new ImageView(phishing_notificationImage);

        Image phoneImage = new Image(getClass().getResource("/objects/phone.png").toExternalForm());
        ImageView phone = new ImageView(phoneImage);

        Image plantImage = new Image(getClass().getResource("/objects/plant.png").toExternalForm());
        ImageView plant = new ImageView(plantImage);

        Image water_bottleImage = new Image(getClass().getResource("/objects/water_bottle.png").toExternalForm());
        ImageView water_bottle = new ImageView(water_bottleImage);

        GameObject usbObj = new GameObject(
                "USB-drive",
                true,
                ThreatType.USB_DRIVE,
                usb,
                1113, 827
        );

        GameObject calculatorObj = new GameObject(
                "Calculator",
                false,
                ThreatType.NO_THREAT,
                calculator,
                420, 842
        );

        GameObject cdObj = new GameObject(
                "CD-disk",
                true,
                ThreatType.SUSPICIOUS_CD,
                cd,
                1273, 428
        );

        GameObject pencilsObj = new GameObject(
                "Pencils",
                false,
                ThreatType.NO_THREAT,
                pencils,
                691, 542
        );

        GameObject plantObj = new GameObject(
                "Plant",
                false,
                ThreatType.NO_THREAT,
                plant,
                627, 635
        );

        GameObject cupObj = new GameObject(
                "Cup",
                false,
                ThreatType.NO_THREAT,
                cup,
                674, 708
        );

        GameObject phishing_mailObj = new GameObject(
                "Phishing_mail",
                true,
                ThreatType.PHISHING_EMAIL,
                phishing_mail,
                748, 241
        );

        GameObject login_stickerObj = new GameObject(
                "Login_sticker",
                true,
                ThreatType.STICKY_NOTE_PASSWORD,
                login_sticker,
                1092, 449
        );

        GameObject mouseObj = new GameObject(
                "Mouse",
                false,
                ThreatType.NO_THREAT,
                mouse,
                1187, 715
        );

        GameObject penObj = new GameObject(
                "Pen",
                false,
                ThreatType.NO_THREAT,
                pen,
                844, 636
        );

        GameObject phishing_notificationObj = new GameObject(
                "Phishing_notification",
                true,
                ThreatType.PHISHING_NOTIFICATION,
                phishing_notification,
                1063, 334
        );

        GameObject phoneObj = new GameObject(
                "Phone",
                true,
                ThreatType.PHONE,
                phone,
                1327, 722
        );

        GameObject water_bottleObj = new GameObject(
                "Water_bottle",
                false,
                ThreatType.NO_THREAT,
                water_bottle,
                497, 436
        );

        // glow effect
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.LIME);
        borderGlow.setOffsetX(0);
        borderGlow.setOffsetY(0);
        borderGlow.setWidth(50);
        borderGlow.setHeight(50);

        //########################################/ MOUSE ACTIONS /########################################//
        usb.setOnMouseEntered(e -> usb.setEffect(borderGlow));
        usb.setOnMouseExited(e -> usb.setEffect(null));

        calculator.setOnMouseEntered(e -> calculator.setEffect(borderGlow));
        calculator.setOnMouseExited(e -> calculator.setEffect(null));

        cd.setOnMouseEntered(e -> cd.setEffect(borderGlow));
        cd.setOnMouseExited(e -> cd.setEffect(null));

        pencils.setOnMouseEntered(e -> pencils.setEffect(borderGlow));
        pencils.setOnMouseExited(e -> pencils.setEffect(null));

        cup.setOnMouseEntered(e -> cup.setEffect(borderGlow));
        cup.setOnMouseExited(e -> cup.setEffect(null));

        phishing_mail.setOnMouseEntered(e -> phishing_mail.setEffect(borderGlow));
        phishing_mail.setOnMouseExited(e -> phishing_mail.setEffect(null));

        login_sticker.setOnMouseEntered(e -> login_sticker.setEffect(borderGlow));
        login_sticker.setOnMouseExited(e -> login_sticker.setEffect(null));

        mouse.setOnMouseEntered(e -> mouse.setEffect(borderGlow));
        mouse.setOnMouseExited(e -> mouse.setEffect(null));

        pen.setOnMouseEntered(e -> pen.setEffect(borderGlow));
        pen.setOnMouseExited(e -> pen.setEffect(null));

        phishing_notification.setOnMouseEntered(e -> phishing_notification.setEffect(borderGlow));
        phishing_notification.setOnMouseExited(e -> phishing_notification.setEffect(null));

        phone.setOnMouseEntered(e -> phone.setEffect(borderGlow));
        phone.setOnMouseExited(e -> phone.setEffect(null));

        plant.setOnMouseEntered(e -> plant.setEffect(borderGlow));
        plant.setOnMouseExited(e -> plant.setEffect(null));

        water_bottle.setOnMouseEntered(e -> water_bottle.setEffect(borderGlow));
        water_bottle.setOnMouseExited(e -> water_bottle.setEffect(null));

        //########################################/ LEVEL START /########################################//
        Level testLevel = new Level(1, List.of(usbObj, calculatorObj, cdObj, plantObj, pencilsObj,
                cupObj, phishing_mailObj, login_stickerObj, mouseObj, penObj, phishing_notificationObj,
                phoneObj, water_bottleObj), background);
        game = new Game(List.of(testLevel));

        view = new GameView(this);
        view.start(stage);
    }

    // GameController.java
    public void onObjectClicked(GameObject obj) {
        String tipFileName;

        // Проверяем, является ли объект угрозой И не был ли он найден ранее
        if (obj.isThreat() && !obj.isFound()) {
            if (!soundEffectsMuted) {
                SoundPlayer.play("button_clicked.mp3");
            }

            // 1. Логика для найденной УГРОЗЫ (tip1.png - tip5.png)
            tipFileName = getRandomTipFileName(1, 5);

            // 1. Помечаем объект как найденный
            obj.setFound(true);

            // 2. Увеличиваем счетчик в модели
            game.incrementThreatsFound();

            // 3. Обновляем текст на экране через View
            view.updateScore(game.getThreatsFound(), game.getTotalThreats());

            // Проверка конца игры
            if (game.getThreatsFound() >= game.getTotalThreats()) {
                view.showEndDialog();
                return;
            }


            // 4. (Рекомендуется) Даем визуальную обратную связь
            obj.getImage().setDisable(true);
            obj.getImage().setOpacity(0.6);

            view.showMessage("Загрозу знайдено: " + obj.getName());

        } else if (obj.isThreat() && obj.isFound()) {
            // Если кликнули по уже найденной угрозе
            view.showMessage("Цю загрозу вже було знайдено!");
            return; // Выходим, чтобы не показывать подсказку

        } else {
            // 2. Логика для БЕЗОПАСНОГО объекта (tip6.png - tip10.png)
            if (!soundEffectsMuted) {
                SoundPlayer.play("button_clicked.mp3"); // Звук ошибки
            }
            tipFileName = getRandomTipFileName(6, 10);
            view.showMessage("Цей об'єкт є безпечним!");
        }

        // ----------- Отображение подсказки -----------
        // Если объект не был найден ранее (для угроз) ИЛИ является безопасным
        if (tipFileName != null) {
            // Координаты объекта (взятые из ImageView)
            double objX = obj.getImage().getLayoutX();
            double objY = obj.getImage().getLayoutY();

            // Отправляем имя файла и координаты для отображения в View
            view.showTipImage(tipFileName, objX - 85, objY + 30);
        }
    }

    public Level getCurrentLevel() {
        return game.getCurrentLevel();
    }

    public long getTotalThreats() {
        // Контроллер просто просит модель дать нужные данные
        return game.getTotalThreats();
    }

    public void toggleMute() {
        if (mediaPlayer != null) {
            mediaPlayer.setMute(!mediaPlayer.isMute());
        }
    }

    public boolean isMuted() {
        return mediaPlayer != null && mediaPlayer.isMute();
    }

    public void toggleSoundEffects() {
        soundEffectsMuted = !soundEffectsMuted;
    }

    public boolean areSoundEffectsMuted() {
        return soundEffectsMuted;
    }


    public void exitGame(Stage stage) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        stage.close();

        // Here you can return to start menu or smth like that
        // Example: controller.showMainMenu();
    }

    private String getRandomTipFileName(int start, int end) {
        // nextInt(bound) возвращает число от 0 до bound-1.
        // nextInt(end - start + 1) возвращает число от 0 до (end - start).
        // Добавляем 'start', чтобы получить диапазон от 'start' до 'end'.
        int tipNumber = random.nextInt(end - start + 1) + start;
        return "tip" + tipNumber + ".png";
    }


    public void resetGame() {
        game.reset();
        view.updateScore(game.getThreatsFound(), game.getTotalThreats());
    }
    public GameController(Game game) {
        this.game = game;
    }
    public void setView(GameView view) {
        this.view = view;
    }
}