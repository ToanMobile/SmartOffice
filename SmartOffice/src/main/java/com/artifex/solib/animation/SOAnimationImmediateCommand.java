package com.artifex.solib.animation;

public abstract class SOAnimationImmediateCommand extends SOAnimationCommand {
    public float delay;

    public SOAnimationImmediateCommand(int i, float f) {
        super(i);
        this.delay = f;
    }

    public String toString() {
        return String.format("SOAnimationImmediateCommand(%s %.2f)", new Object[]{super.toString(), Float.valueOf(this.delay)});
    }
}
