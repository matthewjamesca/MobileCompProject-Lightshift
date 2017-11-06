package mc.group.seven.mcprojectlightshift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button b,b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b = (Button)findViewById(R.id.loadbutton);
        b1 = (Button)findViewById(R.id.toSave);

    }

    public void openGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void loadGameState(View v){
        Intent intent = new Intent(this, LoadGameState.class);
        startActivity(intent);
    }

    public void toSave(View v){
        Intent intent = new Intent(this, SaveActivity.class);
        startActivity(intent);
    }
}
