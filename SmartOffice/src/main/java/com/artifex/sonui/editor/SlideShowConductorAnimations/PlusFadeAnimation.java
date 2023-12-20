package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;

import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class PlusFadeAnimation extends ShapeFadeAnimation {
    public PlusFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
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
        int i2 = this.mHeight;
        Path path2 = path;
        path2.addRect(BitmapDescriptorFactory.HUE_RED, ((float) (i2 / 2)) - (((float) (i2 / 2)) * f), (float) this.mWidth, (((float) (i2 / 2)) * f) + ((float) (i2 / 2)), Path.Direction.CW);
        int i3 = this.mWidth;
        Path path3 = path;
        path3.addRect(((float) (i3 / 2)) - (((float) (i3 / 2)) * f), BitmapDescriptorFactory.HUE_RED, (((float) (i3 / 2)) * f) + ((float) (i3 / 2)), (float) this.mHeight, Path.Direction.CW);
        int i4 = this.mWidth;
        int i5 = this.mHeight;
        float f2 = ((float) (i5 / 2)) * f;
        Path path4 = path;
        path4.addRect(((float) (i4 / 2)) - (((float) (i4 / 2)) * f), ((float) (i5 / 2)) - (((float) (i5 / 2)) * f), ((float) (i4 / 2)) + (((float) (i4 / 2)) * f), f2 + ((float) (i5 / 2)), Path.Direction.CCW);
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
