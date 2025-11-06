package gamedev.model;

import java.util.HashSet; // ✅ Импорт
import java.util.List;
import java.util.Set; // ✅ Импорт

public class Game {
    private int currentLevelIndex = 0;
    private final List<Level> levels;
    private int threatsFound = 0;
    private long totalThreats = 0;

    // ✅ НОВОЕ ПОЛЕ: Хранит ID пройденных уровней
    private Set<Integer> completedLevels = new HashSet<>();

    public Game(List<Level> levels) {
        this.levels = levels;
        if (!levels.isEmpty()) {
            updateTotalThreats();
        }
    }

    private void updateTotalThreats() {
        Level current = getCurrentLevel();
        if (current != null) {
            this.totalThreats = current.getObjects().stream()
                    .filter(GameObject::isThreat)
                    .count();
        } else {
            this.totalThreats = 0;
        }
    }

    // ✅ НОВЫЙ МЕТОД: Проверяет, пройден ли уровень по его ID
    public boolean isLevelCompleted(int levelId) {
        return completedLevels.contains(levelId);
    }

    // ✅ НОВЫЙ МЕТОД: Отмечает текущий уровень как пройденный
    public void markCurrentLevelAsComplete() {
        Level current = getCurrentLevel();
        if (current != null) {
            // Предполагается, что у Level есть getLevelId()
            completedLevels.add(current.getLevelId());
        }
    }

    /**
     * Сбрасывает состояние только текущего уровня.
     */
    public void reset() {
        this.threatsFound = 0;
        Level current = getCurrentLevel();
        if (current != null) {
            for (GameObject obj : current.getObjects()) {
                obj.setFound(false);
                obj.getImage().setDisable(false);
                obj.getImage().setOpacity(1.0);
            }
        }
    }

    // Удален старый reset(), чтобы избежать сброса ВСЕХ уровней сразу.
    // Если нужно сбросить ВСЮ игру, можно вызвать resetCurrentLevelState()
    // и установить currentLevelIndex = 0.

    public Level getCurrentLevel() {
        // Проверка границ, чтобы избежать IndexOutOfBoundsException
        if (levels.isEmpty() || currentLevelIndex < 0 || currentLevelIndex >= levels.size()) {
            return null;
        }
        return levels.get(currentLevelIndex);
    }

    // =======================================================
    // НОВЫЕ/ИЗМЕНЕННЫЕ МЕТОДЫ ДЛЯ ВЫБОРА УРОВНЯ (LevelSelectMenu)
    // =======================================================

    /**
     * Возвращает список всех уровней. Используется для LevelSelectMenu.
     */
    public List<Level> getLevels() {
        return levels;
    }

    /**
     * Устанавливает текущий уровень по его номеру (1-based index).
     */
    public void setCurrentLevel(int levelNumber) {
        // levelNumber (1, 2, 3...) -> currentLevelIndex (0, 1, 2...)
        int newIndex = levelNumber - 1;
        if (newIndex >= 0 && newIndex < levels.size()) {
            this.currentLevelIndex = newIndex;
            reset(); // Сброс состояния нового выбранного уровня
            updateTotalThreats(); // Обновляем общее количество угроз для нового уровня
        } else {
            System.err.println("Попытка установить несуществующий уровень: " + levelNumber);
        }
    }

    public void incrementThreatsFound() {
        this.threatsFound++;
    }

    public int getThreatsFound() {
        return threatsFound;
    }

    public long getTotalThreats() {
        return totalThreats;
    }

    public boolean hasNextLevel() {
        return currentLevelIndex < levels.size() - 1;
    }

    public void nextLevel() {
        if (hasNextLevel()) {
            currentLevelIndex++;
            reset(); // Сброс состояния для перехода
            updateTotalThreats();
        }
    }

    // Переименовал, чтобы соответствовать сбросу состояния уровня
    public void resetThreatsFound() {
        this.threatsFound = 0;
    }
}