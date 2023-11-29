package com.artifex.mupdf.fitz;

public abstract class Device {
    public static final int BLEND_COLOR = 14;
    public static final int BLEND_COLOR_BURN = 7;
    public static final int BLEND_COLOR_DODGE = 6;
    public static final int BLEND_DARKEN = 4;
    public static final int BLEND_DIFFERENCE = 10;
    public static final int BLEND_EXCLUSION = 11;
    public static final int BLEND_HARD_LIGHT = 8;
    public static final int BLEND_HUE = 12;
    public static final int BLEND_LIGHTEN = 5;
    public static final int BLEND_LUMINOSITY = 15;
    public static final int BLEND_MULTIPLY = 1;
    public static final int BLEND_NORMAL = 0;
    public static final int BLEND_OVERLAY = 3;
    public static final int BLEND_SATURATION = 13;
    public static final int BLEND_SCREEN = 2;
    public static final int BLEND_SOFT_LIGHT = 9;
    public long pointer;

    static {
        Context.init();
    }

    public Device() {
        this.pointer = newNative();
    }

    private native long newNative();

    public abstract void beginGroup(Rect rect, ColorSpace colorSpace, boolean z, boolean z2, int i, float f);

    public abstract void beginLayer(String str);

    public abstract void beginMask(Rect rect, boolean z, ColorSpace colorSpace, float[] fArr, int i);

    public abstract int beginTile(Rect rect, Rect rect2, float f, float f2, Matrix matrix, int i);

    public abstract void clipImageMask(Image image, Matrix matrix);

    public abstract void clipPath(Path path, boolean z, Matrix matrix);

    public abstract void clipStrokePath(Path path, StrokeState strokeState, Matrix matrix);

    public abstract void clipStrokeText(Text text, StrokeState strokeState, Matrix matrix);

    public abstract void clipText(Text text, Matrix matrix);

    public abstract void close();

    public void destroy() {
        finalize();
    }

    public abstract void endGroup();

    public abstract void endLayer();

    public abstract void endMask();

    public abstract void endTile();

    public abstract void fillImage(Image image, Matrix matrix, float f, int i);

    public abstract void fillImageMask(Image image, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public abstract void fillPath(Path path, boolean z, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public abstract void fillShade(Shade shade, Matrix matrix, float f, int i);

    public abstract void fillText(Text text, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public native void finalize();

    public abstract void ignoreText(Text text, Matrix matrix);

    public abstract void popClip();

    public abstract void strokePath(Path path, StrokeState strokeState, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public abstract void strokeText(Text text, StrokeState strokeState, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public Device(long j) {
        this.pointer = j;
    }
}
