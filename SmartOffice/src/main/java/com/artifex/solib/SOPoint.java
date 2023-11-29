package com.artifex.solib;

import android.graphics.PointF;

public class SOPoint extends PointF {
    public int type = 0;

    public SOPoint(int i, int i2, int i3) {
        this.x = (float) i;
        this.y = (float) i2;
        if (i3 >= 0 && i3 <= 1) {
            this.type = i3;
        }
    }

    public SOPoint(float f, float f2, int i) {
        this.x = f;
        this.y = f2;
        if (i >= 0 && i <= 1) {
            this.type = i;
        }
    }

    public SOPoint(PointF pointF, int i) {
        this.x = pointF.x;
        this.y = pointF.y;
        if (i >= 0 && i <= 1) {
            this.type = i;
        }
    }
}
