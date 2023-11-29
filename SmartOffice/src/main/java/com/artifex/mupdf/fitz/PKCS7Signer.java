package com.artifex.mupdf.fitz;

public abstract class PKCS7Signer {
    private long pointer = newNative(this);

    static {
        Context.init();
    }

    private native long newNative(PKCS7Signer pKCS7Signer);

    public void destroy() {
        finalize();
    }

    public native void finalize();

    public abstract int maxDigest();

    public abstract PKCS7DistinguishedName name();

    public abstract byte[] sign(FitzInputStream fitzInputStream);
}
