package mc.group.seven.mcprojectlightshift;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    public GridView gv;
    Button b;
    TextView tv, name, score, level;
    public String[] items = new String[]
            {"1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9","1","2","3","4","5","6","7","8","9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        b = (Button)findViewById(R.id.Savebutton);
        gv = (GridView) this.findViewById(R.id.mygrid);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(GameActivity.this, items);
        gv.setAdapter(gridAdapter);
    }

    public void changeColor(View v){

       tv= (TextView) v.findViewById(R.id.tv);
      tv.setText("  ");
      tv.setBackgroundColor(Color.RED);
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

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        //editor.put
    }
}
