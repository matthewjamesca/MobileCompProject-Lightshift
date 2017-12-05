package mc.group.seven.mcprojectlightshift;

/**
 * Created by Matthew on 2017-12-04.
 *
 * Object that acts as engine for playing sound effects in game
 */

import android.content.Context;
import android.media.MediaPlayer;

public class SoundPlayer {

    private MediaPlayer mMediaPlayer;

    /**
     * when finished, release the player
     */
    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * Given a reference to a raw mp3 file, play it and wrap up upon completion
     * @param c
     * @param rid
     */
    public void play(Context c, int rid) {
        stop();

        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setVolume(0.25f, 0.25f);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mMediaPlayer.start();
    }

}
