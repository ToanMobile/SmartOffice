package com.artifex.solib.animation;

import android.graphics.PointF;

public class SOAnimationRenderCommand extends SOAnimationCommand {
    public float h;
    public PointF origin;
    public int renderable;
    public float w;
    public float x;
    public float y;
    public float zoom;

    public SOAnimationRenderCommand(int i, int i2, float f, PointF pointF, float f2, float f3, float f4, float f5) {
        super(i);
        this.renderable = i2;
        this.zoom = f;
        this.origin = pointF;
        this.x = f2;
        this.y = f3;
        this.w = f4;
        this.h = f5;
    }

    public String toString() {
        return String.format("SOAnimationRenderCommand(%s, %d, %.2f, (%.2f, %.2f), (%.2f, %.2f, %.2f, %.2f))", new Object[]{super.toString(), Integer.valueOf(this.renderable), Float.valueOf(this.zoom), Float.valueOf(this.origin.x), Float.valueOf(this.origin.y), Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.w), Float.valueOf(this.h)});
    }
}
