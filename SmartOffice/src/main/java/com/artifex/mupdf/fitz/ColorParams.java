package com.artifex.mupdf.fitz;

public final class ColorParams {
    public static final int BP = 32;
    public static final int OP = 64;
    public static final int OPM = 128;

    /* renamed from: com.artifex.mupdf.fitz.ColorParams$1  reason: invalid class name */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$artifex$mupdf$fitz$ColorParams$RenderingIntent;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.artifex.mupdf.fitz.ColorParams$RenderingIntent[] r0 = com.artifex.mupdf.fitz.ColorParams.RenderingIntent.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$artifex$mupdf$fitz$ColorParams$RenderingIntent = r0
                com.artifex.mupdf.fitz.ColorParams$RenderingIntent r1 = com.artifex.mupdf.fitz.ColorParams.RenderingIntent.PERCEPTUAL     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$artifex$mupdf$fitz$ColorParams$RenderingIntent     // Catch:{ NoSuchFieldError -> 0x001d }
                com.artifex.mupdf.fitz.ColorParams$RenderingIntent r1 = com.artifex.mupdf.fitz.ColorParams.RenderingIntent.RELATIVE_COLORIMETRIC     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = $SwitchMap$com$artifex$mupdf$fitz$ColorParams$RenderingIntent     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.artifex.mupdf.fitz.ColorParams$RenderingIntent r1 = com.artifex.mupdf.fitz.ColorParams.RenderingIntent.SATURATION     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = $SwitchMap$com$artifex$mupdf$fitz$ColorParams$RenderingIntent     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.artifex.mupdf.fitz.ColorParams$RenderingIntent r1 = com.artifex.mupdf.fitz.ColorParams.RenderingIntent.ABSOLUTE_COLORIMETRIC     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.artifex.mupdf.fitz.ColorParams.AnonymousClass1.<clinit>():void");
        }
    }

    public enum RenderingIntent {
        PERCEPTUAL,
        RELATIVE_COLORIMETRIC,
        SATURATION,
        ABSOLUTE_COLORIMETRIC
    }

    public static boolean BP(int i) {
        return (i & 32) != 0;
    }

    public static boolean OP(int i) {
        return (i & 64) != 0;
    }

    public static boolean OPM(int i) {
        return (i & 128) != 0;
    }

    public static RenderingIntent RI(int i) {
        int i2 = i & 3;
        if (i2 == 1) {
            return RenderingIntent.RELATIVE_COLORIMETRIC;
        }
        if (i2 == 2) {
            return RenderingIntent.SATURATION;
        }
        if (i2 != 3) {
            return RenderingIntent.PERCEPTUAL;
        }
        return RenderingIntent.ABSOLUTE_COLORIMETRIC;
    }

    public static int pack(RenderingIntent renderingIntent, boolean z, boolean z2, boolean z3) {
        int i = AnonymousClass1.$SwitchMap$com$artifex$mupdf$fitz$ColorParams$RenderingIntent[renderingIntent.ordinal()];
        int i2 = 3;
        if (i == 2) {
            i2 = 1;
        } else if (i == 3) {
            i2 = 2;
        } else if (i != 4) {
            i2 = 0;
        }
        if (z) {
            i2 |= 32;
        }
        if (z2) {
            i2 |= 64;
        }
        return z3 ? i2 | 128 : i2;
    }
}
