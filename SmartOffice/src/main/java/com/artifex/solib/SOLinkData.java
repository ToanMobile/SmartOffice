package com.artifex.solib;

import android.graphics.RectF;

public class SOLinkData {
    public RectF box;
    public int page;

    public SOLinkData(int i, RectF rectF) {
        this.page = i;
        this.box = rectF;
    }
}
