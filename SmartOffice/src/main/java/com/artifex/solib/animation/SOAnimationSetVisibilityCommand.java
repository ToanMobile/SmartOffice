package com.artifex.solib.animation;

public class SOAnimationSetVisibilityCommand extends SOAnimationImmediateCommand {
    public boolean visible;

    public SOAnimationSetVisibilityCommand(int i, float f, boolean z) {
        super(i, f);
        this.visible = z;
    }

    public String toString() {
        return String.format("SOAnimationSetVisibilityCommand(%s %b)", new Object[]{super.toString(), Boolean.valueOf(this.visible)});
    }
}
