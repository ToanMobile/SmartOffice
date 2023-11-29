package com.artifex.solib.animation;

public class SOAnimationSetOpacityCommand extends SOAnimationImmediateCommand {
    public float opacity;

    public SOAnimationSetOpacityCommand(int i, float f, float f2) {
        super(i, f);
        this.opacity = f2;
    }

    public String toString() {
        return String.format("SOAnimationSetOpacityCommand(%s %.2f)", new Object[]{super.toString(), Float.valueOf(this.opacity)});
    }
}
