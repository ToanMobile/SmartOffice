package com.artifex.solib.animation;

public class SOAnimationWaitForEventCommand extends SOAnimationCommand {
    public int event;

    public SOAnimationWaitForEventCommand(int i, int i2) {
        super(i);
        this.event = i2;
    }

    public String toString() {
        return String.format("SOAnimationWaitForEventCommand(%s %d)", new Object[]{super.toString(), Integer.valueOf(this.event)});
    }
}
