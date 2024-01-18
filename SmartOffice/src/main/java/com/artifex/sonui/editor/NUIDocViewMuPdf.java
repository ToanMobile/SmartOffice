package com.artifex.sonui.editor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.artifex.sonui.editor.NUIDocView;

public class NUIDocViewMuPdf extends NUIDocViewPdf {
    public NUIDocViewMuPdf(Context context) {
        super(context);
    }

    public boolean canSelect() {
        return false;
    }

    public void checkXFA() {
    }

    public TabData[] getTabData() {
        if (this.mTabs == null) {
            TabData[] tabDataArr = new TabData[5];
            this.mTabs = tabDataArr;
            tabDataArr[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
            TabData[] tabDataArr2 = this.mTabs;
            String string = getContext().getString(R.string.sodk_editor_tab_annotate);
            int i = R.id.annotateTab;
            int i2 = R.layout.sodk_editor_tab;
            tabDataArr2[1] = new TabData(this, string, i, i2, 8);
            this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_redact), R.id.redactTab, i2, 8);
            this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_sign), R.id.signTab, i2, 8);
            this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_pages), R.id.pagesTab, R.layout.sodk_editor_tab_right, 0);
        }
        return this.mTabs;
    }

    public boolean hasRedo() {
        return false;
    }

    public boolean hasUndo() {
        return false;
    }

    public void hideUnusedButtons() {
        super.hideUnusedButtons();
        Button button = this.mTocButton;
        if (button != null) {
            button.setVisibility(View.GONE);
        }
    }

    public void setConfigurableButtons() {
        super.setConfigurableButtons();
        ToolbarButton toolbarButton = this.mSaveButton;
        if (toolbarButton != null) {
            toolbarButton.setVisibility(View.GONE);
        }
        ToolbarButton toolbarButton2 = this.mSaveAsButton;
        if (toolbarButton2 != null) {
            toolbarButton2.setVisibility(View.GONE);
        }
        ToolbarButton toolbarButton3 = this.mOpenPdfInButton;
        if (toolbarButton3 != null) {
            toolbarButton3.setVisibility(View.GONE);
        }
        ToolbarButton toolbarButton4 = this.mSavePdfButton;
        if (toolbarButton4 != null) {
            toolbarButton4.setVisibility(View.GONE);
        }
        ToolbarButton toolbarButton5 = this.mPrintButton;
        if (toolbarButton5 != null) {
            toolbarButton5.setVisibility(View.GONE);
        }
        ToolbarButton toolbarButton6 = this.mOpenInButton;
        if (toolbarButton6 != null) {
            toolbarButton6.setVisibility(View.GONE);
        }
    }

    public boolean shouldConfigureExportPdfAsButton() {
        return false;
    }
}
