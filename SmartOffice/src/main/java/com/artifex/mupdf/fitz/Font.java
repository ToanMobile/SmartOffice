package com.artifex.mupdf.fitz;

import a.a.a.a.a.c$$ExternalSyntheticOutline0;

public class Font {
    public static final int ADOBE_CNS = 0;
    public static final int ADOBE_GB = 1;
    public static final int ADOBE_JAPAN = 2;
    public static final int ADOBE_KOREA = 3;
    public static final int SIMPLE_ENCODING_CYRILLIC = 2;
    public static final int SIMPLE_ENCODING_GREEK = 1;
    public static final int SIMPLE_ENCODING_LATIN = 0;
    private long pointer;

    static {
        Context.init();
    }

    private Font(long j) {
        this.pointer = j;
    }

    private native long newNative(String str, int i);

    public float advanceGlyph(int i) {
        return advanceGlyph(i, false);
    }

    public native float advanceGlyph(int i, boolean z);

    public void destroy() {
        finalize();
    }

    public native int encodeCharacter(int i);

    public native void finalize();

    public native String getName();

    public String toString() {
        StringBuilder m = c$$ExternalSyntheticOutline0.m("Font(");
        m.append(getName());
        m.append(")");
        return m.toString();
    }

    public Font(String str, int i) {
        this.pointer = newNative(str, i);
    }

    public Font(String str) {
        this.pointer = newNative(str, 0);
    }
}
