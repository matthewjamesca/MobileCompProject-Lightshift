package mc.group.seven.mcprojectlightshift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView newGame = (TextView)findViewById(R.id.btn_play);
        Animation bigger = AnimationUtils.loadAnimation(this, R.anim.demo);
        newGame.startAnimation(bigger);
    }


    /**
     * Listener, called my play game button. Opens Game Activity.
     * @param view
     */
    public void openGame(View view) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);

        ImageButton newGame = (ImageButton)findViewById(R.id.img_play);
        Animation bigger = AnimationUtils.loadAnimation(this, R.anim.demo);
        newGame.startAnimation(bigger);
        //startActivity(intent);
    }
}
