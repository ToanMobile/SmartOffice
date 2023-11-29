package com.artifex.solib;

import android.util.Log;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SOInputStream extends InputStream {
    public Object mInputStream;
    public SOSecureFS mSecureFs;

    public SOInputStream(String str) {
        SOSecureFS sOSecureFS = ArDkLib.mSecureFS;
        this.mSecureFs = sOSecureFS;
        if (sOSecureFS == null || !sOSecureFS.isSecurePath(str)) {
            try {
                this.mInputStream = new FileInputStream(str);
            } catch (FileNotFoundException e) {
                this.mInputStream = null;
                e.printStackTrace();
            } catch (SecurityException e2) {
                this.mInputStream = null;
                e2.printStackTrace();
            }
        } else {
            this.mInputStream = this.mSecureFs.getFileHandleForReading(str);
        }
    }

    public int available() {
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            try {
                return ((FileInputStream) obj).available();
            } catch (IOException unused) {
                Log.e("SOInputStream", "Available: Error in FileInputStream");
                return 0;
            }
        } else {
            int fileLength = (int) this.mSecureFs.getFileLength(obj);
            if (fileLength != -1) {
                return fileLength;
            }
            Log.e("SOInputStream", "Available: Error in SecureFS");
            return 0;
        }
    }

    public void close() {
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            try {
                ((FileInputStream) obj).close();
            } catch (IOException unused) {
                Log.e("SOInputStream", "Error: closing FileInputStream");
            }
        } else if (!this.mSecureFs.closeFile(obj)) {
            Log.e("SOInputStream", "Error: closing SecureFS");
        }
    }

    public void mark(int i) {
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            ((FileInputStream) obj).mark(i);
        }
    }

    public boolean markSupported() {
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            return ((FileInputStream) obj).markSupported();
        }
        return false;
    }

    public int read() {
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            try {
                return ((FileInputStream) obj).read();
            } catch (IOException unused) {
                Log.e("SOInputStream", "Error: reading byte from FileInputStream");
                return -1;
            }
        } else {
            byte[] bArr = new byte[1];
            int readFromFile = this.mSecureFs.readFromFile(obj, bArr);
            if (readFromFile != -1 && readFromFile != 0) {
                return bArr[0];
            }
            Log.e("SOInputStream", "Error: reading byte from SecureFS");
            return -1;
        }
    }

    public void reset() {
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            try {
                ((FileInputStream) obj).reset();
            } catch (IOException unused) {
                Log.e("SOInputStream", "Error: resetting FileInputStream");
            }
        }
    }

    public long skip(long j) {
        return 0;
    }

    public int read(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            return -1;
        }
        if (i2 == 0) {
            return 0;
        }
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            try {
                return ((FileInputStream) obj).read(bArr, i, i2);
            } catch (IOException unused) {
                Log.e("SOInputStream", "Error: reading offset byte array from FileInputStream");
                return -1;
            }
        } else {
            byte[] bArr2 = new byte[i2];
            int readFromFile = this.mSecureFs.readFromFile(obj, bArr2);
            if (readFromFile == -1 || readFromFile == 0) {
                Log.e("SOInputStream", "Error: reading offset byte array from SecureFS");
                return -1;
            }
            System.arraycopy(bArr2, 0, bArr, i, readFromFile);
            return readFromFile;
        }
    }

    public int read(byte[] bArr) {
        if (bArr == null) {
            return -1;
        }
        if (bArr.length == 0) {
            return 0;
        }
        Object obj = this.mInputStream;
        if (obj instanceof FileInputStream) {
            try {
                return ((FileInputStream) obj).read(bArr);
            } catch (IOException unused) {
                Log.e("SOInputStream", "Error: reading byte array from FileInputStream");
                return -1;
            } catch (NullPointerException unused2) {
                Log.e("SOInputStream", "Error: reading byte array from FileInputStream - NullPointerException");
                return -1;
            }
        } else {
            int readFromFile = this.mSecureFs.readFromFile(obj, bArr);
            if (readFromFile != -1 && readFromFile != 0) {
                return readFromFile;
            }
            Log.e("SOInputStream", "Error: reading byte array from SecureFS");
            return -1;
        }
    }
}
