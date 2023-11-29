package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import com.artifex.solib.SODoc;
import com.artifex.solib.SOPage;
import java.util.Objects;

public class DocPowerPointView extends DocView {
    public DocPowerPointView(Context context) {
        super(context);
    }

    public void setSelectionBoxBounds(RectF rectF) {
        SOPage sOPage = (SOPage) this.mSelectionStartPage.getPage();
        Objects.requireNonNull(sOPage);
        sOPage.setSelectionLimitsBox(rectF.left, rectF.top, rectF.right, rectF.bottom);
    }

    public void setupNoteEditor() {
    }

    public boolean shouldPreclearSelection() {
        return !((SODoc) getDoc()).selectionIsAutoshapeOrImage();
    }

    public void updateReview() {
    }

    public DocPowerPointView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DocPowerPointView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
