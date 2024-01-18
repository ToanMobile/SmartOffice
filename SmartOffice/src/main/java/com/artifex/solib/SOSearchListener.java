package com.artifex.solib;

import android.graphics.RectF;

public interface SOSearchListener {

    void found(int i, RectF rectF);

    void notFound();
}
