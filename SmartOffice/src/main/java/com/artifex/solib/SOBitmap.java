package com.artifex.solib;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.artifex.solib.ArDkBitmap;

public class SOBitmap extends ArDkBitmap {
    public SOBitmap(int i, int i2) {
        allocateBitmap(i, i2, Type.RGBA8888);
    }

    private native void nativeCopyPixels565(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Bitmap bitmap, Bitmap bitmap2);

    private native void nativeCopyPixels888(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Bitmap bitmap, Bitmap bitmap2);

    private native void nativeMergePixels565(Bitmap bitmap, int i, int i2, int i3, int i4, Bitmap bitmap2, Bitmap bitmap3, int i5, int i6, int i7, int i8);

    private native void nativeMergePixels888(Bitmap bitmap, int i, int i2, int i3, int i4, Bitmap bitmap2, Bitmap bitmap3, int i5, int i6, int i7, int i8);

    public void alphaCombine(ArDkBitmap arDkBitmap, ArDkBitmap arDkBitmap2) {
        ArDkBitmap arDkBitmap3 = arDkBitmap;
        ArDkBitmap arDkBitmap4 = arDkBitmap2;
        if (arDkBitmap3 == null || arDkBitmap4 == null) {
            throw null;
        } else if (!this.rect.equals(arDkBitmap4.rect)) {
            throw new IllegalArgumentException("alphaCombine's source bitmaps must have identical dimensions");
        } else if (this.bitmap.getConfig() != Bitmap.Config.RGB_565 && this.bitmap.getConfig() != Bitmap.Config.ARGB_8888) {
            throw new IllegalArgumentException("alphaCombine's source bitmap must be RGB_565 or ARGB_8888 format");
        } else if (arDkBitmap4.bitmap.getConfig() != Bitmap.Config.ALPHA_8) {
            throw new IllegalArgumentException("alphaCombine's source alpha mask must be ALPHA_8 format");
        } else if (arDkBitmap3.bitmap.getConfig() == Bitmap.Config.ARGB_8888) {
            Rect rect = arDkBitmap3.rect;
            if (this.bitmap.getConfig() == Bitmap.Config.RGB_565) {
                Bitmap bitmap = arDkBitmap3.bitmap;
                int i = rect.left;
                int i2 = rect.top;
                int width = rect.width();
                int height = rect.height();
                Bitmap bitmap2 = this.bitmap;
                Bitmap bitmap3 = arDkBitmap4.bitmap;
                Rect rect2 = this.rect;
                nativeMergePixels565(bitmap, i, i2, width, height, bitmap2, bitmap3, rect2.left, rect2.top, rect2.width(), this.rect.height());
                return;
            }
            Bitmap bitmap4 = arDkBitmap3.bitmap;
            int i3 = rect.left;
            int i4 = rect.top;
            int width2 = rect.width();
            int height2 = rect.height();
            Bitmap bitmap5 = this.bitmap;
            Bitmap bitmap6 = arDkBitmap4.bitmap;
            Rect rect3 = this.rect;
            nativeMergePixels888(bitmap4, i3, i4, width2, height2, bitmap5, bitmap6, rect3.left, rect3.top, rect3.width(), this.rect.height());
        } else {
            throw new IllegalArgumentException("alphaCombine's destination bitmap must be ARGB_8888 format");
        }
    }

    public ArDkBitmap createBitmap(int i, int i2, int i3, int i4) {
        return new SOBitmap(this, i, i2, i3, i4);
    }

    public native void invertLuminance();

    public SOBitmap(ArDkBitmap arDkBitmap, int i, int i2, int i3, int i4) {
        this.serial = arDkBitmap.serial;
        this.bitmap = arDkBitmap.bitmap;
        this.rect = new Rect(i, i2, i3, i4);
    }

    public SOBitmap(int i, int i2, Type type) {
        allocateBitmap(i, i2, type);
    }
}
