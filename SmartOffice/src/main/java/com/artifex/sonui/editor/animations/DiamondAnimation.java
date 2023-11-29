package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;

public class DiamondAnimation extends ShapeAnimation {
    public DiamondAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        Path path = new Path();
        int i = this.mWidth;
        path.moveTo(((float) (i / 2)) - (((float) i) * f), (float) (this.mHeight / 2));
        int i2 = this.mHeight;
        path.lineTo((float) (this.mWidth / 2), ((float) (i2 / 2)) - (((float) i2) * f));
        int i3 = this.mWidth;
        path.lineTo((((float) i3) * f) + ((float) (i3 / 2)), (float) (this.mHeight / 2));
        int i4 = this.mHeight;
        path.lineTo((float) (this.mWidth / 2), (((float) i4) * f) + ((float) (i4 / 2)));
        path.close();
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
