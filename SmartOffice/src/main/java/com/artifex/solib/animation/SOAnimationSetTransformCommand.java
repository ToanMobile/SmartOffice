package com.artifex.solib.animation;

public class SOAnimationSetTransformCommand extends SOAnimationImmediateCommand {
    public float trfmA;
    public float trfmB;
    public float trfmC;
    public float trfmD;
    public float trfmX;
    public float trfmY;

    public SOAnimationSetTransformCommand(int i, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        super(i, f);
        this.trfmA = f2;
        this.trfmB = f3;
        this.trfmC = f4;
        this.trfmD = f5;
        this.trfmX = f6;
        this.trfmY = f7;
    }

    public String toString() {
        return String.format("SOAnimationSetTransformCommand(%s (%.2f %.2f %.2f %.2f %.2f %.2f)", new Object[]{super.toString(), Float.valueOf(this.trfmA), Float.valueOf(this.trfmB), Float.valueOf(this.trfmC), Float.valueOf(this.trfmD), Float.valueOf(this.trfmX), Float.valueOf(this.trfmY)});
    }
}
