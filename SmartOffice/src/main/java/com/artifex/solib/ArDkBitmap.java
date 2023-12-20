package com.artifex.solib;

import android.graphics.Bitmap;
import android.graphics.Rect;
public class ArDkBitmap implements Comparable<ArDkBitmap> {
    protected static int serialBase;
    protected int serial;
    protected Bitmap bitmap;
    protected Rect rect;

    @Override
    public int compareTo(ArDkBitmap obj) {
        Bitmap bitmap2 = this.bitmap;
        int byteCount = bitmap2 == null ? 0 : bitmap2.getByteCount();
        Bitmap bitmap3 = obj.bitmap;
        int byteCount2 = bitmap3 == null ? 0 : bitmap3.getByteCount();
        if (byteCount <= byteCount2) {
            if (byteCount >= byteCount2) {
                int i2 = this.serial;
                int i3 = obj.serial;
                if (i2 <= i3) {
                    if (i2 < i3) {
                        return -1;
                    }
                    return 0;
                }
            }
            return -1;
        }
        return 1;
    }

    public enum Type {
        A8,
        RGB555,
        RGB565,
        RGBA8888
    }

    public ArDkBitmap() {
    }

    /* access modifiers changed from: protected */
    public void allocateBitmap(int i2, int i3, Type type) {
        Bitmap.Config config;
        int i4 = serialBase + 1;
        serialBase = i4;
        this.serial = i4;
        int ordinal = type.ordinal();
        if (ordinal == 0) {
            config = Bitmap.Config.ALPHA_8;
        } else if (ordinal != 3) {
            config = Bitmap.Config.RGB_565;
        } else {
            config = Bitmap.Config.ARGB_8888;
        }
        this.bitmap = Bitmap.createBitmap(i2, i3, config);
        this.rect = new Rect(0, 0, i2, i3);
    }

    public ArDkBitmap createBitmap(int i2, int i3, int i4, int i5) {
        return null;
    }

    public void dispose() {
        if (!this.bitmap.isRecycled()) {
            this.bitmap.recycle();
        }
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public int getHeight() {
        Rect rect2 = this.rect;
        return rect2.bottom - rect2.top;
    }

    public Rect getRect() {
        return this.rect;
    }

    public int getWidth() {
        Rect rect2 = this.rect;
        return rect2.right - rect2.left;
    }

    public ArDkBitmap(Bitmap bitmap2) {
        int i2 = serialBase + 1;
        serialBase = i2;
        this.serial = i2;
        this.bitmap = bitmap2;
        this.rect = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
    }
}
