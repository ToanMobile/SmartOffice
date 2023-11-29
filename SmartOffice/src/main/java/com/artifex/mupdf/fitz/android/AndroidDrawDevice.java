package com.artifex.mupdf.fitz.android;

import android.graphics.Bitmap;
import com.artifex.mupdf.fitz.Context;
import com.artifex.mupdf.fitz.NativeDevice;

public final class AndroidDrawDevice extends NativeDevice {
    static {
        Context.init();
    }

    public AndroidDrawDevice(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6) {
        super(0);
        this.pointer = newNative(bitmap, i, i2, i3, i4, i5, i6);
    }

    private native long newNative(Bitmap bitmap, int i, int i2, int i3, int i4, int i5, int i6);

    public final native void invertLuminance();

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public AndroidDrawDevice(Bitmap bitmap) {
        super(0);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        this.pointer = newNative(bitmap, 0, 0, 0, 0, width, height);
    }
}
