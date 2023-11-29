package com.artifex.solib.animation;

import android.graphics.PointF;

public class SOAnimationScaleCommand extends SOAnimationRunningCommand {
    public PointF centre;
    public float endX;
    public float endY;
    public int profile;
    public float startX;
    public float startY;

    public SOAnimationScaleCommand(int i, int i2, boolean z, boolean z2, float f, float f2, float f3, float f4, float f5, float f6, PointF pointF, int i3) {
        super(i, i2, z, z2, f, f2);
        this.startX = f3;
        this.startY = f4;
        this.endX = f5;
        this.endY = f6;
        this.centre = pointF;
        this.profile = i3;
    }

    public String toString() {
        return String.format("SOAnimationScaleCommand(%s (%.2f, %.2f) (%.2f, %.2f) (%.2f, %.2f) %d)", new Object[]{super.toString(), Float.valueOf(this.startX), Float.valueOf(this.startY), Float.valueOf(this.endX), Float.valueOf(this.endY), Float.valueOf(this.centre.x), Float.valueOf(this.centre.y), Integer.valueOf(this.profile)});
    }
}
