package com.artifex.mupdf.fitz;

public class Path implements PathWalker {
    private long pointer;

    static {
        Context.init();
    }

    public Path() {
        this.pointer = newNative();
    }

    private native long cloneNative();

    private native long newNative();

    public native void closePath();

    public native Point currentPoint();

    public native void curveTo(float f, float f2, float f3, float f4, float f5, float f6);

    public void curveTo(Point point, Point point2, Point point3) {
        curveTo(point.x, point.y, point2.x, point2.y, point3.x, point3.y);
    }

    public native void curveToV(float f, float f2, float f3, float f4);

    public void curveToV(Point point, Point point2) {
        curveToV(point.x, point.y, point2.x, point2.y);
    }

    public native void curveToY(float f, float f2, float f3, float f4);

    public void curveToY(Point point, Point point2) {
        curveToY(point.x, point.y, point2.x, point2.y);
    }

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native Rect getBounds(StrokeState strokeState, Matrix matrix);

    public native void lineTo(float f, float f2);

    public void lineTo(Point point) {
        lineTo(point.x, point.y);
    }

    public native void moveTo(float f, float f2);

    public void moveTo(Point point) {
        moveTo(point.x, point.y);
    }

    public native void rect(int i, int i2, int i3, int i4);

    public native void transform(Matrix matrix);

    public native void walk(PathWalker pathWalker);

    private Path(long j) {
        this.pointer = j;
    }

    public Path(Path path) {
        this.pointer = path.cloneNative();
    }
}
