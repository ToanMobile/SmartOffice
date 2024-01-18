package com.artifex.mupdf.fitz;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class Matrix {

    /* renamed from: a  reason: collision with root package name */
    public float f2131a;
    public float b;
    public float c;
    public float d;
    public float e;
    public float f;

    public Matrix(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.f2131a = f2;
        this.b = f3;
        this.c = f4;
        this.d = f5;
        this.e = f6;
        this.f = f7;
    }

    public static Matrix Identity() {
        return new Matrix(1.0f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, 1.0f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static Matrix Inverted(Matrix matrix) {
        float f2 = (matrix.f2131a * matrix.d) - (matrix.b * matrix.c);
        if (f2 > (-Math.ulp(BitmapDescriptorFactory.HUE_RED)) && f2 < Math.ulp(BitmapDescriptorFactory.HUE_RED)) {
            return matrix;
        }
        float f3 = 1.0f / f2;
        float f4 = matrix.d * f3;
        float f5 = (-matrix.b) * f3;
        float f6 = (-matrix.c) * f3;
        float f7 = matrix.f2131a * f3;
        float f8 = matrix.e;
        float f9 = matrix.f;
        return new Matrix(f4, f5, f6, f7, ((-f8) * f4) - (f9 * f6), ((-f8) * f5) - (f9 * f7));
    }

    public static Matrix Rotate(float f2) {
        float f3;
        while (f2 < BitmapDescriptorFactory.HUE_RED) {
            f2 += 360.0f;
        }
        while (f2 >= 360.0f) {
            f2 -= 360.0f;
        }
        float f4 = 1.0f;
        if (((double) Math.abs(BitmapDescriptorFactory.HUE_RED - f2)) < 1.0E-4d) {
            f4 = BitmapDescriptorFactory.HUE_RED;
            f3 = 1.0f;
        } else {
            if (((double) Math.abs(90.0f - f2)) >= 1.0E-4d) {
                if (((double) Math.abs(180.0f - f2)) < 1.0E-4d) {
                    f4 = BitmapDescriptorFactory.HUE_RED;
                    f3 = -1.0f;
                } else if (((double) Math.abs(270.0f - f2)) < 1.0E-4d) {
                    f4 = -1.0f;
                } else {
                    double d2 = (((double) f2) * 3.141592653589793d) / 180.0d;
                    f4 = (float) Math.sin(d2);
                    f3 = (float) Math.cos(d2);
                }
            }
            f3 = BitmapDescriptorFactory.HUE_RED;
        }
        return new Matrix(f3, f4, -f4, f3, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static Matrix Scale(float f2) {
        return new Matrix(f2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, f2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public static Matrix Translate(float f2, float f3) {
        return new Matrix(1.0f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, 1.0f, f2, f3);
    }

    public Matrix concat(Matrix matrix) {
        float f2 = this.f2131a;
        float f3 = matrix.f2131a;
        float f4 = this.b;
        float f5 = matrix.c;
        float f6 = (f4 * f5) + (f2 * f3);
        float f7 = matrix.b;
        float f8 = matrix.d;
        float f9 = (f4 * f8) + (f2 * f7);
        float f10 = this.c;
        float f11 = this.d;
        float f12 = (f11 * f5) + (f10 * f3);
        float f13 = (f11 * f8) + (f10 * f7);
        float f14 = this.e;
        float f15 = this.f;
        float f16 = f5 * f15;
        this.f = (f15 * f8) + (f14 * f7) + matrix.f;
        this.f2131a = f6;
        this.b = f9;
        this.c = f12;
        this.d = f13;
        this.e = f16 + (f3 * f14) + matrix.e;
        return this;
    }

    public Matrix invert() {
        float f2 = (this.f2131a * this.d) - (this.b * this.c);
        if (f2 > (-Math.ulp(BitmapDescriptorFactory.HUE_RED)) && f2 < Math.ulp(BitmapDescriptorFactory.HUE_RED)) {
            return this;
        }
        float f3 = this.f2131a;
        float f4 = this.b;
        float f5 = this.c;
        float f6 = this.d;
        float f7 = this.e;
        float f8 = this.f;
        float f9 = 1.0f / f2;
        float f10 = f6 * f9;
        this.f2131a = f10;
        float f11 = (-f4) * f9;
        this.b = f11;
        float f12 = (-f5) * f9;
        this.c = f12;
        float f13 = f3 * f9;
        this.d = f13;
        float f14 = -f7;
        this.e = (f10 * f14) - (f12 * f8);
        this.f = (f14 * f11) - (f8 * f13);
        return this;
    }

    public Matrix rotate(float f2) {
        while (f2 < BitmapDescriptorFactory.HUE_RED) {
            f2 += 360.0f;
        }
        while (f2 >= 360.0f) {
            f2 -= 360.0f;
        }
        if (((double) Math.abs(BitmapDescriptorFactory.HUE_RED - f2)) >= 1.0E-4d) {
            if (((double) Math.abs(90.0f - f2)) < 1.0E-4d) {
                float f3 = this.f2131a;
                float f4 = this.b;
                this.f2131a = this.c;
                this.b = this.d;
                this.c = -f3;
                this.d = -f4;
            } else if (((double) Math.abs(180.0f - f2)) < 1.0E-4d) {
                this.f2131a = -this.f2131a;
                this.b = -this.b;
                this.c = -this.c;
                this.d = -this.d;
            } else if (((double) Math.abs(270.0f - f2)) < 1.0E-4d) {
                float f5 = this.f2131a;
                float f6 = this.b;
                this.f2131a = -this.c;
                this.b = -this.d;
                this.c = f5;
                this.d = f6;
            } else {
                double d2 = (((double) f2) * 3.141592653589793d) / 180.0d;
                float sin = (float) Math.sin(d2);
                float cos = (float) Math.cos(d2);
                float f7 = this.f2131a;
                float f8 = this.b;
                float f9 = this.c;
                this.f2131a = (sin * f9) + (cos * f7);
                float f10 = this.d;
                this.b = (sin * f10) + (cos * f8);
                float f11 = -sin;
                this.c = (f9 * cos) + (f7 * f11);
                this.d = (cos * f10) + (f11 * f8);
            }
        }
        return this;
    }

    public Matrix scale(float f2, float f3) {
        this.f2131a *= f2;
        this.b *= f2;
        this.c *= f3;
        this.d *= f3;
        return this;
    }

    public String toString() {
        StringBuilder m = new StringBuilder("[");
        m.append(this.f2131a);
        m.append(" ");
        m.append(this.b);
        m.append(" ");
        m.append(this.c);
        m.append(" ");
        m.append(this.d);
        m.append(" ");
        m.append(this.e);
        m.append(" ");
        m.append(this.f);
        m.append("]");
        return m.toString();
    }

    public Matrix translate(float f2, float f3) {
        this.e = (this.c * f3) + (this.f2131a * f2) + this.e;
        this.f = (f3 * this.d) + (f2 * this.b) + this.f;
        return this;
    }

    public static Matrix Scale(float f2, float f3) {
        return new Matrix(f2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, f3, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public Matrix scale(float f2) {
        return scale(f2, f2);
    }

    public Matrix(float f2, float f3, float f4, float f5) {
        this(f2, f3, f4, f5, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public Matrix(float f2, float f3) {
        this(f2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, f3, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public Matrix(float f2) {
        this(f2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, f2, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public Matrix() {
        this(1.0f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED, 1.0f, BitmapDescriptorFactory.HUE_RED, BitmapDescriptorFactory.HUE_RED);
    }

    public Matrix(Matrix matrix) {
        this(matrix.f2131a, matrix.b, matrix.c, matrix.d, matrix.e, matrix.f);
    }

    public Matrix(Matrix matrix, Matrix matrix2) {
        float f2 = matrix.f2131a * matrix2.f2131a;
        float f3 = matrix.b;
        float f4 = matrix2.c;
        this.f2131a = (f3 * f4) + f2;
        float f5 = matrix.f2131a * matrix2.b;
        float f6 = matrix2.d;
        this.b = (f3 * f6) + f5;
        float f7 = matrix.c;
        float f8 = matrix2.f2131a;
        float f9 = matrix.d;
        this.c = (f4 * f9) + (f7 * f8);
        float f10 = matrix.c;
        float f11 = matrix2.b;
        this.d = (f9 * f6) + (f10 * f11);
        float f12 = matrix.e * f8;
        float f13 = matrix.f;
        this.e = (matrix2.c * f13) + f12 + matrix2.e;
        this.f = (f13 * matrix2.d) + (matrix.e * f11) + matrix2.f;
    }
}
