package com.artifex.mupdf.fitz;

import com.artifex.source.util.a.util_a.a.a.c$$ExternalSyntheticOutline0;

public class Rect {
    private static final int FZ_MAX_INF_RECT = 2147483520;
    private static final int FZ_MIN_INF_RECT = Integer.MIN_VALUE;
    public float x0;
    public float x1;
    public float y0;
    public float y1;

    static {
        Context.init();
    }

    public Rect() {
        this.y0 = 2.14748352E9f;
        this.x0 = 2.14748352E9f;
        this.y1 = -2.14748365E9f;
        this.x1 = -2.14748365E9f;
    }

    public native void adjustForStroke(StrokeState strokeState, Matrix matrix);

    public boolean contains(float f, float f2) {
        if (!isEmpty() && f >= this.x0 && f < this.x1 && f2 >= this.y0 && f2 < this.y1) {
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.x0 >= this.x1 || this.y0 >= this.y1;
    }

    public boolean isInfinite() {
        return this.x0 == -2.14748365E9f && this.y0 == -2.14748365E9f && this.x1 == 2.14748352E9f && this.y1 == 2.14748352E9f;
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
        m.append(this.y1);
        m.append("]");
        return m.toString();
    }

    public Rect transform(Matrix matrix) {
        if (isInfinite() || !isValid()) {
            return this;
        }
        float f = this.x0;
        float f2 = matrix.f2131a;
        float f3 = f * f2;
        float f4 = this.x1;
        float f5 = f2 * f4;
        if (f3 > f5) {
            float f6 = f3;
            f3 = f5;
            f5 = f6;
        }
        float f7 = this.y0;
        float f8 = matrix.c;
        float f9 = f7 * f8;
        float f10 = this.y1;
        float f11 = f8 * f10;
        if (f9 > f11) {
            float f12 = f9;
            f9 = f11;
            f11 = f12;
        }
        float f13 = matrix.e;
        float f14 = f9 + f13 + f3;
        float f15 = f11 + f13 + f5;
        float f16 = matrix.b;
        float f17 = f * f16;
        float f18 = f4 * f16;
        if (f17 > f18) {
            float f19 = f18;
            f18 = f17;
            f17 = f19;
        }
        float f20 = matrix.d;
        float f21 = f7 * f20;
        float f22 = f10 * f20;
        if (f21 > f22) {
            float f23 = f22;
            f22 = f21;
            f21 = f23;
        }
        float f24 = matrix.f;
        this.x0 = f14;
        this.x1 = f15;
        this.y0 = f21 + f24 + f17;
        this.y1 = f22 + f24 + f18;
        return this;
    }

    public void union(Rect rect) {
        if (rect.isValid() && !isInfinite()) {
            if (!isValid() || rect.isInfinite()) {
                this.x0 = rect.x0;
                this.y0 = rect.y0;
                this.x1 = rect.x1;
                this.y1 = rect.y1;
                return;
            }
            float f = rect.x0;
            if (f < this.x0) {
                this.x0 = f;
            }
            float f2 = rect.y0;
            if (f2 < this.y0) {
                this.y0 = f2;
            }
            float f3 = rect.x1;
            if (f3 > this.x1) {
                this.x1 = f3;
            }
            float f4 = rect.y1;
            if (f4 > this.y1) {
                this.y1 = f4;
            }
        }
    }

    public boolean contains(Point point) {
        return contains(point.x, point.y);
    }

    public Rect(float f, float f2, float f3, float f4) {
        this.x0 = f;
        this.y0 = f2;
        this.x1 = f3;
        this.y1 = f4;
    }

    public boolean contains(Rect rect) {
        if (isEmpty() || rect.isEmpty() || rect.x0 < this.x0 || rect.x1 > this.x1 || rect.y0 < this.y0 || rect.y1 > this.y1) {
            return false;
        }
        return true;
    }

    public Rect(Quad quad) {
        this.x0 = quad.ll_x;
        this.y0 = quad.ll_y;
        this.x1 = quad.ur_x;
        this.y1 = quad.ur_y;
    }

    public Rect(Rect rect) {
        this(rect.x0, rect.y0, rect.x1, rect.y1);
    }

    public Rect(RectI rectI) {
        this((float) rectI.x0, (float) rectI.y0, (float) rectI.x1, (float) rectI.y1);
    }
}
