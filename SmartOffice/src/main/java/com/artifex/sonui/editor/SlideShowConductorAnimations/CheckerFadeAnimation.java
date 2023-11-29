package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;
import com.artifex.sonui.editor.SlideShowConductorView;

public class CheckerFadeAnimation extends ShapeFadeAnimation {
    public CheckerFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
        this.subType = 10;
    }

    public void doStep(float f) {
        int i;
        int i2;
        int i3;
        int i4;
        Path path = new Path();
        float f2 = this.transitionType == 1 ? 1.0f - f : f;
        int i5 = this.mWidth / 20;
        int i6 = this.mHeight / 20;
        for (int i7 = 0; i7 < 21; i7++) {
            for (int i8 = 0; i8 < 21; i8++) {
                int i9 = this.subType;
                if (i9 == 10) {
                    i4 = i8 * i6;
                    i3 = i7 * 2 * i5;
                    if (i8 % 2 != 0) {
                        i3 -= i5;
                    }
                    i2 = (int) ((((float) i5) * f2 * 2.0f) + ((float) i3));
                    i = i6 + i4;
                } else if (i9 == 5) {
                    i3 = i8 * i5;
                    i4 = i7 * 2 * i6;
                    if (i8 % 2 != 0) {
                        i4 -= i6;
                    }
                    i = (int) ((((float) i6) * f2 * 2.0f) + ((float) i4));
                    i2 = i3 + i5;
                } else {
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                    i = 0;
                }
                path.addRect((float) i3, (float) i4, (float) i2, (float) i, Path.Direction.CW);
            }
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(path);
            this.viewToAnim.invalidate();
        }
    }
}
