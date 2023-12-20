package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;

import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class CircleFadeAnimation extends ShapeFadeAnimation {
    public CircleFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 16;
    }

    public void doStep(float f) {
        if (this.subType == 16) {
            f = 1.0f - f;
        }
        Path path = new Path();
        int i = this.transitionType;
        if ((i == 1 && this.subType == 32) || (i == 0 && this.subType == 16)) {
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight, Path.Direction.CCW);
        }
        int i2 = this.mWidth;
        int i3 = this.mHeight;
        path.addCircle((float) (this.mWidth / 2), (float) (this.mHeight / 2), ((float) Math.sqrt(((double) (i3 * i3) / 4) + ((double) (i2 * i2) / 4))) * f, Path.Direction.CW);
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
