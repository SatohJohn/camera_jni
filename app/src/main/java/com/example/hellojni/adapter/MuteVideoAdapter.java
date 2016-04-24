package com.example.hellojni.adapter;

import android.media.MediaPlayer;

/**
 * Created by satohjohn on 16/04/23.
 */
public class MuteVideoAdapter implements MediaPlayer.OnPreparedListener {

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setVolume(0f, 0f);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
