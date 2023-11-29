package com.artifex.mupdf.fitz;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileStream implements SeekableInputStream, SeekableOutputStream {
    public RandomAccessFile file;

    public FileStream(String str, String str2) throws IOException {
        this.file = new RandomAccessFile(str, str2);
    }

    public void close() throws IOException {
        this.file.close();
    }

    public long position() throws IOException {
        return this.file.getFilePointer();
    }

    public int read(byte[] bArr) throws IOException {
        return this.file.read(bArr);
    }

    public long seek(long j, int i) throws IOException {
        if (i == 0) {
            this.file.seek(j);
        } else if (i == 1) {
            RandomAccessFile randomAccessFile = this.file;
            randomAccessFile.seek(randomAccessFile.getFilePointer() + j);
        } else if (i == 2) {
            RandomAccessFile randomAccessFile2 = this.file;
            randomAccessFile2.seek(randomAccessFile2.length() + j);
        }
        return this.file.getFilePointer();
    }

    public void truncate() throws IOException {
        RandomAccessFile randomAccessFile = this.file;
        randomAccessFile.setLength(randomAccessFile.getFilePointer());
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.file.write(bArr, i, i2);
    }

    public FileStream(File file2, String str) throws IOException {
        this.file = new RandomAccessFile(file2, str);
    }
}
