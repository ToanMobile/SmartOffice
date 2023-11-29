package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import com.artifex.solib.ArDkDoc;

public class SlideShowPageView extends DocPageView {
    public SlideShowPageView(Context context, ArDkDoc arDkDoc) {
        super(context, arDkDoc);
    }

    public void drawBackgroundColor(Canvas canvas) {
    }

    public void resize(int i, int i2) {
        PointF zoomToFitRect = this.mPage.zoomToFitRect(i, i2);
        double min = (double) Math.min(zoomToFitRect.x, zoomToFitRect.y);
        this.mZoom = min;
        this.mSize = this.mPage.sizeAtZoom(min);
    }

    public void setupPage(int i, int i2, int i3) {
        changePage(i);
        resize(i2, i3);
    }
}
