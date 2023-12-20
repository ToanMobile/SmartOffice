package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;

import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class BlindsFadeAnimation extends ShapeFadeAnimation {
    public BlindsFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 5;
    }

    public void doStep(float f) {
        Path path = new Path();
        int i = this.mWidth / 10;
        int i2 = this.mHeight / 10;
        if (this.transitionType == 1) {
            f = 1.0f - f;
        }
        for (int i3 = 0; i3 < 10; i3++) {
            int i4 = this.subType;
            if (i4 == 5) {
                float f2 = (float) (i3 * i);
                path.addRect(f2, BitmapDescriptorFactory.HUE_RED, (((float) i) * f) + f2, (float) this.mHeight, Path.Direction.CW);
            } else if (i4 == 10) {
                float f3 = (float) (i3 * i2);
                path.addRect(BitmapDescriptorFactory.HUE_RED, f3, (float) this.mWidth, (((float) i2) * f) + f3, Path.Direction.CW);
            }
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
