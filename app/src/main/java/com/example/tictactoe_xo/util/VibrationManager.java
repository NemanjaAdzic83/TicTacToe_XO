package com.example.tictactoe_xo.util;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 * Upravllja vibracijom uredjaja
 */
public class VibrationManager {

    private final Vibrator vibrator;
    private boolean enabled;

    public VibrationManager(Context context, boolean enabled) {
        this.enabled = enabled;
        vibrator = (Vibrator) context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrateTap(){
        vibrate(25);
    }
    public void vibrateWIn(){
        vibrate(80);
    }

    private void vibrate(long duration) {
        if (!enabled || vibrator == null || !vibrator.hasVibrator()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            vibrator.vibrate(VibrationEffect.createOneShot(duration,VibrationEffect.DEFAULT_AMPLITUDE));
        }else {
            vibrator.vibrate(duration);
        }
    }
}
