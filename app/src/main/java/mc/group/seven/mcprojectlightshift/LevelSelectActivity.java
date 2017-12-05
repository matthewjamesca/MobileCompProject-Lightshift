package mc.group.seven.mcprojectlightshift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Level select view that allows users to execute unique levels as one-offs
 */
public class LevelSelectActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    ListView levelsView;
    LevelList levelList;
    String[] levels;
    int levelStarted = 0;
    ArrayAdapter<String> adapter;
    TextView noLvls;
    int adapterPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        // grab level list object from intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        levelList = (LevelList) bundle.getSerializable("levels");

        noLvls = (TextView) findViewById(R.id.tv_noLevels);
        noLvls.setVisibility(View.INVISIBLE);

        // set up the campaign level list based off of levels completed
        levels = setupAdapterArray(levelList);

        // set up adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, levels);

        levelsView = (ListView) findViewById(R.id.lv_levels);

        levelsView.setAdapter(adapter);

        //listener that starts level based on item clicked
        levelsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                String levelString = levels[position];

                String[] levelStringComp = levelString.split("\\r?\\n");

                String levelName = levelStringComp[0];

                int lvlId = -1;

                for (int i = 0; i < levelList.getLevels().size(); i++) {
                    if (levelName.equals(levelList.getLevels().get(i).getName())) {
                        lvlId = i;
                    }
                }

                levelStarted = lvlId;
                startLevel(lvlId);
                adapterPosition = position;
            }
        });
    }

    /**
     * Starts a level based off user selection.
     * @param levelid
     */
    private void startLevel(int levelid) {
        // create intent and put level clicked and activity it was opened from
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("openFrom", "l");
        intent.putExtra("levelToOpen", "" + levelid);

        // grab the levels and place them in
        Bundle bundle = new Bundle();
        bundle.putSerializable("levels", levelList);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    /**
     * Sets up the adapter array for the listview of levels
     * @param levelList
     * @return
     */
    private String[] setupAdapterArray(LevelList levelList) {
        // create an arraylist
        ArrayList<String> levelsComplete = new ArrayList<>();

        // populate it based off of levels completed
        for (int i = 1; i < 13; i++) {
            Level currentLevel = levelList.getLevels().get(i);
            if (currentLevel.getBestMoves() > 0) { // best moves will be -1 if not complete
                levelsComplete.add("" + currentLevel.getName() + "\n" + "Best Moves: " + currentLevel.getBestMoves());
            }
        }

        // convert back to an array and return
        String[] levels = new String[levelsComplete.size()];

        for (int i = 0; i < levels.length; i++) {
            levels[i] = levelsComplete.get(i);
        }

        // if no levels indicate state to user.
        if (levels.length == 0) {
            noLvls.setVisibility(View.VISIBLE);
        }

        return levels;
    }

    /**
     * Update view on resume with new record.
     */
    @Override
    public void onResume() {
        super.onResume();

        // if a level was started, get that levels new best upon completion incase
        // user made a new high score, then update the adapter.
        // first conditional for campaign level
        if (levelStarted != 0 && levelStarted < 10 && adapterPosition != -1) {
            // grab level
            Level level = levelList.getLevels().get(levelStarted);
            // grab storage
            SharedPreferences savedProgress = getSharedPreferences(PREFS_NAME, 0);
            // get the current best score for that level
            int moveChange = savedProgress.getInt("level" + (levelStarted) + "best", -1);
            // set the best moves and update the listview text
            level.setBestMoves(moveChange);
            levels[adapterPosition] = "" + level.getName() + "\n" + "Best Moves: " + level.getBestMoves();
        }

        // dlc level, needs to be saved in a specific place
        else if (adapterPosition != -1){
            // grab level
            Log.d("Level Started", "It's: " + levelStarted);
            Level level = levelList.getLevels().get(levelStarted);
            // grab storage
            SharedPreferences savedProgress = getSharedPreferences(PREFS_NAME, 0);
            // get the current best score for that level
            int moveChange = savedProgress.getInt("dl" + (levelStarted - 9) + "best", -1);
            // set the best moves and update the listview text
            level.setBestMoves(moveChange);
            levels[adapterPosition] = "" + level.getName() + "\n" + "Best Moves: " + level.getBestMoves();
        }

        // notify change and force a refresh of data on the listview
        adapter.notifyDataSetChanged();
        levelsView.invalidateViews();
    }
}
