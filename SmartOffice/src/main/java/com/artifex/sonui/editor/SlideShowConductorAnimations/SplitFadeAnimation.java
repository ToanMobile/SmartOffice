package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;

import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class SplitFadeAnimation extends ShapeFadeAnimation {
    public SplitFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 21;
    }

    public void doStep(float f) {
        Path path = new Path();
        if (this.transitionType == 0) {
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight, Path.Direction.CCW);
        }
        int i = this.mHeight;
        int i2 = (int) (((float) i) * f);
        int i3 = this.mWidth;
        int i4 = (int) (((float) i3) * f);
        int i5 = this.subType;
        if (i5 == 21) {
            int i6 = i4 / 2;
            Path path2 = path;
            path2.addRect((float) i6, BitmapDescriptorFactory.HUE_RED, (float) (i3 - i6), (float) i, Path.Direction.CW);
        } else if (i5 == 26) {
            int i7 = i2 / 2;
            path.addRect(BitmapDescriptorFactory.HUE_RED, (float) i7, (float) i3, (float) (i - i7), Path.Direction.CW);
        } else if (i5 == 37) {
            int i8 = i4 / 2;
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) ((i3 / 2) - i8), (float) i, Path.Direction.CW);
            int i9 = this.mWidth;
            path.addRect((float) ((i9 / 2) + i8), BitmapDescriptorFactory.HUE_RED, (float) i9, (float) this.mHeight, Path.Direction.CW);
        } else if (i5 == 42) {
            int i10 = i2 / 2;
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) i3, (float) ((i / 2) - i10), Path.Direction.CW);
            int i11 = this.mHeight;
            path.addRect(BitmapDescriptorFactory.HUE_RED, (float) ((i11 / 2) + i10), (float) this.mWidth, (float) i11, Path.Direction.CW);
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.postInvalidate();
        }
    }
}
