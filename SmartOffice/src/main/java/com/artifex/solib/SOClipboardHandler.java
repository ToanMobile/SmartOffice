package com.artifex.solib;

import android.app.Activity;

public interface SOClipboardHandler {
    boolean clipboardHasPlaintext();

    String getPlainTextFromClipoard();

    void initClipboardHandler(Activity activity);

    void putPlainTextToClipboard(String str);

    void releaseClipboardHandler();
}
