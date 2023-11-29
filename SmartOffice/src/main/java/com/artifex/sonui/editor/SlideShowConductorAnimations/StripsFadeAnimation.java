package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;
import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class StripsFadeAnimation extends ShapeFadeAnimation {
    public StripsFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 3;
    }

    public void doStep(float f) {
        Path path = new Path();
        if (this.transitionType == 1) {
            f = 1.0f - f;
        }
        int i = this.subType;
        if (i == 3) {
            path.moveTo(BitmapDescriptorFactory.HUE_RED, (float) this.mHeight);
            int i2 = this.mHeight;
            path.lineTo(BitmapDescriptorFactory.HUE_RED, ((float) i2) - (((float) (i2 * 2)) * f));
            path.lineTo(((float) (this.mWidth * 2)) * f, (float) this.mHeight);
            path.lineTo(BitmapDescriptorFactory.HUE_RED, (float) this.mHeight);
            path.close();
        } else if (i == 6) {
            path.moveTo(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
            path.lineTo(BitmapDescriptorFactory.HUE_RED, ((float) (this.mHeight * 2)) * f);
            path.lineTo(((float) (this.mWidth * 2)) * f, BitmapDescriptorFactory.HUE_RED);
            path.lineTo(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
            path.close();
        } else if (i == 9) {
            path.moveTo((float) this.mWidth, (float) this.mHeight);
            int i3 = this.mHeight;
            path.lineTo((float) this.mWidth, ((float) i3) - (((float) (i3 * 2)) * f));
            int i4 = this.mWidth;
            path.lineTo(((float) i4) - (((float) (i4 * 2)) * f), (float) this.mHeight);
            path.lineTo((float) this.mWidth, (float) this.mHeight);
            path.close();
        } else if (i == 12) {
            path.moveTo((float) this.mWidth, BitmapDescriptorFactory.HUE_RED);
            path.lineTo((float) this.mWidth, ((float) (this.mHeight * 2)) * f);
            int i5 = this.mWidth;
            path.lineTo(((float) i5) - (((float) (i5 * 2)) * f), BitmapDescriptorFactory.HUE_RED);
            path.lineTo((float) this.mWidth, BitmapDescriptorFactory.HUE_RED);
            path.close();
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.postInvalidate();
        }
    }
}
