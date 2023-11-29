package com.artifex.sonui.editor;

public interface SlideShowViewListener {
    void slideAnimating(int i);

    void slideAnimationsCompleted(int i);

    void slideAnimationsStarted(int i);

    void slideAnimationsWaiting(int i);

    void slideEnded(int i);

    void slideShowCompleted();

    void slideStarted(int i);
}
