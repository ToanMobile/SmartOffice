package com.artifex.solib;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class MuPDFSelectionLimits extends ArDkSelectionLimits {
    public Rect mBox;

    public MuPDFSelectionLimits(Rect rect) {
        this.mBox = new Rect(rect);
    }

    public void combine(ArDkSelectionLimits arDkSelectionLimits) {
        MuPDFSelectionLimits muPDFSelectionLimits = (MuPDFSelectionLimits) arDkSelectionLimits;
        Rect rect = this.mBox;
        rect.left = Math.min(rect.left, muPDFSelectionLimits.mBox.left);
        Rect rect2 = this.mBox;
        rect2.right = Math.max(rect2.right, muPDFSelectionLimits.mBox.right);
        Rect rect3 = this.mBox;
        rect3.top = Math.min(rect3.top, muPDFSelectionLimits.mBox.top);
        Rect rect4 = this.mBox;
        rect4.bottom = Math.max(rect4.bottom, muPDFSelectionLimits.mBox.bottom);
    }

    public RectF getBox() {
        Rect rect = this.mBox;
        return new RectF((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.bottom);
    }

    public PointF getEnd() {
        Rect rect = this.mBox;
        return new PointF((float) rect.right, (float) rect.bottom);
    }

    public boolean getHasSelectionEnd() {
        return true;
    }

    public boolean getHasSelectionStart() {
        return true;
    }

    public boolean getIsActive() {
        return true;
    }

    public boolean getIsCaret() {
        return false;
    }

    public PointF getStart() {
        Rect rect = this.mBox;
        return new PointF((float) rect.left, (float) rect.top);
    }

    public MuPDFSelectionLimits(PointF pointF, PointF pointF2) {
        this.mBox = new Rect((int) pointF.x, (int) pointF.y, (int) pointF2.x, (int) pointF2.y);
    }
}
