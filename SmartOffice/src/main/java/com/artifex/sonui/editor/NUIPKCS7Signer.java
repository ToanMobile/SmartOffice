package com.artifex.sonui.editor;

import com.artifex.mupdf.fitz.PKCS7Signer;

public abstract class NUIPKCS7Signer extends PKCS7Signer {

    public interface NUIPKCS7SignerListener {
        void onCancel();

        void onSignatureReady();
    }

    public abstract void doSign(NUIPKCS7SignerListener nUIPKCS7SignerListener);
}
