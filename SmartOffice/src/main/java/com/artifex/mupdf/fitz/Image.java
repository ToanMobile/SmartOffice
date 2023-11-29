package com.artifex.mupdf.fitz;

public class Image {
    public long pointer;

    static {
        Context.init();
    }

    public Image(long j) {
        this.pointer = j;
    }

    private native long newNativeFromBytes(byte[] bArr);

    private native long newNativeFromFile(String str);

    private native long newNativeFromPixmap(Pixmap pixmap);

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native int getBitsPerComponent();

    public native ColorSpace getColorSpace();

    public native int getHeight();

    public native boolean getImageMask();

    public native boolean getInterpolate();

    public native Image getMask();

    public native int getNumberOfComponents();

    public native int getWidth();

    public native int getXResolution();

    public native int getYResolution();

    public native Pixmap toPixmap();

    public Image(Pixmap pixmap) {
        this.pointer = newNativeFromPixmap(pixmap);
    }

    public Image(String str) {
        this.pointer = newNativeFromFile(str);
    }

    public Image(byte[] bArr) {
        this.pointer = newNativeFromBytes(bArr);
    }
}
