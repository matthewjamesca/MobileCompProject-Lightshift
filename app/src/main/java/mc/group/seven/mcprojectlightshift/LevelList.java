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

        /**
         * Base level set, where...
         *  X = Exit Tile
         *  B = Blue Tile
         *  Y = Yellow Tile
         *  G = Grey Tile
         *  T = Teleporter 1 Tile
         *  U = Teleporter 2 Tile
         *  V = Teleporter 3 Tile
         **/
        String lvl0str = "23 x 30 b 31 y 32 y 37 g 39 y 44 y 45 y 46 y 51 b 51 s";
        String lvl1str = "24 y 31 y 38 b 39 y 45 y 52 x 45 s";
        String lvl2str = "22 x 23 y 24 y 25 y 26 u 36 u 37 y 38 y 39 y 40 t 50 t 51 y 52 y 53 y 54 g 54 s";

        Level lvl0 = new Level(0, lvl0str, "Level 0 - Learning to Shift", 0, -1);
        Level lvl1 = new Level(1, lvl1str, "Level 1 - Getting Started", 0, -1);
        Level lvl2 = new Level(2, lvl2str, "Level T - Teleporter Test", 0, -1);


        addLevel(lvl0);
        addLevel(lvl1);
        addLevel(lvl2);
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }
}
