package mc.group.seven.mcprojectlightshift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SaveActivity extends AppCompatActivity {
    TextView tv, name, score, level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
    }

    public void saveGameState(View v){

        name = (TextView)findViewById(R.id.username);
        score = (TextView) findViewById(R.id.score);
        level = (TextView) findViewById(R.id.level);

        SharedPreferences saveGame = getSharedPreferences("SAVEGAME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = saveGame.edit();
        editor.putString("username", name.getText().toString());
        editor.putString("score", score.getText().toString());
        editor.putString("level", level.getText().toString());
        editor.apply();

        finish();
    }
}
