package com.artifex.sonui.editor;

import android.app.Activity;
import com.artifex.sonui.editor.Utilities;

public class NUIDefaultSignerFactory implements Utilities.SigningFactoryListener {
    public static NUIDefaultSignerFactory mInstance;

    public static NUIDefaultSignerFactory getInstance() {
        if (mInstance == null) {
            synchronized (NUIDefaultSignerFactory.class) {
                mInstance = new NUIDefaultSignerFactory();
            }
        }
        return mInstance;
    }

    public NUIPKCS7Signer getSigner(Activity activity) {
        return new NUIDefaultSigner(activity);
    }

    public NUIPKCS7Verifier getVerifier(Activity activity) {
        return new NUIDefaultVerifier(activity, NUICertificateViewer.class);
    }
}
