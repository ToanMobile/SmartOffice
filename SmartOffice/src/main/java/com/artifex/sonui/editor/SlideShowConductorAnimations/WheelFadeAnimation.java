package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;
import android.graphics.RectF;
import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class WheelFadeAnimation extends ShapeFadeAnimation {
    public WheelFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
    }

    public final void doSpokes(int i, Path path, RectF rectF, float f) {
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = 360 / i;
            path.moveTo(rectF.centerX(), rectF.centerY());
            path.arcTo(rectF, (float) ((i2 * i3) - 90), (((float) i3) * f) - 0.05f);
            path.lineTo(rectF.centerX(), rectF.centerY());
        }
    }

    public void doStep(float f) {
        Path path = new Path();
        if (this.transitionType == 1) {
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight, Path.Direction.CCW);
        }
        int i = this.mHeight;
        int i2 = this.mWidth;
        int sqrt = (int) Math.sqrt((double) ((i2 * i2) + (i * i)));
        int i3 = this.mWidth;
        int i4 = this.mHeight;
        RectF rectF = new RectF((float) ((i3 / 2) - sqrt), (float) ((i4 / 2) - sqrt), (float) ((i3 / 2) + sqrt), (float) ((i4 / 2) + sqrt));
        int i5 = this.subType;
        if (i5 == 1) {
            doSpokes(1, path, rectF, f);
        } else if (i5 == 2) {
            doSpokes(2, path, rectF, f);
        } else if (i5 == 3) {
            doSpokes(3, path, rectF, f);
        } else if (i5 == 4) {
            doSpokes(4, path, rectF, f);
        } else if (i5 == 8) {
            doSpokes(8, path, rectF, f);
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
