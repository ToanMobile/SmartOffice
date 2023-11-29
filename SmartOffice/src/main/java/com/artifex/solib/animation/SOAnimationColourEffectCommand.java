package com.artifex.solib.animation;

public class SOAnimationColourEffectCommand extends SOAnimationRunningCommand {
    public int effect;

    public SOAnimationColourEffectCommand(int i, int i2, boolean z, boolean z2, float f, float f2, int i3) {
        super(i, i2, z, z2, f, f2);
        this.effect = i3;
    }

    public String toString() {
        return String.format("SOAnimationColourEffectCommand(%s %d)", new Object[]{super.toString(), Integer.valueOf(this.effect)});
    }
}
