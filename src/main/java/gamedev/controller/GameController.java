package gamedev.controller;

import gamedev.model.*;
import gamedev.model.view.GameView;
import gamedev.model.view.LevelSelectMenu;
import gamedev.model.view.MainMenu;
import javafx.scene.effect.DropShadow; // ✅ Импорт сохранен (вдруг понадобится)
import javafx.scene.image.Image; // ✅ Импорт сохранен
import javafx.scene.image.ImageView; // ✅ Импорт сохранен
import javafx.scene.paint.Color; // ✅ Импорт сохранен
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.*;

public class GameController {
    private Game game; // Поле остается
    private GameView view;
    private MediaPlayer mediaPlayer;
    private boolean soundEffectsMuted = false;
    private Stage stage;
    private final Random random = new Random();
    private MediaPlayer gameMusicPlayer;

    // ❌ Старый пустой конструктор УДАЛЕН
    // public GameController() { }

    // ✅ НОВЫЙ КОНСТРУКТОР: Принимает готовую модель Game
    public GameController(Game game) {
        this.game = game;
    }

    /**
     * ✅ ИСПРАВЛЕННЫЙ МЕТОД:
     * Теперь он не создает уровни, а только запускает музыку
     * и показывает меню выбора уровня.
     */
    public void startGame(Stage stage) {
        // Этот метод вызывается из MainMenu
        stopGameMusic();

        // (Опционально) Вы можете оставить эту музыку как фон для LevelSelectMenu
        try {
            Media media = new Media(getClass().getResource("/sounds/background.mp3").toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Не удалось загрузить музыку: " + e.getMessage());
        }

        LevelSelectMenu levelSelect = new LevelSelectMenu(stage, this);
        levelSelect.show();

        // ❌ ВЕСЬ КОД (от LEVEL 1 до game = new Game(...)) ОТСЮДА УДАЛЕН
    }

    // ... (Остальные методы: goToNextLevel, onObjectClicked, getTotalLevelsCount, и т.д.
    // остаются БЕЗ ИЗМЕНЕНИЙ, так как они теперь корректно работают с this.game)

    public void goToNextLevel() {
        if (game.hasNextLevel()) {
            game.nextLevel();
            Level newLevel = game.getCurrentLevel();
            view.loadNewLevel(newLevel);
            game.resetThreatsFound();
            view.updateScore(game.getThreatsFound(), game.getTotalThreats());
            view.clearMessageHistory();
        } else {
            view.showEndDialog();
        }
    }


    public void onObjectClicked(GameObject obj) {
        String tipFileName;

        if (obj.isThreat() && !obj.isFound()) {
            if (!soundEffectsMuted) {
                SoundPlayer.play("button_clicked.mp3");
            }
            tipFileName = getRandomTipFileName(1, 5);
            obj.setFound(true);
            game.incrementThreatsFound();
            view.updateScore(game.getThreatsFound(), game.getTotalThreats());
            obj.getImage().setDisable(true);
            obj.getImage().setOpacity(0.6);
            boolean isFinalThreat = (game.getThreatsFound() == game.getTotalThreats());
            view.showMessage("Загрозу знайдено: " + obj.getName(), isFinalThreat);
        } else if (obj.isThreat() && obj.isFound()) {
            view.showMessage("Цю загрозу вже було знайдено!", false);
            return;
        } else {
            if (!soundEffectsMuted) {
                SoundPlayer.play("button_clicked.mp3");
            }
            tipFileName = getRandomTipFileName(6, 10);
            view.showMessage("Цей об'єкт є безпечним!", false);
        }

        if (tipFileName != null) {
            double objX = obj.getImage().getLayoutX();
            double objY = obj.getImage().getLayoutY();
            view.showTipImage(tipFileName, objX - 85, objY + 30);
        }
    }

    public Level getCurrentLevel() {
        return game.getCurrentLevel();
    }

    public long getTotalThreats() {
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

        // 1. Останавливаем МУЗЫКУ ИГРЫ (background.mp3)
        // (mediaPlayer - это тот, что вы запускали в startGameFromLevel)
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null; // Убедитесь, что он сброшен
        }
        // Вы также можете использовать ваш метод stopGameMusic(), если он останавливает mediaPlayer
        // stopGameMusic();

        // 2. Запускаем МУЗЫКУ МЕНЮ (main_menu.mp3)
        MainMenu.startMenuMusic();

        // 3. Показываем меню выбора уровня
        LevelSelectMenu levelSelectMenu = new LevelSelectMenu(stage, this);
        levelSelectMenu.show();
    }


    private String getRandomTipFileName(int start, int end) {
        int tipNumber = random.nextInt(end - start + 1) + start;
        return "tip" + tipNumber + ".png";
    }

    public int getTotalLevelsCount() {
        return game.getLevels().size();
    }

    public void startGameFromLevel(Stage stage, int levelNumber) {
        // 1. Останавливаем МУЗЫКУ МЕНЮ (main_menu.mp3)
        MainMenu.stopMenuMusic();

        // (Ваш код stopGameMusic() также вызывается, что хорошо для очистки)
        stopGameMusic();

        // 2. Устанавливаем уровень
        game.setCurrentLevel(levelNumber);

        // 3. Запускаем МУЗЫКУ ИГРЫ (background.mp3)
        try {
            Media media = new Media(getClass().getResource("/sounds/background.mp3").toExternalForm());
            // Убедитесь, что используете правильную переменную (mediaPlayer или gameMusicPlayer)
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.setVolume(0.5);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Не удалось загрузить музыку: " + e.getMessage());
        }

        // 4. Инициализация View
        this.stage = stage;
        view = new GameView(this, stage);
        view.start(stage);

        // 5. Обновляем счетчик
        game.resetThreatsFound();
        view.updateScore(game.getThreatsFound(), game.getTotalThreats());
    }


    public void resetGame() {
        game.reset(); // Вызов обновленного game.reset()
        view.updateScore(game.getThreatsFound(), game.getTotalThreats());
    }

    // Этот метод больше не нужен, т.к. view создается в startGameFromLevel
    // public void setView(GameView view) {
    //    this.view = view;
    // }

    public void stopGameMusic() {
        if (gameMusicPlayer != null) {
            gameMusicPlayer.stop();
            gameMusicPlayer = null;
        }
        // Также останавливаем музыку меню, если она есть
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }
    // ✅ НОВЫЙ МЕТОД: Вызывается из GameView, когда уровень пройден
    public void markCurrentLevelAsComplete() {
        game.markCurrentLevelAsComplete();
    }

    // ✅ ОБНОВЛЕННЫЙ МЕТОД: Теперь проверяет реальный статус
    public boolean isLevelCompleted(int levelNumber) {
        // levelNumber здесь - это ID уровня (например, 1, 2, 3)
        // LevelSelectMenu вызывает: controller.isLevelCompleted(levelNumber - 1)
        // Если мы на кнопке 2 (levelNumber=2), он проверит isLevelCompleted(1)
        return game.isLevelCompleted(levelNumber);
    }


    public Game getGame() {
        return game;
    }
}