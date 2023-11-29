package com.artifex.solib.animation;

import android.graphics.PointF;

public class SOAnimationRotateCommand extends SOAnimationRunningCommand {
    public float endAngle;
    public PointF origin;
    public int profile;
    public float startAngle;

    public SOAnimationRotateCommand(int i, int i2, boolean z, boolean z2, float f, float f2, PointF pointF, float f3, float f4, int i3) {
        super(i, i2, z, z2, f, f2);
        this.origin = pointF;
        this.startAngle = f3;
        this.endAngle = f4;
        this.profile = i3;
    }

    public String toString() {
        return String.format("SOAnimationRotateCommand(%s (%.2f, %.2f), %.2f, %.2f, %d)", new Object[]{super.toString(), Float.valueOf(this.origin.x), Float.valueOf(this.origin.y), Float.valueOf(this.startAngle), Float.valueOf(this.endAngle), Integer.valueOf(this.profile)});
    }
}
