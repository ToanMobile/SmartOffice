package com.artifex.solib.animation;

public class SOAnimationDisposeCommand extends SOAnimationCommand {
    public SOAnimationDisposeCommand(int i) {
        super(i);
    }

    public String toString() {
        return String.format("SOAnimationDisposeCommand(%s)", new Object[]{super.toString()});
    }
}
