package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import kotlin.KotlinVersion;

public class DottedLineView extends View {
    public int lineWidthPx;
    public final Paint mPaint = new Paint();
    public final Path mPath = new Path();
    public float[] mPattern;

    public DottedLineView(Context context) {
        super(context);
        init();
    }

    public final void init() {
        setWillNotDraw(false);
        this.mPaint.setStyle(Paint.Style.STROKE);
        int round = Math.round((getContext().getResources().getDisplayMetrics().xdpi / 160.0f) * ((float) 6));
        this.lineWidthPx = round;
        this.mPaint.setStrokeWidth((float) round);
        this.mPaint.setColor(-16777216);
        this.mPaint.setARGB(KotlinVersion.MAX_COMPONENT_VALUE, 0, 0, 0);
    }

    public void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int i = 0;
        float f = BitmapDescriptorFactory.HUE_RED;
        while (true) {
            float[] fArr = this.mPattern;
            if (i < fArr.length) {
                f += fArr[i];
                i++;
            } else {
                int i2 = (int) f;
                this.mPath.moveTo(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
                this.mPath.lineTo((float) ((measuredWidth / i2) * i2), BitmapDescriptorFactory.HUE_RED);
                canvas.drawPath(this.mPath, this.mPaint);
                return;
            }
        }
    }

    public void setPattern(float[] fArr) {
        this.mPattern = new float[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            this.mPattern[i] = fArr[i] * ((float) this.lineWidthPx);
        }
        this.mPaint.setPathEffect(new DashPathEffect(this.mPattern, BitmapDescriptorFactory.HUE_RED));
        invalidate();
    }

    public DottedLineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public DottedLineView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }
}
