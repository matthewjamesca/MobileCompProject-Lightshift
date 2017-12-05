package mc.group.seven.mcprojectlightshift;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    /** FILE STORAGE **/
    public static final String PREFS_NAME = "MyPrefsFile";
    public Intent musicService;
    public LevelList levelList;

    /** BUTTON OBJECTS **/
    public Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start music
        musicService = new Intent(this, BackgroundSoundService.class);
        startService(musicService);

        play = (Button) findViewById(R.id.btn_play);

        // Get current campaign progress
        int currentLevelId;
        SharedPreferences savedProgress = getSharedPreferences(PREFS_NAME, 0);
        currentLevelId = savedProgress.getInt("currentCampaignLevel", 0);

        buildLevelList();

        if (currentLevelId == 0) {
            play.setText("New Game");
        }
        else if (currentLevelId == -1) {
            play.setText("Replay Game");
        }
        else {
            play.setText("Continue Game");
        }
    }

    /** Builds global level list**/
    private void buildLevelList() {
        SharedPreferences savedProgress = getSharedPreferences(PREFS_NAME, 0);

        int tempBestMoves = 0;

        // Get current level progress
        levelList = new LevelList();

        tempBestMoves = savedProgress.getInt("level1best", -1);
        levelList.getLevels().get(1).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level2best", -1);
        levelList.getLevels().get(2).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level3best", -1);
        levelList.getLevels().get(3).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level4best", -1);
        levelList.getLevels().get(4).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level5best", -1);
        levelList.getLevels().get(5).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level6best", -1);
        levelList.getLevels().get(6).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level7best", -1);
        levelList.getLevels().get(7).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level8best", -1);
        levelList.getLevels().get(8).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("level9best", -1);
        levelList.getLevels().get(9).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl1best", -1);
        levelList.getLevels().get(10).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl2best", -1);
        levelList.getLevels().get(11).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl3best", -1);
        levelList.getLevels().get(12).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl4best", -1);
        levelList.getLevels().get(13).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl5best", -1);
        levelList.getLevels().get(14).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl6best", -1);
        levelList.getLevels().get(15).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl7best", -1);
        levelList.getLevels().get(16).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl8best", -1);
        levelList.getLevels().get(17).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl9best", -1);
        levelList.getLevels().get(18).setBestMoves(tempBestMoves);

        tempBestMoves = savedProgress.getInt("dl10best", -1);
        levelList.getLevels().get(19).setBestMoves(tempBestMoves);
    }

    @Override
    /**
     * Starts music and loads current game state
     */
    public void onResume() {

        super.onResume();

        // rebuild level list to update it
        buildLevelList();

        if (musicService != null) {
            startService(musicService);
        }

        int currentLevelId;
        SharedPreferences savedProgress = getSharedPreferences(PREFS_NAME, 0);
        currentLevelId = savedProgress.getInt("currentCampaignLevel", 0);

        if (currentLevelId == 0) {
            play.setText("New Game");
        }
        else if (currentLevelId == -1) {
            play.setText("Replay Game");
        }
        else {
            play.setText("Continue Game");
        }
    }

    @Override
    /**
     * Stops music.
     */
    public void onDestroy() {
        super.onDestroy();

        if (musicService != null) {
            stopService(musicService);
        }
    }

    /**
     * Buttons to open various activities.
     */

    public void openGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("openFrom", "c");
        startActivity(intent);
    }

    public void openDownloadableLevels(View view) {
        Intent intent = new Intent(this, DownloadLevelsActivity.class);
        startActivity(intent);
    }

    public void openLevelSelect(View view) {
        Intent intent = new Intent(this, LevelSelectActivity.class);

        // build levels just incase an update hasnt gone through yet
        buildLevelList();

        // grab the levels and place them in
        Bundle bundle = new Bundle();
        bundle.putSerializable("levels", levelList);

        intent.putExtras(bundle);

        startActivity(intent);
    }

    public void openContactUs(View view) {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }
}
