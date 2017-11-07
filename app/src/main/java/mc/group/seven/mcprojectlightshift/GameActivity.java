package mc.group.seven.mcprojectlightshift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

//testing it
public class GameActivity extends AppCompatActivity {

    //test commit and push

    public GridView gv;
    public String[] items = new String[64];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gv = (GridView) this.findViewById(R.id.mygrid);
        CustomGridAdapter gridAdapter = new CustomGridAdapter(GameActivity.this, items);
        gv.setAdapter(gridAdapter);
    }
}
