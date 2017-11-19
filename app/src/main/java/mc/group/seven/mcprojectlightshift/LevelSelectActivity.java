package mc.group.seven.mcprojectlightshift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LevelSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        /* --this will be the load function to load play stats--

            SharedPreferences shared = getSharedPreferences("shareInfor", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = shared.edit();
            editor.putString("username", latitude);
            editor.putString("score", longitude);
            editor.apply();

*/
    }
}
