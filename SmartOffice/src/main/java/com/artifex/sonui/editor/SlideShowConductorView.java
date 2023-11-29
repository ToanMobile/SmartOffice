package com.artifex.sonui.editor;

import android.graphics.ColorMatrix;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;

public interface SlideShowConductorView extends AnimatableView {
    void applyColorAdjustmentMatrix(ColorMatrix colorMatrix);

    void begin();

    void commit();

    void concatColorAdjustmentMatrix(ColorMatrix colorMatrix);

    void dispose();

    Point getSize();

    void render();

    void setOpacity(float f);

    void setPosition(PointF pointF);

    void setRotation(float f);

    void setScale(float f, float f2);

    void setTransform(Matrix matrix);

    void setVisibility(boolean z);

    void setZPosition(int i);

    void setZoomScale(double d);
}
