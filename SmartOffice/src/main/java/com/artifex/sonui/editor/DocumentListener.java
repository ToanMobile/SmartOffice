package com.artifex.sonui.editor;

import android.graphics.Rect;

public interface DocumentListener {
    void onDocCompleted();

    void onPageLoaded(int i);

    void onPasswordRequired();

    void onViewChanged(float f, int i, int i2, Rect rect);
}
