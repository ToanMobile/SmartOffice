package com.artifex.solib.animation;

public abstract class SOAnimationRunningCommand extends SOAnimationCommand {
    public boolean bouncing;
    public float delay;
    public float duration;
    public boolean reversed;
    public int turns;

    public SOAnimationRunningCommand(int i, int i2, boolean z, boolean z2, float f, float f2) {
        super(i);
        this.turns = i2;
        this.reversed = z;
        this.bouncing = z2;
        this.delay = f;
        this.duration = f2;
    }

    public String toString() {
        return String.format("SOAnimationRunningCommand(%d %d %b %b %.2f %.2f)", new Object[]{Integer.valueOf(this.layer), Integer.valueOf(this.turns), Boolean.valueOf(this.reversed), Boolean.valueOf(this.bouncing), Float.valueOf(this.delay), Float.valueOf(this.duration)});
    }
}
