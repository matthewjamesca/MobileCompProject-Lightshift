package mc.group.seven.mcprojectlightshift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class DownloadLevelsActivity extends AppCompatActivity {
    //Array of Downloadable Levels
    String[] downloadableLevels = {"Custom Level 1", "Custom Level 2", "Custom Level 3", "Custom Level 4", "Custom Level 5",
            "Custom Level 6", "Custom Level 7", "Custom Level 8"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_levels);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_download_levels, downloadableLevels);

        ListView listView = (ListView) findViewById(R.id.download_list);
        listView.setAdapter(adapter);
    }
}
