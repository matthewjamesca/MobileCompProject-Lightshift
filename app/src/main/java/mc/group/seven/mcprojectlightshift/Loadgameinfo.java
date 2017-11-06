package mc.group.seven.mcprojectlightshift;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Loadgameinfo extends AppCompatActivity {

   public static TextView t1,t2,t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadgameinfo);

        t1 = (TextView) findViewById(R.id.loadname);
        t2 = (TextView) findViewById(R.id.loadscore);
        t3 = (TextView) findViewById(R.id.loadlevel);

        SharedPreferences sharedPreferences = getSharedPreferences("SAVEGAME", Context.MODE_PRIVATE);
       t1.setText(sharedPreferences.getString("username","N/A"));
        t2.setText(sharedPreferences.getString("score","N/A"));
       t3.setText(sharedPreferences.getString("level","N/A"));
    }
}
