package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import java.util.Arrays;

public class Ruler extends View {
    public boolean changed = false;
    public float[] mGraduations = null;
    public int mOffsetX;
    public int mOffsetY;
    public final Paint mPaint = new Paint();
    public final Rect mRect = new Rect();
    public float mScale;
    public final Rect textBounds = new Rect();

    public Ruler(Context context) {
        super(context);
        init();
    }

    public void drawTextCentered(Canvas canvas, Paint paint, String str, float f, float f2) {
        float textSize = paint.getTextSize();
        Paint.Style style = paint.getStyle();
        paint.setTextSize((getEffectiveScale() * textSize) / 2.0f);
        paint.getTextBounds(str, 0, str.length(), this.textBounds);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawText(str, f - this.textBounds.exactCenterX(), f2 - this.textBounds.exactCenterY(), paint);
        paint.setTextSize(textSize);
        paint.setStyle(style);
    }

    public float getEffectiveScale() {
        return this.mScale;
    }

    public final void init() {
        setWillNotDraw(false);
        this.mPaint.setTypeface(Typeface.DEFAULT_BOLD);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setColor(-16777216);
        Paint paint = this.mPaint;
        paint.setTextSize(paint.getTextSize() * 2.0f);
    }

    public void onUpdate() {
        throw new AssertionError();
    }

    public void setGraduations(float[] fArr) {
        if (!Arrays.equals(fArr, this.mGraduations)) {
            this.changed = true;
        }
        this.mGraduations = fArr;
    }

    public void setOffsetX(int i) {
        if (i != this.mOffsetX) {
            this.changed = true;
        }
        this.mOffsetX = i;
    }

    public void setOffsetY(int i) {
        if (i != this.mOffsetY) {
            this.changed = true;
        }
        this.mOffsetY = i;
    }

    public void setScale(float f) {
        if (f != this.mScale) {
            this.changed = true;
        }
        this.mScale = f;
    }

    public void update(boolean z) {
        if (z) {
            this.changed = true;
        }
        update();
    }

    public void update() {
        if (this.changed) {
            this.changed = false;
            new Handler().post(new Runnable(this) {
                public void run() {
                    this.onUpdate();
                }
            });
        }
    }

    public Ruler(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public Ruler(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }
}
