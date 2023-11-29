package com.artifex.solib.animation;

public class SOAnimationFadeCommand extends SOAnimationRunningCommand {
    public int effect;
    public float endOpacity;
    public int profile;
    public float startOpacity;
    public int subType;

    public SOAnimationFadeCommand(int i, int i2, boolean z, boolean z2, float f, float f2, int i3, int i4, float f3, float f4, int i5) {
        super(i, i2, z, z2, f, f2);
        this.effect = i3;
        this.subType = i4;
        this.startOpacity = f3;
        this.endOpacity = f4;
        this.profile = i5;
    }

    public String toString() {
        return String.format("SOAnimationFadeCommand(%s %d %.2f %.2f %d)", new Object[]{super.toString(), Integer.valueOf(this.effect), Float.valueOf(this.startOpacity), Float.valueOf(this.endOpacity), Integer.valueOf(this.profile)});
    }
}
