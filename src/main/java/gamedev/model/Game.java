package gamedev.model;

import java.util.List;

public class Game {
    private int currentLevel;
    private List<Level> levels;
    private int threatsFound = 0;
    private long totalThreats = 0;

    public Game(List<Level> levels) {

        this.levels = levels;
        if (!levels.isEmpty()) {
            this.totalThreats = getCurrentLevel().getObjects().stream()
                    .filter(GameObject::isThreat)
                    .count();
        }
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevel);
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
}
