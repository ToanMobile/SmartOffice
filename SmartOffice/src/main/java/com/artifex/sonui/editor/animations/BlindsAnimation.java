package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class BlindsAnimation extends ShapeAnimation {
    public BlindsAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        Path path = new Path();
        int i = this.mWidth / 10;
        int i2 = this.mHeight / 10;
        for (int i3 = 0; i3 < 10; i3++) {
            if (this.mDirection.equals("horz")) {
                float f2 = (float) (i3 * i);
                path.addRect(f2, BitmapDescriptorFactory.HUE_RED, (((float) i) * f) + f2, (float) this.mHeight, Path.Direction.CW);
            } else if (this.mDirection.equals("vert")) {
                float f3 = (float) (i3 * i2);
                path.addRect(BitmapDescriptorFactory.HUE_RED, f3, (float) this.mWidth, (((float) i2) * f) + f3, Path.Direction.CW);
            }
        }
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
