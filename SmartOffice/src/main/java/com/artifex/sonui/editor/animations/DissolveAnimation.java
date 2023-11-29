package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.artifex.sonui.editor.SlideShowConductorAnimations.DissolveFadeAnimation$$ExternalSyntheticOutline0;
import java.util.Random;

public class DissolveAnimation extends ShapeAnimation {
    public Path mClipPath = new Path();
    public Random mGenerator = new Random();

    public DissolveAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        int duration = (int) (f * ((float) getDuration()));
        for (int i = 0; i < 40; i++) {
            for (int i2 = 0; i2 < 40; i2++) {
                if (this.mGenerator.nextInt((int) getDuration()) <= duration) {
                    int i3 = this.mWidth;
                    float m = (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i2, 1, i3, 40);
                    int i4 = this.mHeight;
                    this.mClipPath.addRect((float) ((i2 * i3) / 40), (float) ((i * i4) / 40), m, (float) DissolveFadeAnimation$$ExternalSyntheticOutline0.m(i, 1, i4, 40), Path.Direction.CW);
                }
            }
        }
        this.mNewView.setClipPath(this.mClipPath);
        this.mNewView.invalidate();
    }
}
