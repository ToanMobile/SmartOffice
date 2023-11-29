package com.artifex.solib;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;

public class DefaultClipboardHandler implements SOClipboardHandler {
    public Activity mActivity;
    public ClipboardManager mClipboard;

    public boolean clipboardHasPlaintext() {
        return this.mClipboard.hasPrimaryClip();
    }

    public String getPlainTextFromClipoard() {
        return this.mClipboard.hasPrimaryClip() ? this.mClipboard.getPrimaryClip().getItemAt(0).coerceToText(this.mActivity).toString() : "";
    }

    public void initClipboardHandler(Activity activity) {
        this.mActivity = activity;
        this.mClipboard = (ClipboardManager) activity.getSystemService("clipboard");
    }

    public void putPlainTextToClipboard(String str) {
        this.mClipboard.setPrimaryClip(ClipData.newPlainText("text", str));
    }

    public void releaseClipboardHandler() {
        this.mActivity = null;
        this.mClipboard = null;
    }
}
