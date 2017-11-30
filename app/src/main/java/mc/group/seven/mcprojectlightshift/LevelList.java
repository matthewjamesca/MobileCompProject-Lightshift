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
        String lvl0str = "17 x 24 y 31 g 38 y 45 y 52 y 59 b 59 s";
        String lvl1str = "24 x 31 y 32 y 33 y 38 y 39 y 40 y 45 y 46 y 31 s";
        String lvl2str = "24 x 31 b 32 y 33 y 38 g 40 y 45 y 46 y 47 y 52 b 52 s";
        String lvl3str = "24 y 31 y 38 b 39 y 45 y 52 x 45 s";
        String lvl4str = "24 x 31 y 38 y 39 y 45 b 46 b 53 y 31 s";
        String lvl5str = "22 x 23 y 24 y 25 y 26 u 36 u 37 y 38 y 39 y 40 t 50 t 51 y 52 y 53 y 54 g 54 s";
        String lvl6str = "17 x 18 t 24 y 30 y 31 y 32 y 37 y 39 y 44 y 45 y 46 y 52 t 24 s";
        String lvl7str = "23 x 29 y 30 y 31 y 36 y 38 y 40 b 43 y 44 g 45 y 46 y 47 b 30 s";
        String lvl8str = "24 x 30 y 31 b 32 y 37 y 38 g 39 y 44 y 45 b 46 y 31 s";
        String lvl9str = "11 x 12 b 13 u 22 t 28 y 29 y 30 y 37 y 39 t 41 u 46 y 47 b 48 y 54 y 55 y 61 y 62 y 29 s";

        Level lvl0 = new Level(0, lvl0str, "Level 0 - Learning to Shift", 0, -1, -1);
        Level lvl1 = new Level(1, lvl1str, "Level 1 - Yellowed Out", 0, -1, -1);
        Level lvl2 = new Level(2, lvl2str, "Level 2 - A Touch of Blue", 0, -1, -1);
        Level lvl3 = new Level(3, lvl3str, "Level 3 - Intersect", 0, -1, -1);
        Level lvl4 = new Level(4, lvl4str, "Level 4 - A Pesky Blue Line", 0, -1, -1);
        Level lvl5 = new Level(5, lvl5str, "Level 5 - In the Blink of an Eye", 0, -1, -1);
        Level lvl6 = new Level(6, lvl6str, "Level 6 - Jumper", 0, -1, -1);
        Level lvl7 = new Level(7, lvl7str, "Level 7 - A New Constraint", 0, -1, 13);
        Level lvl8 = new Level(8, lvl8str, "Level 8 - Budgeted", 0, -1, 13);
        Level lvl9 = new Level(9, lvl9str, "Level 9 - A Final Test", 0, -1, 25);

        addLevel(lvl0);
        addLevel(lvl1);
        addLevel(lvl2);
        addLevel(lvl3);
        addLevel(lvl4);
        addLevel(lvl5);
        addLevel(lvl6);
        addLevel(lvl7);
        addLevel(lvl8);
        addLevel(lvl9);
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }
}
