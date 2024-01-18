package com.artifex.mupdf.fitz;

import java.util.Objects;

public class Point {
    public float x;
    public float y;

    public Point(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        Point point = (Point) obj;
        if (this.x == point.x && this.y == point.y) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Float.valueOf(this.x), Float.valueOf(this.y)});
    }

    public String toString() {
        StringBuilder m = new StringBuilder("[");
        m.append(this.x);
        m.append(" ");
        m.append(this.y);
        m.append("]");
        return m.toString();
    }

    public Point transform(Matrix matrix) {
        float f = this.x;
        float f2 = this.y;
        this.x = (matrix.c * f2) + (matrix.f2131a * f) + matrix.e;
        this.y = (f2 * matrix.d) + (f * matrix.b) + matrix.f;
        return this;
    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }
}
