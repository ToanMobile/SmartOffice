package com.artifex.mupdf.fitz;

public class PDFGraftMap {
    private long pointer;

    static {
        Context.init();
    }

    private PDFGraftMap(long j) {
        this.pointer = j;
    }

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native PDFObject graftObject(PDFObject pDFObject);

    public native void graftPage(int i, PDFDocument pDFDocument, int i2);
}
