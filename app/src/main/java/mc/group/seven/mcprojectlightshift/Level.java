package mc.group.seven.mcprojectlightshift;

/**
 * Created by Matthew on 2017-11-17.
 */

public class Level {
    private String key, name;
    private int difficulty, bestMoves, id;

    public Level(int id, String key, String name, int difficulty, int bestMoves) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.difficulty = difficulty;
        this.bestMoves = bestMoves;
    }

    public String getKey() {
        return key;
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

    public int getId() { return id; }

    public void setBestMoves(int bestMoves) {
        this.bestMoves = bestMoves;
    }
}
