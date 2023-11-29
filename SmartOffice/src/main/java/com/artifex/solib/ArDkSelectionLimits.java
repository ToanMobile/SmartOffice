package com.artifex.solib;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class ArDkSelectionLimits {
    public abstract void combine(ArDkSelectionLimits arDkSelectionLimits);

    public abstract RectF getBox();

    public abstract PointF getEnd();

    public abstract boolean getHasSelectionEnd();

    public abstract boolean getHasSelectionStart();

    public abstract boolean getIsActive();

    public abstract boolean getIsCaret();

    public abstract PointF getStart();
}
