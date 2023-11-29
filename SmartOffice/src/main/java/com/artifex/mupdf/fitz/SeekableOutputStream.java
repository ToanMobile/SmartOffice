package com.artifex.mupdf.fitz;

import java.io.IOException;

public interface SeekableOutputStream extends SeekableStream {
    void truncate() throws IOException;

    void write(byte[] bArr, int i, int i2) throws IOException;
}
