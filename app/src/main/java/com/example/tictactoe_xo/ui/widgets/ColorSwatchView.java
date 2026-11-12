package com.example.tictactoe_xo.ui.widgets;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Kruzni swatch koji prkazuje jednu boju iz palete za biranje boje igraca X ili O
 * Kada je izabran , prikazuje istaknut bijeli presten oko sebe i blagi glow u svojoj boji
 */

public class ColorSwatchView extends View {

    private int color = Color.WHITE;
    private boolean selected = false;

    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint glowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public ColorSwatchView(Context context) {
        super(context);
        init();
    }

    public ColorSwatchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        fillPaint.setStyle(Paint.Style.FILL);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(6f);
        ringPaint.setColor(Color.WHITE);

        glowPaint.setStyle(Paint.Style.FILL);
        glowPaint.setMaskFilter(new BlurMaskFilter(14f, BlurMaskFilter.Blur.NORMAL));
    }

    public void setColorValue(int color){
        this.color = color;
        fillPaint.setColor(color);
        glowPaint.setColor(color);
        invalidate();
    }

    public int getColorValue(){
        return color;
    }

    public void setSelected2(boolean selected){
        this.selected = selected;
        invalidate();
    }

    boolean isSelected2(){
        return selected;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;
        float radius = Math.min(getWidth(), getHeight()) /2f * 0.78f;

        if (selected){
            glowPaint.setAlpha(110);
            canvas.drawCircle(cx, cy, radius * 1.25f, glowPaint);
        }

        canvas.drawCircle(cx, cy, radius, fillPaint);

        if (selected){
            canvas.drawCircle(cx , cy, radius + 6f, ringPaint);
        }
    }

}
