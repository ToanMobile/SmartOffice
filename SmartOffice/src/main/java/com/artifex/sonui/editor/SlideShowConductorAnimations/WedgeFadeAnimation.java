package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;
import android.graphics.RectF;

import com.artifex.sonui.editor.SlideShowConductorView;

public class WedgeFadeAnimation extends ShapeFadeAnimation {
    public WedgeFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
    }

    public void doStep(float f) {
        if (this.transitionType == 1) {
            f = 1.0f - f;
        }
        float f2 = f * 180.0f;
        int i = this.mHeight;
        int i2 = this.mWidth;
        int sqrt = (int) Math.sqrt((i2 * i2) + (i * i));
        int i3 = this.mWidth;
        int i4 = this.mHeight;
        RectF rectF = new RectF((float) ((i3 / 2) - sqrt), (float) ((i4 / 2) - sqrt), (float) ((i3 / 2) + sqrt), (float) ((i4 / 2) + sqrt));
        Path path = new Path();
        path.moveTo(rectF.centerX(), rectF.centerY());
        path.arcTo(rectF, (-90.0f - f2) + 0.01f, (f2 * 2.0f) - 0.1f);
        path.lineTo(rectF.centerX(), rectF.centerY());
        path.close();
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
