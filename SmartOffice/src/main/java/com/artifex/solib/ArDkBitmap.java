package com.artifex.solib;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class ArDkBitmap implements Comparable<ArDkBitmap> {
    public static int serialBase;
    public Bitmap bitmap;
    public Rect rect;
    public int serial;

    /* renamed from: com.artifex.solib.ArDkBitmap$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$artifex$solib$ArDkBitmap$Type;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.artifex.solib.ArDkBitmap$Type[] r0 = com.artifex.solib.ArDkBitmap.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$artifex$solib$ArDkBitmap$Type = r0
                com.artifex.solib.ArDkBitmap$Type r1 = com.artifex.solib.ArDkBitmap.Type.RGB555     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$artifex$solib$ArDkBitmap$Type     // Catch:{ NoSuchFieldError -> 0x001d }
                com.artifex.solib.ArDkBitmap$Type r1 = com.artifex.solib.ArDkBitmap.Type.RGB565     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$artifex$solib$ArDkBitmap$Type     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.artifex.solib.ArDkBitmap$Type r1 = com.artifex.solib.ArDkBitmap.Type.A8     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$artifex$solib$ArDkBitmap$Type     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.artifex.solib.ArDkBitmap$Type r1 = com.artifex.solib.ArDkBitmap.Type.RGBA8888     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.artifex.solib.ArDkBitmap.AnonymousClass1.<clinit>():void");
        }
    }

    public enum Type {
        A8,
        RGB555,
        RGB565,
        RGBA8888
    }

    public ArDkBitmap() {
    }

    public void allocateBitmap(int i, int i2, Type type) {
        Bitmap.Config config;
        int i3 = serialBase + 1;
        serialBase = i3;
        this.serial = i3;
        int i4 = AnonymousClass1.$SwitchMap$com$artifex$solib$ArDkBitmap$Type[type.ordinal()];
        if (i4 == 3) {
            config = Bitmap.Config.ALPHA_8;
        } else if (i4 != 4) {
            config = Bitmap.Config.RGB_565;
        } else {
            config = Bitmap.Config.ARGB_8888;
        }
        this.bitmap = Bitmap.createBitmap(i, i2, config);
        this.rect = new Rect(0, 0, i, i2);
    }

    public int compareTo(Object obj) {
        ArDkBitmap arDkBitmap = (ArDkBitmap) obj;
        Bitmap bitmap2 = this.bitmap;
        int byteCount = bitmap2 == null ? 0 : bitmap2.getByteCount();
        Bitmap bitmap3 = arDkBitmap.bitmap;
        int byteCount2 = bitmap3 == null ? 0 : bitmap3.getByteCount();
        if (byteCount <= byteCount2) {
            if (byteCount >= byteCount2) {
                int i = this.serial;
                int i2 = arDkBitmap.serial;
                if (i <= i2) {
                    if (i < i2) {
                        return -1;
                    }
                    return 0;
                }
            }
            return -1;
        }
        return 1;
    }

    public ArDkBitmap createBitmap(int i, int i2, int i3, int i4) {
        return null;
    }

    public void dispose() {
        if (!this.bitmap.isRecycled()) {
            this.bitmap.recycle();
        }
    }

    public int getHeight() {
        Rect rect2 = this.rect;
        return rect2.bottom - rect2.top;
    }

    public int getWidth() {
        Rect rect2 = this.rect;
        return rect2.right - rect2.left;
    }

    public ArDkBitmap(Bitmap bitmap2) {
        int i = serialBase + 1;
        serialBase = i;
        this.serial = i;
        this.bitmap = bitmap2;
        this.rect = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
    }
}
