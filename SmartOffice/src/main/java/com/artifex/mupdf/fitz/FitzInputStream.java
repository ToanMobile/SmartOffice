package com.artifex.mupdf.fitz;

import java.io.IOException;
import java.io.InputStream;

public class FitzInputStream extends InputStream {
    private boolean closed = false;
    private long markpos = -1;
    private long pointer;

    static {
        Context.init();
    }

    private FitzInputStream(long j) {
        this.pointer = j;
    }

    private native int readArray(byte[] bArr, int i, int i2);

    private native int readByte();

    public native int available();

    public native void close() throws IOException;

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native void mark(int i);

    public native boolean markSupported();

    public int read() {
        return readByte();
    }

    public native void reset() throws IOException;

    public int read(byte[] bArr, int i, int i2) {
        return readArray(bArr, i, i2);
    }

    public int read(byte[] bArr) {
        return readArray(bArr, 0, bArr.length);
    }
}
