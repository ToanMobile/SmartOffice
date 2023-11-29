package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.artifex.solib.SOPage;
import java.util.Objects;

public class DocDocView extends DocView {
    public DocDocView(Context context) {
        super(context);
    }

    public void setSelectionBoxBounds(RectF rectF) {
        SOPage sOPage = (SOPage) this.mSelectionStartPage.getPage();
        Objects.requireNonNull(sOPage);
        sOPage.setSelectionLimitsBox(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }

    public DocDocView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DocDocView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
