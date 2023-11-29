package com.artifex.sonui.editor;

import android.graphics.Path;
import android.view.animation.Animation;

public interface AnimatableView {
    void clearAnimation();

    Path getClipPath();

    int getMeasuredHeight();

    int getMeasuredWidth();

    void invalidate();

    void postInvalidate();

    void setClipPath(Path path);

    void startAnimation(Animation animation);
}
