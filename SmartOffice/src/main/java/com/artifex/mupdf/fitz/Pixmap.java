package com.artifex.mupdf.fitz;

import com.artifex.source.util.a.util_a.a.a.c$$ExternalSyntheticOutline0;

public class Pixmap {
    private long pointer;

    static {
        Context.init();
    }

    private Pixmap(long j) {
        this.pointer = j;
    }

    private native void clearWithValue(int i);

    private native long newNative(ColorSpace colorSpace, int i, int i2, int i3, int i4, boolean z);

    public native void clear();

    public void clear(int i) {
        clearWithValue(i);
    }

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native void gamma(float f);

    public native boolean getAlpha();

    public Rect getBounds() {
        int x = getX();
        int y = getY();
        return new Rect((float) x, (float) y, (float) (getWidth() + x), (float) (getHeight() + y));
    }

    public native ColorSpace getColorSpace();

    public native int getHeight();

    public native int getNumberOfComponents();

    public native int[] getPixels();

    public native byte getSample(int i, int i2, int i3);

    public native byte[] getSamples();

    public native int getStride();

    public native int getWidth();

    public native int getX();

    public native int getXResolution();

    public native int getY();

    public native int getYResolution();

    public native void invert();

    public native void invertLuminance();

    public native void saveAsPNG(String str);

    public native void setResolution(int i, int i2);

    public native void tint(int i, int i2);

    public String toString() {
        StringBuilder m = c$$ExternalSyntheticOutline0.m("Pixmap(w=");
        m.append(getWidth());
        m.append(" h=");
        m.append(getHeight());
        m.append(" x=");
        m.append(getX());
        m.append(" y=");
        m.append(getY());
        m.append(" n=");
        m.append(getNumberOfComponents());
        m.append(" alpha=");
        m.append(getAlpha());
        m.append(" cs=");
        m.append(getColorSpace());
        m.append(")");
        return m.toString();
    }

    public Pixmap(ColorSpace colorSpace, int i, int i2, int i3, int i4, boolean z) {
        this.pointer = newNative(colorSpace, i, i2, i3, i4, z);
    }

    public Pixmap(ColorSpace colorSpace, int i, int i2, int i3, int i4) {
        this(colorSpace, i, i2, i3, i4, false);
    }

    public Pixmap(ColorSpace colorSpace, int i, int i2, boolean z) {
        this(colorSpace, 0, 0, i, i2, z);
    }

    public Pixmap(ColorSpace colorSpace, int i, int i2) {
        this(colorSpace, 0, 0, i, i2, false);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Pixmap(ColorSpace r9, Rect r10, boolean r11) {
        /*
            r8 = this;
            float r0 = r10.x0
            int r3 = (int) r0
            float r1 = r10.y0
            int r4 = (int) r1
            float r2 = r10.x1
            float r2 = r2 - r0
            int r5 = (int) r2
            float r10 = r10.y1
            float r10 = r10 - r1
            int r6 = (int) r10
            r1 = r8
            r2 = r9
            r7 = r11
            r1.<init>(r2, r3, r4, r5, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.mupdf.fitz.Pixmap.<init>(com.artifex.mupdf.fitz.ColorSpace, com.artifex.mupdf.fitz.Rect, boolean):void");
    }

    public Pixmap(ColorSpace colorSpace, Rect rect) {
        this(colorSpace, rect, false);
    }
}
