package com.artifex.sonui.editor;

public interface SlideAnimationsListener {
    void animating(int i);

    void animationsCompleted(int i);

    void animationsStarted(int i);

    void animationsWaiting(int i);
}
