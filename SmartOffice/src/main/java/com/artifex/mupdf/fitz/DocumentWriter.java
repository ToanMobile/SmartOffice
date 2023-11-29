package com.artifex.mupdf.fitz;

public class DocumentWriter {
    private long ocrlistener;
    private long pointer;

    public interface OCRListener {
        boolean progress(int i, int i2);
    }

    static {
        Context.init();
    }

    public DocumentWriter(String str, String str2, String str3) {
        this.pointer = newNativeDocumentWriter(str, str2, str3);
    }

    private static native long newNativeDocumentWriter(String str, String str2, String str3);

    private static native long newNativeDocumentWriterWithSeekableOutputStream(SeekableOutputStream seekableOutputStream, String str, String str2);

    public native void addOCRListener(OCRListener oCRListener);

    public native Device beginPage(Rect rect);

    public native void close();

    public void destroy() {
        finalize();
    }

    public native void endPage();

    public native void finalize();

    public DocumentWriter(SeekableOutputStream seekableOutputStream, String str, String str2) {
        this.pointer = newNativeDocumentWriterWithSeekableOutputStream(seekableOutputStream, str, str2);
    }
}
