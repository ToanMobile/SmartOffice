package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawSignatureView extends View {
    public Paint paintDraw = new Paint();
    public Paint paintFill = new Paint();
    public Path path = new Path();

    public DrawSignatureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.paintDraw.setAntiAlias(true);
        this.paintDraw.setStrokeWidth((float) Utilities.convertDpToPixel(6.0f));
        this.paintDraw.setColor(-16777216);
        this.paintDraw.setStyle(Paint.Style.STROKE);
        this.paintDraw.setStrokeJoin(Paint.Join.ROUND);
        this.paintFill.setColor(-1);
        this.paintFill.setStyle(Paint.Style.FILL);
    }

    public void erase() {
        this.path = new Path();
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        canvas.drawPaint(this.paintFill);
        canvas.drawPath(this.path, this.paintDraw);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int action = motionEvent.getAction();
        if (action == 0) {
            this.path.moveTo(x, y);
            return true;
        } else if (action != 2) {
            return false;
        } else {
            this.path.lineTo(x, y);
            invalidate();
            return true;
        }
    }

    public Bitmap toBitmap() {
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawPath(this.path, this.paintDraw);
        return createBitmap;
    }
}
