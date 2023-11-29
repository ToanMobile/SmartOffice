package com.artifex.sonui.editor;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;

public class NUIPopupWindow extends PopupWindow {
    public NUIPopupWindow(Context context) {
        super(context);
        setup();
    }

    public void getLocationInWindow(int[] iArr) {
        getContentView().getLocationOnScreen(iArr);
        NUIDocView currentNUIDocView = NUIDocView.currentNUIDocView();
        if (currentNUIDocView != null) {
            iArr[1] = iArr[1] - currentNUIDocView.getCutoutHeightForRotation();
        }
    }

    public final void setup() {
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
    }

    public NUIPopupWindow(View view) {
        super(view);
        setup();
    }

    public NUIPopupWindow(View view, int i, int i2) {
        super(view, i, i2);
        setup();
    }
}
