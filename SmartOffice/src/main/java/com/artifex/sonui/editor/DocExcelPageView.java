package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.Point;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.ArDkPage;
import com.artifex.solib.SOPage;

public class DocExcelPageView extends DocPageView {
    public DocExcelPageView(Context context, ArDkDoc arDkDoc) {
        super(context, arDkDoc);
    }

    public float[] getHorizontalRuler() {
        ArDkPage arDkPage;
        if (!this.mFinished && (arDkPage = this.mPage) != null) {
            return ((SOPage) arDkPage).getHorizontalRuler();
        }
        return null;
    }

    public int getUnscaledHeight() {
        Point point = this.mSize;
        if (point == null) {
            return 0;
        }
        return point.y;
    }

    public int getUnscaledWidth() {
        Point point = this.mSize;
        if (point == null) {
            return 0;
        }
        return point.x;
    }

    public float[] getVerticalRuler() {
        ArDkPage arDkPage;
        if (!this.mFinished && (arDkPage = this.mPage) != null) {
            return ((SOPage) arDkPage).getVerticalRuler();
        }
        return null;
    }
}
