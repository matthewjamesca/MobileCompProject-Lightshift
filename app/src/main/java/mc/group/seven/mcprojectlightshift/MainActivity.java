package mc.group.seven.mcprojectlightshift;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("message");
        mDatabase.setValue("ss");
        mDatabase.child("users").child("zzz").setValue("ccc");
    }

    public void openGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void openDownloadableLevels(View view) {
        Intent intent = new Intent(this, DownloadLevelsActivity.class);
        startActivity(intent);
    }

    public void openLevelSelect(View view) {
        Intent intent = new Intent(this, LevelSelectActivity.class);
        startActivity(intent);
    }

    public void openContactUs(View view) {
        Intent intent = new Intent(this, ContactUsActivity.class);
        startActivity(intent);
    }
}
