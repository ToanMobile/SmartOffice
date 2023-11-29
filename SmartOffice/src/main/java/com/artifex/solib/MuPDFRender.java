package com.artifex.solib;

import com.artifex.mupdf.fitz.Cookie;

public class MuPDFRender extends ArDkRender {
    public volatile Cookie mCookie;

    public void abort() {
        try {
            if (this.mCookie != null) {
                this.mCookie.abort();
            }
        } catch (Exception unused) {
        }
    }

    public void destroy() {
        this.mCookie = null;
    }
}
