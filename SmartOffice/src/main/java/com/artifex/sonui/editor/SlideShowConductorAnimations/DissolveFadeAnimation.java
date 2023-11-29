package com.artifex.sonui.editor.SlideShowConductorAnimations;

import android.graphics.Path;
import com.artifex.sonui.editor.SlideShowConductorView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import java.util.Random;

public class DissolveFadeAnimation extends ShapeFadeAnimation {
    public Path mClipPath = null;
    public Random mGenerator = new Random();

    public DissolveFadeAnimation(int i, boolean z, int i2, SlideShowConductorView slideShowConductorView) {
        super(i, z, i2, slideShowConductorView);
    }

    public void doStep(float f) {
        if (this.mClipPath == null) {
            Path path = new Path();
            this.mClipPath = path;
            if (this.transitionType == 1) {
                path.addRect(BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, (float) this.mWidth, (float) this.mHeight, Path.Direction.CW);
            }
        }
        int duration = (int) (f * ((float) getDuration()));
        for (int i = 0; i < 40; i++) {
            for (int i2 = 0; i2 < 40; i2++) {
                if (this.mGenerator.nextInt((int) getDuration()) <= duration) {
                    int i3 = this.mWidth;
                    float f2 = (float) ((i2 * i3) / 40);
                    float m = (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i2, 1, i3, 40);
                    int i4 = this.mHeight;
                    float f3 = (float) ((i * i4) / 40);
                    float m2 = (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i, 1, i4, 40);
                    if (this.transitionType == 1) {
                        this.mClipPath.addRect(f2, f3, m, m2, Path.Direction.CCW);
                    } else {
                        this.mClipPath.addRect(f2, f3, m, m2, Path.Direction.CW);
                    }
                }
            }
        }
        SlideShowConductorView slideShowConductorView = this.viewToAnim;
        if (slideShowConductorView != null) {
            slideShowConductorView.setClipPath(this.mClipPath);
            this.viewToAnim.invalidate();
        }
    }
}
