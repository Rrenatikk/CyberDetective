package gamedev.model;

import java.util.List;

public class Game {
    private int score;
    private int currentLevel;
    private List<Level> levels;

    public Game(List<Level> levels) {
        this.score = 0;
        this.currentLevel = 0;
        this.levels = levels;
    }

    public Level getCurrentLevel() {
        return levels.get(currentLevel);
    }

    public void nextLevel() {
        if (currentLevel < levels.size() - 1) {
            currentLevel++;
        }
    }

    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }
}
