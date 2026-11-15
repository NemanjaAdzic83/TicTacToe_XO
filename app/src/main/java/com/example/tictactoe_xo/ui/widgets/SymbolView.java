package com.example.tictactoe_xo.ui.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.example.tictactoe_xo.model.Player;

/**
 * Custom View koji crta simbol X ili O direktno u Canvasu sa neonskim glow efektom
 * , zaokruzuje linijma i animacijom pojavljivanja iscrtavanja poteza. Boja simbola i intezitet
 * glow-a se mogu prilagoditi spolja
 */


public class SymbolView extends View {

    public SymbolView(Context context) {
        super(context);
        init();
    }

    public SymbolView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private Player symbol = Player.EMPTY;
    private int symbolColor = Color.CYAN;

    private final Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    // Reused in every onDraw() call; onDraw runs up to 60x per second during animation
    private final RectF arcRect = new RectF();

    private float drawProgress = 0f; // 0..1 progres animacije iscrtavanja

    private ValueAnimator drawAnimator;

    private void init() {
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);

        glowPaint.setStyle(Paint.Style.STROKE);
        glowPaint.setStrokeCap(Paint.Cap.ROUND);
        glowPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void setSymbolColor(int color) {
        this.symbolColor = color;
        invalidate();
    }

    /** Postavlja simbol i pokreće animaciju iscrtavanja od 0 do 1. */
    public void setSymbolAnimated(Player player) {
        this.symbol = player;
        if (player == Player.EMPTY) {
            drawProgress = 0f;
            invalidate();
            return;
        }
        drawProgress = 0f;
        if (drawAnimator != null) drawAnimator.cancel();
        drawAnimator = ValueAnimator.ofFloat(0f, 1f);
        drawAnimator.setDuration(280);
        drawAnimator.setInterpolator(new OvershootInterpolator(0.9f));
        drawAnimator.addUpdateListener(anim -> {
            drawProgress = (float) anim.getAnimatedValue();
            invalidate();
        });
        drawAnimator.start();
    }

    /** Trenutno postavlja simbol bez animacije (npr. prilikom undo / restore stanja). */
    public void setSymbolInstant(Player player) {
        this.symbol = player;
        drawProgress = player == Player.EMPTY ? 0f : 1f;
        invalidate();
    }

    // Blur radius depends on the view size, so the filter is created only when the size changes
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float strokeW = Math.min(w, h) * 0.11f;
        glowPaint.setMaskFilter(strokeW > 0f
                ? new BlurMaskFilter(strokeW * 0.9f, BlurMaskFilter.Blur.NORMAL)
                : null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (symbol == Player.EMPTY || drawProgress <= 0f) return;

        float w = getWidth();
        float h = getHeight();
        float size = Math.min(w, h);
        float padding = size * 0.22f;
        float strokeW = size * 0.11f;

        strokePaint.setColor(symbolColor);
        strokePaint.setStrokeWidth(strokeW);

        glowPaint.setColor(symbolColor);
        glowPaint.setStrokeWidth(strokeW * 1.9f);
        glowPaint.setAlpha(70);

        float cx = w / 2f;
        float cy = h / 2f;

        if (symbol == Player.X) {
            drawX(canvas, cx, cy, size, padding);
        } else {
            drawO(canvas, cx, cy, size, padding);
        }
    }

    private void drawX(Canvas canvas, float cx, float cy, float size, float padding) {
        float left = padding;
        float right = size - padding;
        float top = padding;
        float bottom = size - padding;

        // Prva dijagonala se crta prvih 50% progresa, druga preostalih 50%
        float firstLineProgress = Math.min(1f, drawProgress * 2f);
        float secondLineProgress = Math.max(0f, (drawProgress - 0.5f) * 2f);

        // Glow ispod
        drawPartialLine(canvas, glowPaint, left, top, right, bottom, firstLineProgress);
        drawPartialLine(canvas, glowPaint, right, top, left, bottom, secondLineProgress);

        // Glavna linija
        drawPartialLine(canvas, strokePaint, left, top, right, bottom, firstLineProgress);
        drawPartialLine(canvas, strokePaint, right, top, left, bottom, secondLineProgress);
    }

    private void drawPartialLine(Canvas canvas, Paint paint, float x1, float y1, float x2, float y2, float progress) {
        if (progress <= 0f) return;
        float currentX2 = x1 + (x2 - x1) * progress;
        float currentY2 = y1 + (y2 - y1) * progress;
        canvas.drawLine(x1, y1, currentX2, currentY2, paint);
    }

    private void drawO(Canvas canvas, float cx, float cy, float size, float padding) {
        arcRect.set(padding, padding, size - padding, size - padding);
        float sweep = 360f * drawProgress;

        canvas.drawArc(arcRect, -90f, sweep, false, glowPaint);
        canvas.drawArc(arcRect, -90f, sweep, false, strokePaint);
    }

}
