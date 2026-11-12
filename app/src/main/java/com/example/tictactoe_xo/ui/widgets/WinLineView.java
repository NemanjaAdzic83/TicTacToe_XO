package com.example.tictactoe_xo.ui.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.GridLayout;

import androidx.annotation.Nullable;

import com.example.tictactoe_xo.model.Player;


/**
 * Transparentan overlay View koji se postavlja preko table i animirano
 * crta neonsku liniju preko tri pobjednička polja kada partija završi
 * pobjedom. Pozicije krajnjih tačaka linije se računaju na osnovu rasporeda
 * GridLayout-a (3x3) i prosljeđuju spolja preko setLine().
 */
public class WinLineView extends View {

    private float startX, startY, endX, endY;
    private boolean hasLine = false;
    private float progress = 0f;
    private int lineColor = Color.YELLOW;

    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WinLineView(Context context) {
        super(context);
        init();
    }

    public WinLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(14f);

        glowPaint.setStyle(Paint.Style.STROKE);
        glowPaint.setStrokeCap(Paint.Cap.ROUND);
        glowPaint.setStrokeWidth(30f);
        glowPaint.setMaskFilter(new BlurMaskFilter(22f, BlurMaskFilter.Blur.NORMAL));
    }

    public void setLineColor(int color) {
        this.lineColor = color;
        linePaint.setColor(color);
        glowPaint.setColor(color);
        invalidate();
    }

    /**
     * Postavlja koordinate linije (u pikselima, relativno na ovaj View) i
     * pokreće animaciju "crtanja" linije od početne do krajnje tačke.
     */
    public void animateLine(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.hasLine = true;

        linePaint.setColor(lineColor);
        glowPaint.setColor(lineColor);

        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(420);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(anim -> {
            progress = (float) anim.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    public void clearLine() {
        hasLine = false;
        progress = 0f;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!hasLine || progress <= 0f) return;

        float currentEndX = startX + (endX - startX) * progress;
        float currentEndY = startY + (endY - startY) * progress;

        canvas.drawLine(startX, startY, currentEndX, currentEndY, glowPaint);
        canvas.drawLine(startX, startY, currentEndX, currentEndY, linePaint);
    }

    public boolean hasLine() {
        return hasLine;
    }
}