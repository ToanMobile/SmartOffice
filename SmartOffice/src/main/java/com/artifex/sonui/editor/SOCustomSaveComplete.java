package com.artifex.sonui.editor;

public interface SOCustomSaveComplete {
    public static final int SOCustomComplete_Cancelled = 2;
    public static final int SOCustomSaveComplete_Error = 1;
    public static final int SOCustomSaveComplete_Succeeded = 0;

    void onComplete(int i, String str, boolean z);
}
