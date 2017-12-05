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

import java.util.ArrayList;
import java.util.List;

public class LevelSelectActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    ListView levelsView;
    LevelList levelList;
    String[] levels;
    int levelStarted = 0;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        // grab level list object from intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        levelList = (LevelList) bundle.getSerializable("levels");

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
                levelStarted = position;
                startLevel(position + 1);
            }
        });
    }

    private void startLevel(int levelid) {
        // create intent and put level clicked and activity it was opened from
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("openFrom", "l");
        intent.putExtra("levelToOpen", "" + levelid);
        startActivity(intent);
    }

    private String[] setupAdapterArray(LevelList levelList) {
        // create an arraylist
        ArrayList<String> levelsComplete = new ArrayList<>();

        // populate it based off of levels completed
        for (int i = 1; i < 10; i++) {
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

        return levels;
    }

    public void onResume() {
        super.onResume();

        // if a level was started, get that levels new best upon completion incase
        // user made a new high score, then update the adapter.
        if (levelStarted != 0) {
            // grab level
            Level level = levelList.getLevels().get(levelStarted + 1);
            // grab storage
            SharedPreferences savedProgress = getSharedPreferences(PREFS_NAME, 0);
            // get the current best score for that level
            int moveChange = savedProgress.getInt("level" + (levelStarted + 1) + "best", -1);
            // set the best moves and update the listview text
            level.setBestMoves(moveChange);
            levels[levelStarted] = "" + level.getName() + "\n" + "Best Moves: " + level.getBestMoves();
        }

        // notify change and force a refresh of data on the listview
        adapter.notifyDataSetChanged();
        levelsView.invalidateViews();
    }
}
