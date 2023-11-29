package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class WedgeAnimation extends ShapeAnimation {
    public WedgeAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        float f2 = f * 180.0f;
        int i = this.mHeight;
        int i2 = this.mWidth;
        int sqrt = (int) Math.sqrt((double) ((i2 * i2) + (i * i)));
        int i3 = this.mWidth;
        int i4 = this.mHeight;
        RectF rectF = new RectF((float) ((i3 / 2) - sqrt), (float) ((i4 / 2) - sqrt), (float) ((i3 / 2) + sqrt), (float) ((i4 / 2) + sqrt));
        Path path = new Path();
        path.moveTo(rectF.centerX(), rectF.centerY());
        path.arcTo(rectF, (-90.0f - f2) + 0.01f, (f2 * 2.0f) - 0.1f);
        path.lineTo(rectF.centerX(), rectF.centerY());
        path.close();
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
