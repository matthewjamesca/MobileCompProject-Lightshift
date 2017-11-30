package mc.group.seven.mcprojectlightshift;

/**
 * Created by Matthew on 2017-11-17.
 */

public class Level {
    private String key, name;
    private int difficulty, bestMoves, id, movesLeft;

    public Level(int id, String key, String name, int difficulty, int bestMoves, int movesLeft) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.difficulty = difficulty;
        this.bestMoves = bestMoves;
        this.movesLeft = movesLeft;
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

    public int getMovesLeft() { return movesLeft; }

    public void setBestMoves(int bestMoves) {
        this.bestMoves = bestMoves;
    }
}
