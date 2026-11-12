package com.example.tictactoe_xo.util;

import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * Handles window insets for edge-to-edge screens.
 *
 * Since Android 15 (API 35), apps targeting SDK 35+ are always drawn edge-to-edge,
 * i.e. behind the status bar and navigation bar. Content has to be moved away
 * from those bars manually, otherwise buttons end up hidden underneath them.
 */
public final class InsetsHelper {

    private InsetsHelper() {
        // Utility class, no instances
    }

    /**
     * Pads the view by the system bar (and display cutout) insets so content isn't drawn
     * under the status/navigation bars. The view's background still extends behind the bars.
     * On older Android versions without edge-to-edge the insets are 0, so nothing changes.
     */
    public static void applySystemBarPadding(View root) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets bars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }
}
