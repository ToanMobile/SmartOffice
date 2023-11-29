package com.artifex.mupdf.fitz;

public class NativeDevice extends Device {
    private long nativeInfo;
    private Object nativeResource;

    static {
        Context.init();
    }

    public NativeDevice(long j) {
        super(j);
    }

    public final native void beginGroup(Rect rect, ColorSpace colorSpace, boolean z, boolean z2, int i, float f);

    public final native void beginLayer(String str);

    public final native void beginMask(Rect rect, boolean z, ColorSpace colorSpace, float[] fArr, int i);

    public final native int beginTile(Rect rect, Rect rect2, float f, float f2, Matrix matrix, int i);

    public final native void clipImageMask(Image image, Matrix matrix);

    public final native void clipPath(Path path, boolean z, Matrix matrix);

    public final native void clipStrokePath(Path path, StrokeState strokeState, Matrix matrix);

    public final native void clipStrokeText(Text text, StrokeState strokeState, Matrix matrix);

    public final native void clipText(Text text, Matrix matrix);

    public final native void close();

    public void destroy() {
        finalize();
    }

    public final native void endGroup();

    public final native void endLayer();

    public final native void endMask();

    public final native void endTile();

    public final native void fillImage(Image image, Matrix matrix, float f, int i);

    public final native void fillImageMask(Image image, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public final native void fillPath(Path path, boolean z, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public final native void fillShade(Shade shade, Matrix matrix, float f, int i);

    public final native void fillText(Text text, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public native void finalize();

    public final native void ignoreText(Text text, Matrix matrix);

    public final native void popClip();

    public final native void strokePath(Path path, StrokeState strokeState, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);

    public final native void strokeText(Text text, StrokeState strokeState, Matrix matrix, ColorSpace colorSpace, float[] fArr, float f, int i);
}
