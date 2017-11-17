package mc.group.seven.mcprojectlightshift;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Matthew on 2017-11-17.
 *
 * Container object for levels.
 */

public class LevelList implements Serializable{

    private ArrayList<Level> levels;

    public LevelList() {
        levels = new ArrayList<Level>();
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public void addLevel(String key, String name, String bestTime, int difficulty, int bestMoves) {
        Level level = new Level(key, bestTime, name, difficulty, bestMoves);
        levels.add(level);
    }
}
