package com.artifex.solib.animation;

public class SOAnimationWaitForLayerCommand extends SOAnimationCommand {
    public int waitee;
    public int whence;

    public SOAnimationWaitForLayerCommand(int i, int i2, int i3) {
        super(i);
        this.waitee = i2;
        this.whence = i3;
    }

    public String toString() {
        return String.format("SOAnimationWaitForLayerCommand(%s %d %d)", new Object[]{super.toString(), Integer.valueOf(this.waitee), Integer.valueOf(this.whence)});
    }
}
