package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.applovin.exoplayer2.b.r$$ExternalSyntheticOutline0;

public class CircleAnimation extends ShapeAnimation {
    public CircleAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        Path path = new Path();
        int i = this.mWidth;
        int i2 = this.mHeight;
        path.addCircle((float) (this.mWidth / 2), (float) (this.mHeight / 2), ((float) Math.sqrt((double) r$$ExternalSyntheticOutline0.m(i2, i2, 4, (i * i) / 4))) * f, Path.Direction.CW);
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
