package com.artifex.mupdf.fitz;

public class Shade {
    private long pointer;

    static {
        Context.init();
    }

    private Shade(long j) {
        this.pointer = j;
    }

    public void destroy() {
        finalize();
    }

    public native void finalize();
}
