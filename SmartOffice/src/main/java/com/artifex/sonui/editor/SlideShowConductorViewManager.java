package com.artifex.sonui.editor;

import android.graphics.PointF;
import android.graphics.RectF;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkPage;

public interface SlideShowConductorViewManager {
    void add(SlideShowConductorView slideShowConductorView);

    void animationsCompleted();

    void animationsRunning();

    void animationsStarted();

    void animationsWaiting();

    void destroyLayer(SlideShowConductorView slideShowConductorView);

    SlideShowConductorView newLayer(ArDkDoc arDkDoc, ArDkPage arDkPage, int i, PointF pointF, RectF rectF);

    void remove(SlideShowConductorView slideShowConductorView);
}
