package com.artifex.sonui.editor;

import android.view.View;

public interface DocViewHost {
    void clickSheetButton(int i, boolean z);

    int getBorderColor();

    DocView getDocView();

    int getKeyboardHeight();

    void layoutNow();

    void onShowKeyboard(boolean z);

    void prefinish();

    void reportViewChanges();

    void selectionupdated();

    void setCurrentPage(View view, int i);

    boolean showKeyboard();

    void updateUI();
}
