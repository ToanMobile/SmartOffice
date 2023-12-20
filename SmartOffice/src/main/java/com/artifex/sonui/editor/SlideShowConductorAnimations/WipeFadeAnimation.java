package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;

import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class WipeFadeAnimation extends ShapeFadeAnimation {
    public WipeFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 1;
    }

    public void doStep(float f) {
        int i = (int) (((float) this.mHeight) * f);
        int i2 = (int) (((float) this.mWidth) * f);
        Path path = new Path();
        if (this.transitionType == 1) {
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight, Path.Direction.CCW);
        }
        int i3 = this.subType;
        if (i3 == 1) {
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) i, Path.Direction.CW);
        } else if (i3 == 2) {
            int i4 = this.mWidth;
            path.addRect((float) (i4 - i2), BitmapDescriptorFactory.HUE_RED, (float) i4, (float) this.mHeight, Path.Direction.CW);
        } else if (i3 == 4) {
            int i5 = this.mHeight;
            path.addRect(BitmapDescriptorFactory.HUE_RED, (float) (i5 - i), (float) this.mWidth, (float) i5, Path.Direction.CW);
        } else if (i3 == 8) {
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) i2, (float) this.mHeight, Path.Direction.CW);
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.postInvalidate();
        }
    }
}
