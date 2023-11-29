package com.artifex.solib.animation;

public abstract class SOAnimationCommand {
    public int layer;

    public SOAnimationCommand(int i) {
        this.layer = i;
    }

    public String toString() {
        return String.format("SOAnimationCommand(%d)", new Object[]{Integer.valueOf(this.layer)});
    }
}
