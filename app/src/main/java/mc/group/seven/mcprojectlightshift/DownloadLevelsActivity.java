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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Activity that hosts downloadable level and executes downloads.
 */
public class DownloadLevelsActivity extends AppCompatActivity {

    String[] downloadableLevels; // adapter for level string representation
                                                 // set to three but can be increased easily.
    private DatabaseReference db; // firebase ref
    ArrayList<Level> dlclevels; // holder for dlc levels
    ArrayAdapter<String> adapter; // adapter
    ListView dlcview; //lv
    LevelList mainLevelList; // global level list
    public static final String PREFS_NAME = "MyPrefsFile"; // prefs
    TextView nolvls; // if nothing to show, this tv shows


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_levels);

        //grab current level list
        // grab level list object from intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        mainLevelList = (LevelList) bundle.getSerializable("levels");

        db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dlc = db.child("levels");

        nolvls = (TextView) findViewById(R.id.tv_nolvls);
        nolvls.setVisibility(View.INVISIBLE);

        // get the data from db a single time on load
        dlc.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    populateDLC((Map<String, Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("DB Error", "Couldn't collect DB Data.");
                }
            }
        );
    }

    /**
     * Populates the list view given database object.
     * @param levels
     */
    private void populateDLC(Map<String,Object> levels) {
        dlclevels = new ArrayList<>();

        //temp level objects
        Level templevel;
        String tempname, tempkey;
        int tempdiff, tempmovesleft;
        boolean alreadyAdded = false;

        //iterate through each level
        for (Map.Entry<String, Object> entry : levels.entrySet()){

            // grab level information and save it to a temp level
            Map singleLevel = (Map) entry.getValue();

            tempname = (String) singleLevel.get("name");
            tempkey = (String) singleLevel.get("key");
            tempdiff = ((Long) singleLevel.get("difficulty")).intValue();
            tempmovesleft = ((Long) singleLevel.get("movesLeft")).intValue();

            templevel = new Level(-1, tempkey, tempname, tempdiff, -1, tempmovesleft);

            // check to see if user already downloaded the level
            for (int i = 0; i < mainLevelList.getLevels().size(); i++) {
                if (mainLevelList.getLevels().get(i).getName().equals(tempname)) {
                    alreadyAdded = true;
                }
            }

            if (!alreadyAdded) {
                dlclevels.add(templevel);
            }

            alreadyAdded = false;
        }

        populateArrayAdapter();
    }

    /**
     * inserts the levels pulled from DB map into an array to be used
     * as a part of the listview adapter.
     */
    private void populateArrayAdapter() {

        // check if any levels remain to be downloaded, if none return.
        downloadableLevels = new String[dlclevels.size()];

        if (downloadableLevels.length == 0) {
            nolvls.setVisibility(View.VISIBLE);
            return;
        }

        // iterate through downloadable levels and get a string to represent them into adapter array
        for (int i = 0; i < dlclevels.size(); i++) {
            downloadableLevels[i] = "Name: " + dlclevels.get(i).getName() + "\nDifficulty: " + dlclevels.get(i).getDifficulty() + "/5";
        }

        // set up adapter
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, downloadableLevels);

        dlcview = (ListView) findViewById(R.id.download_list);

        dlcview.setAdapter(adapter);

        // onclick, download level
        dlcview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long l) {
                int placeToDownload = -1;

                //  grab levellist level object index to place the level to download based on name
                for (int i = 10; i < mainLevelList.getLevels().size() ; i++) {
                    if (mainLevelList.getLevels().get(i).getName().equals("")) {
                        placeToDownload = i;
                        break;
                    }
                }

                String name, key;
                int movesLeft;

                // grab the downloadable level information
                name = dlclevels.get(position).getName();
                key = dlclevels.get(position).getKey();
                movesLeft = dlclevels.get(position).getMovesLeft();

                // save that information to a free level object (based off placeToDownload)
                SharedPreferences savedProgress = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = savedProgress.edit();

                if (placeToDownload == 10) {
                    editor.putInt("dl1ml", movesLeft);
                    editor.putString("dl1name", name);
                    editor.putString("dl1key", key);
                    editor.putInt("dl1best", 100);
                }
                else if (placeToDownload == 11) {
                    editor.putInt("dl2ml", movesLeft);
                    editor.putString("dl2name", name);
                    editor.putString("dl2key", key);
                    editor.putInt("dl2best", 100);
                }
                else {
                    editor.putInt("dl3ml", movesLeft);
                    editor.putString("dl3name", name);
                    editor.putString("dl3key", key);
                    editor.putInt("dl3best", 100);
                }

                // commit and finish
                editor.commit();

                finish();
            }
        });
    }
}
