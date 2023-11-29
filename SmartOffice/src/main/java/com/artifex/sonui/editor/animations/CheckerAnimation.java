package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;

public class CheckerAnimation extends ShapeAnimation {
    public CheckerAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        int i;
        int i2;
        int i3;
        int i4;
        Path path = new Path();
        int i5 = this.mWidth / 20;
        int i6 = this.mHeight / 20;
        for (int i7 = 0; i7 < 21; i7++) {
            for (int i8 = 0; i8 < 21; i8++) {
                if (this.mDirection.equals("horz")) {
                    i4 = i8 * i6;
                    i2 = i7 * 2 * i5;
                    if (i8 % 2 != 0) {
                        i2 -= i5;
                    }
                    i3 = (int) ((((float) i5) * f * 2.0f) + ((float) i2));
                    i = i6 + i4;
                } else if (this.mDirection.equals("vert")) {
                    i2 = i8 * i5;
                    i4 = i7 * 2 * i6;
                    if (i8 % 2 != 0) {
                        i4 -= i6;
                    }
                    i = (int) ((((float) i6) * f * 2.0f) + ((float) i4));
                    i3 = i2 + i5;
                } else {
                    i4 = 0;
                    i3 = 0;
                    i2 = 0;
                    i = 0;
                }
                path.addRect((float) i2, (float) i4, (float) i3, (float) i, Path.Direction.CW);
            }
        }
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
