package com.artifex.mupdf.fitz;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Buffer {
    private long pointer;

    static {
        Context.init();
    }

    public Buffer(int i) {
        this.pointer = newNativeBuffer(i);
    }

    private native long newNativeBuffer(int i);

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public native int getLength();

    public native int readByte(int i);

    public native int readBytes(int i, byte[] bArr);

    public native int readBytesInto(int i, byte[] bArr, int i2, int i3);

    public void readIntoStream(OutputStream outputStream) {
        try {
            byte[] bArr = new byte[getLength()];
            readBytes(0, bArr);
            outputStream.write(bArr);
        } catch (IOException unused) {
            throw new RuntimeException("unable to write all bytes from buffer into stream");
        }
    }

    public native void save(String str);

    public native void writeBuffer(Buffer buffer);

    public native void writeByte(byte b);

    public native void writeBytes(byte[] bArr);

    public native void writeBytesFrom(byte[] bArr, int i, int i2);

    public void writeFromStream(InputStream inputStream) {
        byte[] bArr = null;
        boolean z = false;
        while (!z) {
            try {
                int available = inputStream.available();
                if (bArr == null || available > bArr.length) {
                    bArr = new byte[available];
                }
                int read = inputStream.read(bArr);
                if (read >= 0) {
                    writeBytesFrom(bArr, 0, read);
                } else {
                    z = true;
                }
            } catch (IOException unused) {
                throw new RuntimeException("unable to read all bytes from stream into buffer");
            }
        }
    }

    public native void writeLine(String str);

    public native void writeLines(String... strArr);

    public native void writeRune(int i);

    public Buffer() {
        this.pointer = newNativeBuffer(0);
    }

    public Buffer(long j) {
        this.pointer = j;
    }
}
