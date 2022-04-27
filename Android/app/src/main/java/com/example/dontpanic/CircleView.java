package com.example.dontpanic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class CircleView extends View {
    private float x = 300;
    private float y = 300;
    private final int r = 75;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Random random = new Random();


    // View constructors
    public CircleView(Context context) {
        super(context);
        init();
    }
    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
    }


    // draws a circle
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(x, y, r, mPaint);
        canvas.restore();
    }


    // gets random (x,y) coordinates within screen
    void generateRandom() {
        int w = getWidth() - r - 50;
        int h = getHeight() - r - 50;

        int x_border = r + 50;
        this.x = x_border + random.nextInt(w - x_border);

        // circle doesn't overlap with instructions or back button
        int y_border = r + 150;
        this.y = y_border + random.nextInt(h - (y_border * 2));
    }


    // Checks if Circle is tapped
    boolean isInsideCircle(float xPoint, float yPoint) {
        float dx = (x - xPoint);
        float dxPow = (float) Math.pow(dx, 2);
        float dy = (y - yPoint);
        float dyPow = (float) Math.pow(dy, 2);
        float radPow = (float) Math.pow(r, 2);
        return (dxPow + dyPow) < radPow || (dxPow + dyPow == radPow);
    }


    // If Circle is tapped, a new circle is drawn
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isInsideCircle(event.getX(), event.getY())) {
            generateRandom();
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}