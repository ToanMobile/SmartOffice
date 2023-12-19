package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.artifex.solib.FileUtils;
import com.artifex.sonui.editor.NUIDocView;
import com.artifex.sonui.editor.NUIView;
import java.util.Arrays;
import java.util.List;

public class NUIDocViewOther extends NUIDocView {
    public NUIDocViewOther(Context context) {
        super(context);
    }

    private String getFileExtension() {
        String str;
        if (this.mStartUri != null) {
            return FileUtils.getFileTypeExtension(getContext(), this.mStartUri);
        }
        SODocSession sODocSession = this.mSession;
        if (sODocSession != null) {
            str = sODocSession.getUserPath();
        } else {
            SOFileState sOFileState = this.mState;
            str = sOFileState != null ? sOFileState.getInternalPath() : "";
        }
        return FileUtils.getExtension(str);
    }

    public void afterFirstLayoutComplete() {
        super.afterFirstLayoutComplete();
        if (useFullToolbar()) {
            hideUnnecessaryDivider2(R.id.other_toolbar);
            return;
        }
        findViewById(R.id.search_toolbar).setVisibility(View.GONE);
        findViewById(R.id.first_page_button).setVisibility(View.GONE);
        findViewById(R.id.last_page_button).setVisibility(View.GONE);
        findViewById(R.id.reflow_button).setVisibility(View.GONE);
        findViewById(R.id.divider_1).setVisibility(View.GONE);
        findViewById(R.id.divider_2).setVisibility(View.GONE);
    }

    public boolean canSelect() {
        return false;
    }

    public PageAdapter createAdapter() {
        return new PageAdapter(activity(), this, 1);
    }

    public void createEditButtons() {
    }

    public void createEditButtons2() {
    }

    public void createInputView() {
    }

    public void createInsertButtons() {
    }

    public DocView createMainView(Activity activity) {
        return new DocView(activity);
    }

    public void createReviewButtons() {
    }

    public void enforceInitialShowUI(View view) {
        boolean showUI = this.mDocCfgOptions.showUI();
        int i = 0;
        if (!this.mShowUI) {
            showUI = false;
        }
        findViewById(R.id.other_top).setVisibility(showUI ? 0 : 8);
        findViewById(R.id.footer).setVisibility(showUI ? 0 : 8);
        View findViewById = findViewById(R.id.header);
        if (!showUI) {
            i = 8;
        }
        findViewById.setVisibility(i);
        this.mFullscreen = !showUI;
    }

    public int getBorderColor() {
        return ContextCompat.getColor(getContext(), R.color.sodk_editor_selected_page_border_color);
    }

    public int getLayoutId() {
        return R.layout.sodk_editor_other_document;
    }

    public TabData[] getTabData() {
        if (this.mTabs == null) {
            TabData[] tabDataArr = new TabData[2];
            this.mTabs = tabDataArr;
            tabDataArr[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_one, 0);
            this.mTabs[1] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_pages), R.id.pagesTab, R.layout.sodk_editor_tab_right, 8);
        }
        return this.mTabs;
    }

    public int getTabUnselectedColor() {
        return ContextCompat.getColor(getContext(), R.color.sodk_editor_header_other_color);
    }

    public boolean hasRedo() {
        return false;
    }

    public boolean hasSearch() {
        String fileExtension = getFileExtension();
        return fileExtension != null && fileExtension.compareToIgnoreCase("txt") == 0;
    }

    public boolean hasUndo() {
        return false;
    }

    public void hideUnnecessaryDivider2(int i) {
        LinearLayout linearLayout = (LinearLayout) findViewById(i);
        if (linearLayout != null) {
            int i2 = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < linearLayout.getChildCount(); i4++) {
                View childAt = linearLayout.getChildAt(i4);
                int id = childAt.getId();
                if (id == R.id.divider_1 || id == R.id.divider_2) {
                    i3++;
                } else if (childAt.getVisibility() == 0 && i3 == 1) {
                    i2++;
                }
            }
            if (i2 == 0) {
                findViewById(R.id.divider_1).setVisibility(View.GONE);
                findViewById(R.id.divider_2).setVisibility(View.GONE);
            }
        }
    }

    public boolean inputViewHasFocus() {
        return false;
    }

    public void onClick(View view) {
        if (!this.mFinished) {
            super.onClick(view);
        }
    }

    public void onDocCompleted() {
        super.onDocCompleted();
        NUIView.DocStateListener docStateListener = this.mDocStateListener;
        if (docStateListener != null) {
            docStateListener.docLoaded();
        }
    }

    public void onFullScreenHide() {
        findViewById(R.id.other_top).setVisibility(View.GONE);
        findViewById(R.id.footer).setVisibility(View.GONE);
        findViewById(R.id.header).setVisibility(View.GONE);
        layoutNow();
    }

    public void onPause(Runnable runnable) {
        onPauseCommon();
        runnable.run();
    }

    public void onRedoButton(View view) {
        super.onRedoButton(view);
    }

    public void onReflowButton(View view) {
        if (useFullToolbar()) {
            super.onReflowButton(view);
        }
    }

    public void onResume() {
        onResumeCommon();
    }

    public void onUndoButton(View view) {
        super.onUndoButton(view);
    }

    public void scaleHeader() {
        scaleToolbar(R.id.other_toolbar, 0.65f);
        this.mBackButton.setScaleX(0.65f);
        this.mBackButton.setScaleY(0.65f);
    }

    public void setupTabs() {
    }

    public void showUI(boolean z) {
        if (z) {
            Runnable runnable = this.mExitFullScreenRunnable;
            if (runnable != null) {
                runnable.run();
            }
            this.mFullscreen = false;
        }
        if (this.mShowUI) {
            View findViewById = findViewById(R.id.other_top);
            if (z) {
                findViewById.setVisibility(View.VISIBLE);
                findViewById(R.id.footer).setVisibility(View.VISIBLE);
                findViewById(R.id.header).setVisibility(View.VISIBLE);
            } else {
                if (!isSearchVisible()) {
                    findViewById.setVisibility(View.GONE);
                }
                findViewById(R.id.footer).setVisibility(View.GONE);
            }
            layoutNow();
            afterShowUI(z);
        }
    }

    public void updateUIAppearance() {
        updateSearchUIAppearance();
    }

    public final boolean useFullToolbar() {
        List asList = Arrays.asList(new String[]{"txt", "csv", "hwp"});
        String fileExtension = getFileExtension();
        if (fileExtension == null) {
            return false;
        }
        return asList.contains(fileExtension.toLowerCase());
    }

    public boolean usePagesView() {
        return false;
    }

    public NUIDocViewOther(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NUIDocViewOther(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
