package com.artifex.mupdf.fitz;

import java.io.IOException;
import java.io.InputStream;

public class BufferInputStream extends InputStream {
    public Buffer buffer;
    public int position = 0;
    public int resetPosition = -1;

    public BufferInputStream(Buffer buffer2) {
        this.buffer = buffer2;
    }

    public int available() {
        return this.buffer.getLength();
    }

    public void mark(int i) {
        this.resetPosition = this.position;
    }

    public boolean markSupported() {
        return true;
    }

    public int read() {
        int readByte = this.buffer.readByte(this.position);
        if (readByte >= 0) {
            this.position++;
        }
        return readByte;
    }

    public void reset() throws IOException {
        int i = this.resetPosition;
        if (i < 0) {
            throw new IOException("cannot reset because mark never set");
        } else if (i < this.buffer.getLength()) {
            this.position = this.resetPosition;
        } else {
            throw new IOException("cannot reset because mark set outside of buffer");
        }
    }

    public int read(byte[] bArr) {
        int readBytes = this.buffer.readBytes(this.position, bArr);
        if (readBytes >= 0) {
            this.position += readBytes;
        }
        return readBytes;
    }

    public int read(byte[] bArr, int i, int i2) {
        int readBytesInto = this.buffer.readBytesInto(this.position, bArr, i, i2);
        if (readBytesInto >= 0) {
            this.position += readBytesInto;
        }
        return readBytesInto;
    }
}
