package com.artifex.sonui.editor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class Slider extends View implements View.OnTouchListener {
    public float current = 0.5f;
    public boolean logarithmic = false;
    public OnSliderEventListener mListener = null;
    public float max = 1.0f;
    public float min = BitmapDescriptorFactory.HUE_RED;
    public float minp;
    public float minv;
    public final Paint paint = new Paint();
    public float scale;
    public int thumbColor = -1;
    public int thumbRadius = 10;
    public int trackColor = 0;
    public int trackWidth = 1;

    public interface OnSliderEventListener {
        void onFinished(View view, float f);

        void onSlide(View view, float f);
    }

    public Slider(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public final void constrainValues() {
        float f = this.current;
        float f2 = this.min;
        if (f < f2) {
            this.current = f2;
        }
        float f3 = this.current;
        float f4 = this.max;
        if (f3 > f4) {
            this.current = f4;
        }
    }

    public final void init(AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.sodk_editor_Slider, 0, 0);
            this.trackColor = obtainStyledAttributes.getColor(R.styleable.sodk_editor_Slider_trackColor, 0);
            this.thumbColor = obtainStyledAttributes.getColor(R.styleable.sodk_editor_Slider_thumbColor, -1);
            this.trackWidth = (int) obtainStyledAttributes.getDimension(R.styleable.sodk_editor_Slider_trackWidth, 1.0f);
            this.thumbRadius = (int) obtainStyledAttributes.getDimension(R.styleable.sodk_editor_Slider_thumbRadius, 10.0f);
            obtainStyledAttributes.recycle();
        }
        setupLogScaling();
        setOnTouchListener(this);
    }

    public void onDraw(Canvas canvas) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int i = this.trackColor;
        int i2 = this.thumbColor;
        if (!isEnabled()) {
            int parseColor = Color.parseColor("#aaaaaa");
            i2 = Color.parseColor("#aaaaaa");
            i = parseColor;
        }
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth((float) this.trackWidth);
        this.paint.setColor(i);
        float f = (float) (measuredHeight / 2);
        canvas.drawLine(BitmapDescriptorFactory.HUE_RED, f, (float) measuredWidth, f, this.paint);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setColor(i2);
        int i3 = this.thumbRadius;
        canvas.drawCircle((float) (((int) ((this.current / (this.max - this.min)) * ((float) (measuredWidth - (i3 * 2))))) + i3), f, (float) i3, this.paint);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
        if (r3 != 3) goto L_0x004c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouch(View r3, android.view.MotionEvent r4) {
        /*
            r2 = this;
            boolean r3 = r2.isEnabled()
            r0 = 1
            if (r3 != 0) goto L_0x0008
            return r0
        L_0x0008:
            int r3 = r4.getAction()
            if (r3 == 0) goto L_0x0025
            if (r3 == r0) goto L_0x0017
            r1 = 2
            if (r3 == r1) goto L_0x0025
            r4 = 3
            if (r3 == r4) goto L_0x0017
            goto L_0x004c
        L_0x0017:
            com.artifex.sonui.editor.Slider$OnSliderEventListener r3 = r2.mListener
            if (r3 == 0) goto L_0x004c
            float r4 = r2.current
            float r4 = r2.pos2val(r4)
            r3.onFinished(r2, r4)
            goto L_0x004c
        L_0x0025:
            float r3 = r4.getX()
            int r4 = r2.getMeasuredWidth()
            float r4 = (float) r4
            float r3 = r3 / r4
            float r4 = r2.min
            float r1 = r2.max
            float r3 = androidx.appcompat.graphics.drawable.DrawerArrowDrawable$$ExternalSyntheticOutline0.m(r1, r4, r3, r4)
            r2.current = r3
            r2.constrainValues()
            r2.invalidate()
            com.artifex.sonui.editor.Slider$OnSliderEventListener r3 = r2.mListener
            if (r3 == 0) goto L_0x004c
            float r4 = r2.current
            float r4 = r2.pos2val(r4)
            r3.onSlide(r2, r4)
        L_0x004c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.Slider.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public void setCurrent(float f) {
        this.current = val2pos(f);
        constrainValues();
        invalidate();
    }

    public void setLogarithmic(boolean z) {
        this.logarithmic = z;
    }

    public void setParameters(float f, float f2, float f3) {
        this.min = f;
        this.max = f2;
        this.current = val2pos(f3);
        constrainValues();
        setupLogScaling();
    }

    public void setSliderEventListener(OnSliderEventListener onSliderEventListener) {
        this.mListener = onSliderEventListener;
    }

    public final void setupLogScaling() {
        float f = this.min;
        this.minp = f;
        float f2 = this.max;
        this.minv = (float) Math.log((double) f);
        this.scale = (((float) Math.log((double) this.max)) - this.minv) / (f2 - this.minp);
    }

    public final float val2pos(float f) {
        if (!this.logarithmic) {
            return f;
        }
        return ((((float) Math.log((double) f)) - this.minv) / this.scale) + this.minp;
    }

    public Slider(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public Slider(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }
}
