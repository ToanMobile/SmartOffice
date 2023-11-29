package com.artifex.solib.animation;

public class SOAnimationWaitForTimeCommand extends SOAnimationCommand {
    public float delay;

    public SOAnimationWaitForTimeCommand(int i, float f) {
        super(i);
        this.delay = f;
    }

    public String toString() {
        return String.format("SOAnimationWaitForTimeCommand(%s %.2f)", new Object[]{super.toString(), Float.valueOf(this.delay)});
    }
}
