package com.example.tictactoe_xo.util;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import com.example.tictactoe_xo.R;

/**
 * Upravlja svim zvucnim efektima u igri pomocu SounPool-a
 * koji je optimalan za kratke zvucne isjecke. Zvuk se moze iskljuciti preko podesavanja
 */

public class SoundManager {

    private final SoundPool soundPool;
    private final int soundMoveX;
    private final int soundMoveO;
    private final int soundWin;
    private final int soundDraw;
    private static  final int TOTAL_SOUNDS = 4;
    private int loadedCount = 0;

    private boolean enabled;

    public SoundManager(Context context, boolean enabled){
        this.enabled = enabled;

        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(attributes)
                .build();

        soundPool.setOnLoadCompleteListener(((pool, sampledId, status) -> loadedCount++));

        soundMoveX = soundPool.load(context, R.raw.move_x,1);
        soundMoveO = soundPool.load(context, R.raw.move_o,1);
        soundWin = soundPool.load(context,R.raw.win,1);
        soundDraw = soundPool.load(context,R.raw.draw,1);
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public void playMoveX(){
        play(soundMoveX);
    }
    public void playMoveO(){
        play(soundMoveO);
    }
    public void playWin(){
        play(soundWin);
    }
    public void playDraw(){
        play(soundDraw);
    }

    private void play(int soundId) {
        if (!enabled || loadedCount < TOTAL_SOUNDS) return;
        soundPool.play(soundId,0.8f,0.8f,1,0,1f);
    }
    public void release(){
        soundPool.release();
    }

}
