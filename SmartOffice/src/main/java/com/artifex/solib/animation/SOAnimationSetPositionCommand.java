package com.artifex.solib.animation;

import android.graphics.PointF;

public class SOAnimationSetPositionCommand extends SOAnimationImmediateCommand {
    public PointF newOrigin;

    public SOAnimationSetPositionCommand(int i, float f, PointF pointF) {
        super(i, f);
        this.newOrigin = pointF;
    }

    public String toString() {
        return String.format("SOAnimationSetPositionCommand(%s (%.2f %.2f))", new Object[]{super.toString(), Float.valueOf(this.newOrigin.x), Float.valueOf(this.newOrigin.y)});
    }
}
