package com.artifex.mupdf.fitz;

public final class DisplayListDevice extends NativeDevice {
    static {
        Context.init();
    }

    public DisplayListDevice(DisplayList displayList) {
        super(newNative(displayList));
    }

    private static native long newNative(DisplayList displayList);
}
