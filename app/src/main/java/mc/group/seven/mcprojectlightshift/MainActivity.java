package mc.group.seven.mcprojectlightshift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button)findViewById(R.id.loadbutton);
    }

    /**
     * Listener, called my play game button. Opens Game Activity.
     * @param view
     *
     * this is the test commit
     */


    public void openGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void loadGameState(View v){

        Intent intent = new Intent(this, Loadgameinfo.class);
        startActivity(intent);
    }
}
