package ch.ma.magischemiesmuschel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    private float startX = 0;
    private float startY = 0;
    private float endX = 0;
    private float endY = 0;

    private void init() {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(15);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    public void drawLine(float startX, float startY, float endX, float endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        invalidate(); // Trigger onDraw to redraw the line
    }

    public void clearCanvas() {
        startX = startY = endX = endY = 0;
        invalidate(); // Trigger onDraw to clear the canvas
    }

}