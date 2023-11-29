package com.artifex.solib;

import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import kotlin.KotlinVersion;

public class SOOutputStream extends OutputStream {
    public Object mOutputStream;
    public SOSecureFS mSecureFs;

    public SOOutputStream(String str) {
        SOSecureFS sOSecureFS = ArDkLib.mSecureFS;
        this.mSecureFs = sOSecureFS;
        if (sOSecureFS == null || !sOSecureFS.isSecurePath(str)) {
            try {
                this.mOutputStream = new FileOutputStream(str);
            } catch (FileNotFoundException e) {
                this.mOutputStream = null;
                e.printStackTrace();
            } catch (SecurityException e2) {
                this.mOutputStream = null;
                e2.printStackTrace();
            }
        } else {
            this.mOutputStream = this.mSecureFs.getFileHandleForWriting(str);
        }
    }

    public void close() {
        Object obj = this.mOutputStream;
        if (obj == null || !(obj instanceof FileOutputStream)) {
            SOSecureFS sOSecureFS = this.mSecureFs;
            if (sOSecureFS != null && obj != null) {
                sOSecureFS.closeFile(obj);
                return;
            }
            return;
        }
        try {
            ((FileOutputStream) obj).close();
        } catch (IOException unused) {
            Log.e("SOOutputStream", "Error: closing FileOutputStream");
        }
    }

    public void flush() {
        Object obj = this.mOutputStream;
        if (obj instanceof FileOutputStream) {
            try {
                ((FileOutputStream) obj).flush();
            } catch (IOException unused) {
                Log.e("SOOutputStream", "Error: flushing FileOutputStream");
            }
        } else if (!this.mSecureFs.syncFile(obj)) {
            Log.e("SOOutputStream", "Error: flushing SecureFS");
        }
    }

    public void write(byte[] bArr) {
        Object obj = this.mOutputStream;
        if (obj instanceof FileOutputStream) {
            try {
                ((FileOutputStream) obj).write(bArr);
            } catch (IOException unused) {
                Log.e("SOOutputStream", "Error: writing byte array to FileOutputStream");
            }
        } else if (this.mSecureFs.writeToFile(obj, bArr) == -1) {
            Log.e("SOOutputStream", "Error: writing byte array to SecureFS");
        }
    }

    public void write(byte[] bArr, int i, int i2) {
        Object obj = this.mOutputStream;
        if (obj instanceof FileOutputStream) {
            try {
                ((FileOutputStream) obj).write(bArr, i, i2);
            } catch (IOException unused) {
                Log.e("SOOutputStream", "Error: writing offset byte array to FileOutputStream");
            }
        } else {
            if (this.mSecureFs.writeToFile(this.mOutputStream, Arrays.copyOfRange(bArr, i, i2 + i)) == -1) {
                Log.e("SOOutputStream", "Error: writing offset byte array to SecureFS");
            }
        }
    }

    public void write(int i) {
        Object obj = this.mOutputStream;
        if (obj instanceof FileOutputStream) {
            try {
                ((FileOutputStream) obj).write(i);
            } catch (IOException unused) {
                Log.e("SOOutputStream", "Error: writing byte to FileOutputStream");
            }
        } else {
            if (this.mSecureFs.writeToFile(obj, new byte[]{(byte) (i & KotlinVersion.MAX_COMPONENT_VALUE)}) == -1) {
                Log.e("SOOutputStream", "Error: writing byte to SecureFS");
            }
        }
    }
}
