package com.artifex.mupdf.fitz;

import java.io.OutputStream;

public class BufferOutputStream extends OutputStream {
    public Buffer buffer;
    public int position = 0;
    public int resetPosition;

    public BufferOutputStream(Buffer buffer2) {
        this.buffer = buffer2;
    }

    public void write(byte[] bArr) {
        this.buffer.writeBytes(bArr);
    }

    public void write(byte[] bArr, int i, int i2) {
        this.buffer.writeBytesFrom(bArr, i, i2);
    }

    public void write(int i) {
        this.buffer.writeByte((byte) i);
    }
}
