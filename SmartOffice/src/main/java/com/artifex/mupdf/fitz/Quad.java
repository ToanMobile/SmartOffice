package com.artifex.mupdf.fitz;

import a.a.a.a.a.c$$ExternalSyntheticOutline0;
import java.util.Objects;

public class Quad {
    public float ll_x;
    public float ll_y;
    public float lr_x;
    public float lr_y;
    public float ul_x;
    public float ul_y;
    public float ur_x;
    public float ur_y;

    public Quad(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        this.ul_x = f;
        this.ul_y = f2;
        this.ur_x = f3;
        this.ur_y = f4;
        this.ll_x = f5;
        this.ll_y = f6;
        this.lr_x = f7;
        this.lr_y = f8;
    }

    public boolean contains(float f, float f2) {
        if (!triangleContainsPoint(f, f2, this.ul_x, this.ul_y, this.ur_x, this.ur_y, this.lr_x, this.lr_y)) {
            return triangleContainsPoint(f, f2, this.ul_x, this.ul_y, this.lr_x, this.lr_y, this.ll_x, this.ll_y);
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Quad)) {
            return false;
        }
        Quad quad = (Quad) obj;
        if (this.ul_x == quad.ul_x && this.ul_y == quad.ul_y && this.ur_x == quad.ur_x && this.ur_y == quad.ur_y && this.ll_x == quad.ll_x && this.ll_y == quad.ll_y && this.lr_x == quad.lr_x && this.lr_y == quad.lr_y) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Float.valueOf(this.ul_x), Float.valueOf(this.ul_y), Float.valueOf(this.ur_x), Float.valueOf(this.ur_y), Float.valueOf(this.ll_x), Float.valueOf(this.ll_y), Float.valueOf(this.lr_x), Float.valueOf(this.lr_y)});
    }

    public Rect toRect() {
        return new Rect(Math.min(Math.min(this.ul_x, this.ur_x), Math.min(this.ll_x, this.lr_x)), Math.min(Math.min(this.ul_y, this.ur_y), Math.min(this.ll_y, this.lr_y)), Math.max(Math.max(this.ul_x, this.ur_x), Math.max(this.ll_x, this.lr_x)), Math.max(Math.max(this.ul_y, this.ur_y), Math.max(this.ll_y, this.lr_y)));
    }

    public String toString() {
        StringBuilder m = c$$ExternalSyntheticOutline0.m("[");
        m.append(this.ul_x);
        m.append(" ");
        m.append(this.ul_y);
        m.append(" ");
        m.append(this.ur_x);
        m.append(" ");
        m.append(this.ur_y);
        m.append(" ");
        m.append(this.ll_x);
        m.append(" ");
        m.append(this.ll_y);
        m.append(" ");
        m.append(this.lr_x);
        m.append(" ");
        m.append(this.lr_y);
        m.append("]");
        return m.toString();
    }

    public Quad transform(Matrix matrix) {
        float f = this.ul_x;
        float f2 = matrix.f2131a;
        float f3 = this.ul_y;
        float f4 = matrix.c;
        float f5 = (f3 * f4) + (f * f2);
        float f6 = matrix.e;
        float f7 = matrix.b;
        float f8 = matrix.d;
        float f9 = matrix.f;
        float f10 = this.ur_x;
        float f11 = this.ur_y;
        float f12 = f11 * f4;
        float f13 = this.ll_x;
        float f14 = this.ll_y;
        float f15 = f14 * f4;
        float f16 = this.lr_x;
        float f17 = this.lr_y;
        float f18 = f4 * f17;
        this.ul_x = f5 + f6;
        this.ul_y = (f3 * f8) + (f * f7) + f9;
        this.ur_x = f12 + (f10 * f2) + f6;
        this.ur_y = (f11 * f8) + (f10 * f7) + f9;
        this.ll_x = f15 + (f13 * f2) + f6;
        this.ll_y = (f14 * f8) + (f13 * f7) + f9;
        this.lr_x = f18 + (f2 * f16) + f6;
        this.lr_y = (f17 * f8) + (f16 * f7) + f9;
        return this;
    }

    public Quad transformed(Matrix matrix) {
        Matrix matrix2 = matrix;
        float f = this.ul_x;
        float f2 = matrix2.f2131a;
        float f3 = this.ul_y;
        float f4 = matrix2.c;
        float f5 = matrix2.e;
        float f6 = (f3 * f4) + (f * f2) + f5;
        float f7 = matrix2.b;
        float f8 = matrix2.d;
        float f9 = matrix2.f;
        float f10 = (f3 * f8) + (f * f7) + f9;
        float f11 = this.ur_x;
        float f12 = this.ur_y;
        float f13 = (f12 * f4) + (f11 * f2) + f5;
        float f14 = (f12 * f8) + (f11 * f7) + f9;
        float f15 = this.ll_x;
        float f16 = this.ll_y;
        float f17 = f16 * f4;
        float f18 = (f16 * f8) + (f15 * f7) + f9;
        float f19 = this.lr_x;
        float f20 = this.lr_y;
        float f21 = f4 * f20;
        float f22 = (f20 * f8) + (f19 * f7) + f9;
        return new Quad(f6, f10, f13, f14, f17 + (f15 * f2) + f5, f18, f21 + (f2 * f19) + f5, f22);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0052 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean triangleContainsPoint(float r7, float r8, float r9, float r10, float r11, float r12, float r13, float r14) {
        /*
            r6 = this;
            float r0 = r10 * r13
            float r1 = r9 * r14
            float r0 = r0 - r1
            float r0 = androidx.appcompat.graphics.drawable.DrawerArrowDrawable$$ExternalSyntheticOutline0.m(r14, r10, r7, r0)
            float r0 = androidx.appcompat.graphics.drawable.DrawerArrowDrawable$$ExternalSyntheticOutline0.m(r9, r13, r8, r0)
            float r1 = r9 * r12
            float r2 = r10 * r11
            float r1 = r1 - r2
            float r7 = androidx.appcompat.graphics.drawable.DrawerArrowDrawable$$ExternalSyntheticOutline0.m(r10, r12, r7, r1)
            float r7 = androidx.appcompat.graphics.drawable.DrawerArrowDrawable$$ExternalSyntheticOutline0.m(r11, r9, r8, r7)
            r8 = 1
            r1 = 0
            r2 = 0
            int r3 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r3 >= 0) goto L_0x0023
            r4 = 1
            goto L_0x0024
        L_0x0023:
            r4 = 0
        L_0x0024:
            int r5 = (r7 > r1 ? 1 : (r7 == r1 ? 0 : -1))
            if (r5 >= 0) goto L_0x002a
            r5 = 1
            goto L_0x002b
        L_0x002a:
            r5 = 0
        L_0x002b:
            if (r4 == r5) goto L_0x002e
            return r2
        L_0x002e:
            float r2 = -r12
            float r2 = r2 * r13
            float r10 = androidx.appcompat.graphics.drawable.DrawerArrowDrawable$$ExternalSyntheticOutline0.m(r13, r11, r10, r2)
            float r9 = androidx.appcompat.graphics.drawable.DrawerArrowDrawable$$ExternalSyntheticOutline0.m(r12, r14, r9, r10)
            float r11 = r11 * r14
            float r11 = r11 + r9
            int r9 = (r11 > r1 ? 1 : (r11 == r1 ? 0 : -1))
            if (r9 >= 0) goto L_0x0048
            if (r3 > 0) goto L_0x0052
            float r0 = r0 + r7
            int r7 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r7 < 0) goto L_0x0052
            goto L_0x0053
        L_0x0048:
            int r9 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r9 < 0) goto L_0x0052
            float r0 = r0 + r7
            int r7 = (r0 > r11 ? 1 : (r0 == r11 ? 0 : -1))
            if (r7 > 0) goto L_0x0052
            goto L_0x0053
        L_0x0052:
            r8 = 0
        L_0x0053:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.mupdf.fitz.Quad.triangleContainsPoint(float, float, float, float, float, float, float, float):boolean");
    }

    public boolean contains(Point point) {
        return contains(point.x, point.y);
    }

    public Quad(Rect rect) {
        float f = rect.x0;
        this.ul_x = f;
        float f2 = rect.y0;
        this.ul_y = f2;
        float f3 = rect.x1;
        this.ur_x = f3;
        this.ur_y = f2;
        this.ll_x = f;
        float f4 = rect.y1;
        this.ll_y = f4;
        this.lr_x = f3;
        this.lr_y = f4;
    }
}
