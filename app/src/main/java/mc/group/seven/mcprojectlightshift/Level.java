package mc.group.seven.mcprojectlightshift;

import java.io.Serializable;

/**
 * Created by Matthew on 2017-11-17.
 */

public class Level implements Serializable{
    private String key, name;
    private int difficulty, bestMoves, id, movesLeft;

    public Level(int id, String key, String name, int difficulty, int bestMoves, int movesLeft) {
        this.id = id; // id of the level
        this.key = key; // key that builds the level
        this.name = name; // name of the level
        this.difficulty = difficulty; //level difficulty
        this.bestMoves = bestMoves; // users best moves
        this.movesLeft = movesLeft; // total moves the player can make without failing level.
                                    // If -1 no moves constraint
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
