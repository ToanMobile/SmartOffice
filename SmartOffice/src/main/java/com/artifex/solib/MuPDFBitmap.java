package com.artifex.solib;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class MuPDFBitmap extends ArDkBitmap {
    public MuPDFBitmap(int i, int i2) {
        int i3 = ArDkBitmap.serialBase + 1;
        ArDkBitmap.serialBase = i3;
        this.serial = i3;
        this.bitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        this.rect = new Rect(0, 0, i, i2);
    }

    public ArDkBitmap createBitmap(int i, int i2, int i3, int i4) {
        return new MuPDFBitmap(this, i, i2, i3, i4);
    }

    public MuPDFBitmap(ArDkBitmap arDkBitmap, int i, int i2, int i3, int i4) {
        this.serial = arDkBitmap.serial;
        this.bitmap = arDkBitmap.bitmap;
        this.rect = new Rect(i, i2, i3, i4);
    }
}
