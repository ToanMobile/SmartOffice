package com.artifex.sonui.editor.animations;

import android.graphics.Path;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class PlusAnimation extends ShapeAnimation {
    public PlusAnimation(String str, View view, View view2, int i) {
        super(str, view, view2, i);
    }

    public void doStep(float f) {
        Path path = new Path();
        int i = this.mHeight;
        float f2 = ((float) (i / 2)) * f;
        path.addRect(BitmapDescriptorFactory.HUE_RED, ((float) (i / 2)) - (((float) (i / 2)) * f), (float) this.mWidth, f2 + ((float) (i / 2)), Path.Direction.CW);
        int i2 = this.mWidth;
        float f3 = ((float) (i2 / 2)) * f;
        path.addRect(((float) (i2 / 2)) - (((float) (i2 / 2)) * f), BitmapDescriptorFactory.HUE_RED, f3 + ((float) (i2 / 2)), (float) this.mHeight, Path.Direction.CW);
        this.mNewView.setClipPath(path);
        this.mNewView.invalidate();
    }
}
