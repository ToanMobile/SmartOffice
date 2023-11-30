package com.artifex.mupdf.fitz;

import com.artifex.source.util.a.util_a.a.a.c$$ExternalSyntheticOutline0;
import androidx.constraintlayout.core.widgets.ConstraintWidget$$ExternalSyntheticOutline0;

public class RectI {
    private static final int FZ_MAX_INF_RECT = 2147483520;
    private static final int FZ_MIN_INF_RECT = Integer.MIN_VALUE;
    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public RectI() {
        this.y0 = FZ_MAX_INF_RECT;
        this.x0 = FZ_MAX_INF_RECT;
        this.y1 = Integer.MIN_VALUE;
        this.x1 = Integer.MIN_VALUE;
    }

    public boolean contains(int i, int i2) {
        if (!isEmpty() && i >= this.x0 && i < this.x1 && i2 >= this.y0 && i2 < this.y1) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.x0 >= this.x1 || this.y0 >= this.y1;
    }

    public boolean isInfinite() {
        return this.x0 == Integer.MIN_VALUE && this.y0 == Integer.MIN_VALUE && this.x1 == FZ_MAX_INF_RECT && this.y1 == FZ_MAX_INF_RECT;
    }

    public boolean isValid() {
        return this.x0 <= this.x1 || this.y0 <= this.y1;
    }

    public String toString() {
        StringBuilder m = c$$ExternalSyntheticOutline0.m("[");
        m.append(this.x0);
        m.append(" ");
        m.append(this.y0);
        m.append(" ");
        m.append(this.x1);
        m.append(" ");
        return ConstraintWidget$$ExternalSyntheticOutline0.m(m, this.y1, "]");
    }

    public RectI transform(Matrix matrix) {
        if (isInfinite() || !isValid()) {
            return this;
        }
        int i = this.x0;
        float f = matrix.f2131a;
        float f2 = ((float) i) * f;
        int i2 = this.x1;
        float f3 = ((float) i2) * f;
        if (f2 > f3) {
            float f4 = f3;
            f3 = f2;
            f2 = f4;
        }
        int i3 = this.y0;
        float f5 = matrix.c;
        float f6 = ((float) i3) * f5;
        int i4 = this.y1;
        float f7 = ((float) i4) * f5;
        if (f6 > f7) {
            float f8 = f7;
            f7 = f6;
            f6 = f8;
        }
        float f9 = matrix.e;
        float f10 = f6 + f9 + f2;
        float f11 = f7 + f9 + f3;
        float f12 = matrix.b;
        float f13 = ((float) i) * f12;
        float f14 = ((float) i2) * f12;
        if (f13 > f14) {
            float f15 = f14;
            f14 = f13;
            f13 = f15;
        }
        float f16 = (float) i3;
        float f17 = matrix.d;
        float f18 = f16 * f17;
        float f19 = ((float) i4) * f17;
        if (f18 > f19) {
            float f20 = f19;
            f19 = f18;
            f18 = f20;
        }
        float f21 = matrix.f;
        this.x0 = (int) Math.floor((double) f10);
        this.x1 = (int) Math.ceil((double) f11);
        this.y0 = (int) Math.floor((double) (f18 + f21 + f13));
        this.y1 = (int) Math.ceil((double) (f19 + f21 + f14));
        return this;
    }

    public void union(RectI rectI) {
        if (rectI.isValid() && !isInfinite()) {
            if (!isValid() || rectI.isInfinite()) {
                this.x0 = rectI.x0;
                this.y0 = rectI.y0;
                this.x1 = rectI.x1;
                this.y1 = rectI.y1;
                return;
            }
            int i = rectI.x0;
            if (i < this.x0) {
                this.x0 = i;
            }
            int i2 = rectI.y0;
            if (i2 < this.y0) {
                this.y0 = i2;
            }
            int i3 = rectI.x1;
            if (i3 > this.x1) {
                this.x1 = i3;
            }
            int i4 = rectI.y1;
            if (i4 > this.y1) {
                this.y1 = i4;
            }
        }
    }

    public boolean contains(Rect rect) {
        if (isEmpty() || rect.isEmpty() || rect.x0 < ((float) this.x0) || rect.x1 > ((float) this.x1) || rect.y0 < ((float) this.y0) || rect.y1 > ((float) this.y1)) {
            return false;
        }
        return true;
    }

    public RectI(int i, int i2, int i3, int i4) {
        this.x0 = i;
        this.y0 = i2;
        this.x1 = i3;
        this.y1 = i4;
    }

    public RectI(RectI rectI) {
        this(rectI.x0, rectI.y0, rectI.x1, rectI.y1);
    }

    public RectI(Rect rect) {
        this.x0 = (int) Math.floor((double) rect.x0);
        this.y0 = (int) Math.ceil((double) rect.y0);
        this.x1 = (int) Math.floor((double) rect.x1);
        this.y1 = (int) Math.ceil((double) rect.y1);
    }
}
