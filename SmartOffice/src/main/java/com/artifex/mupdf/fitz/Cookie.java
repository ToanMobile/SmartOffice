package com.artifex.mupdf.fitz;

public class Cookie {
    private long pointer = newNative();

    static {
        Context.init();
    }

    private native long newNative();

    public native void abort();

    public void destroy() {
        finalize();
    }

    public native void finalize();
}
