package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;
import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class DiamondFadeAnimation extends ShapeFadeAnimation {
    public DiamondFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 16;
    }

    public void doStep(float f) {
        Path path = new Path();
        int i = this.subType;
        if (i == 16) {
            f = 1.0f - f;
        }
        int i2 = this.transitionType;
        if ((i2 == 1 && i == 32) || (i2 == 0 && i == 16)) {
            path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight, Path.Direction.CCW);
        }
        int i3 = this.mWidth;
        path.moveTo(((float) (i3 / 2)) - (((float) i3) * f), (float) (this.mHeight / 2));
        int i4 = this.mHeight;
        path.lineTo((float) (this.mWidth / 2), ((float) (i4 / 2)) - (((float) i4) * f));
        int i5 = this.mWidth;
        path.lineTo((((float) i5) * f) + ((float) (i5 / 2)), (float) (this.mHeight / 2));
        int i6 = this.mHeight;
        path.lineTo((float) (this.mWidth / 2), (((float) i6) * f) + ((float) (i6 / 2)));
        path.close();
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
