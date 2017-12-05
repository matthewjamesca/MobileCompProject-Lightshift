package mc.group.seven.mcprojectlightshift;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by Matthew on 2017-12-04.
 */

public class BackgroundSoundService extends Service {
    private static final String TAG = null;
    MediaPlayer player;
    public IBinder onBind(Intent arg0) {

        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        // create media player for music and run in background at low volume.
        player = MediaPlayer.create(this, R.raw.ossuary_rest);
        player.setLooping(true); // Set looping
        player.setVolume(0.35f, 0.35f);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }

    @Override
    public void onLowMemory() {
        // do nothing.
    }
}
