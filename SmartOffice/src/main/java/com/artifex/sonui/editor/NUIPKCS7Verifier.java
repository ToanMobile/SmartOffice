package com.artifex.sonui.editor;

import com.artifex.mupdf.fitz.PKCS7Verifier;
import java.util.HashMap;
import java.util.Map;

public abstract class NUIPKCS7Verifier extends PKCS7Verifier {
    public int mSignatureValidity = 0;

    public interface NUIPKCS7VerifierListener {
        void onInitComplete();

        void onVerifyResult(Map<String, String> map, int i);
    }

    public abstract void certificateUpdated();

    public abstract void doVerify(NUIPKCS7VerifierListener nUIPKCS7VerifierListener, int i);

    public abstract void presentResults(HashMap<String, String> hashMap, int i);
}
