package com.artifex.mupdf.fitz;

public final class ColorParams {
    public static final int BP = 32;
    public static final int OP = 64;
    public static final int OPM = 128;

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
        int i = renderingIntent.ordinal();
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
