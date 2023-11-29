package com.artifex.solib;

import android.graphics.PointF;
import android.graphics.RectF;

public class SOSelectionLimits extends ArDkSelectionLimits {
    private long internal;

    public SOSelectionLimits(long j) {
        this.internal = j;
    }

    private native void destroy();

    public void combine(ArDkSelectionLimits arDkSelectionLimits) {
        combineWith((SOSelectionLimits) arDkSelectionLimits);
    }

    public native void combineWith(SOSelectionLimits sOSelectionLimits);

    public void finalize() throws Throwable {
        try {
            destroy();
        } finally {
            super.finalize();
        }
    }

    public native RectF getBox();

    public native PointF getEnd();

    public native PointF getHandle();

    public native boolean getHasPendingVisualChanges();

    public native boolean getHasSelectionEnd();

    public native boolean getHasSelectionStart();

    public native boolean getIsActive();

    public native boolean getIsCaret();

    public native boolean getIsComposing();

    public native boolean getIsExtensible();

    public native PointF getStart();

    public native void offsetBy(double d, double d2);

    public native void scaleBy(double d);

    public SOSelectionLimits() {
    }
}
