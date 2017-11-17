package mc.group.seven.mcprojectlightshift;

/**
 * Created by Matthew on 2017-11-17.
 */

public class Level {
    private String key, bestTime, name;
    private int difficulty, bestMoves;

    public Level(String key, String bestTime, String name, int difficulty, int bestMoves) {
        this.key = key;
        this.name = name;
        this.bestTime = bestTime;
        this.difficulty = difficulty;
        this.bestMoves = bestMoves;
    }

    public String getKey() {
        return key;
    }

    public String getBestTime() {
        return bestTime;
    }

    public String getName() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getBestMoves() {
        return bestMoves;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }

    public void setBestMoves(int bestMoves) {
        this.bestMoves = bestMoves;
    }
}
