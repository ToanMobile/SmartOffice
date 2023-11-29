package com.artifex.solib.animation;

import android.graphics.PointF;

public class SOAnimationPlotCommand extends SOAnimationImmediateCommand {
    public PointF position;
    public int zPosition;

    public SOAnimationPlotCommand(int i, float f, PointF pointF, int i2) {
        super(i, f);
        this.position = pointF;
        this.zPosition = i2;
    }

    public String toString() {
        return String.format("SOAnimationPlotCommand(%s (%.2f, %.2f) %d)", new Object[]{super.toString(), Float.valueOf(this.position.x), Float.valueOf(this.position.y), Integer.valueOf(this.zPosition)});
    }
}
