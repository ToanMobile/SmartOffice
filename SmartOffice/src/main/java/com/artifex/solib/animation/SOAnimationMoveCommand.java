package com.artifex.solib.animation;

import android.graphics.PointF;

public class SOAnimationMoveCommand extends SOAnimationRunningCommand {
    public PointF end;
    public int profile;
    public PointF start;

    public SOAnimationMoveCommand(int i, int i2, boolean z, boolean z2, float f, float f2, PointF pointF, PointF pointF2, int i3) {
        super(i, i2, z, z2, f, f2);
        this.start = pointF;
        this.end = pointF2;
        this.profile = i3;
    }

    public String toString() {
        return String.format("SOAnimationMoveCommand(%s (%.2f, %.2f) (%.2f, %.2f) %d)", new Object[]{super.toString(), Float.valueOf(this.start.x), Float.valueOf(this.start.y), Float.valueOf(this.end.x), Float.valueOf(this.end.y), Integer.valueOf(this.profile)});
    }
}
