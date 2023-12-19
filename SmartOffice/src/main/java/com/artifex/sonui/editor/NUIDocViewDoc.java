package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.TabWidget;
import androidx.core.content.ContextCompat;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.FileUtils;
import com.artifex.solib.SODoc;
import com.artifex.solib.SOLib;
import com.artifex.solib.SmartOfficeDocType;
import com.artifex.sonui.editor.NUIDocView;
import com.artifex.sonui.editor.NUIView;
import java.util.ArrayList;
import java.util.Objects;

public class NUIDocViewDoc extends NUIDocView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public NUIEditToolbar mNuiEditToolbar = new NUIEditToolbar();
    public ToolbarButton mReviewAcceptButton;
    public ToolbarButton mReviewAuthorButton;
    public ToolbarButton mReviewCommentButton;
    public ToolbarButton mReviewDeleteCommentButton;
    public ToolbarButton mReviewNextButton;
    public ToolbarButton mReviewPreviousButton;
    public ToolbarButton mReviewRejectButton;
    public ToolbarButton mReviewShowChangesButton;
    public ToolbarButton mReviewTrackChangesButton;

    public NUIDocViewDoc(Context context) {
        super(context);
    }

    public void afterFirstLayoutComplete() {
        super.afterFirstLayoutComplete();
        this.mNuiEditToolbar.create(this);
    }

    public void applyDocumentTypeConfig(String str) {
        SOLib lib = SOLib.getLib(activity());
        if (lib != null) {
            if (SmartOfficeDocType.values()[lib.getDocTypeFromFileExtension(str)] == SmartOfficeDocType.SmartOfficeDocType_ODT) {
                this.mDocCfgOptions.setEditingEnabled(false);
            }
        }
    }

    public void createDrawButtons() {
        this.mDrawButton = (ToolbarButton) createToolbarButton(R.id.draw_button);
        this.mDrawLineColorButton = (ToolbarButton) createToolbarButton(R.id.line_color_button);
        this.mDrawLineThicknessButton = (ToolbarButton) createToolbarButton(R.id.line_thickness_button);
        this.mDeleteInkButton = (ToolbarButton) createToolbarButton(R.id.delete_ink_button);
    }

    public DocView createMainView(Activity activity) {
        return new DocDocView(activity);
    }

    public void createReviewButtons() {
        if (this.mDocCfgOptions.isEditingEnabled()) {
            this.mReviewShowChangesButton = (ToolbarButton) createToolbarButton(R.id.show_changes_button);
            this.mReviewTrackChangesButton = (ToolbarButton) createToolbarButton(R.id.track_changes_button);
            this.mReviewCommentButton = (ToolbarButton) createToolbarButton(R.id.comment_button);
            this.mReviewDeleteCommentButton = (ToolbarButton) createToolbarButton(R.id.delete_comment_button);
            this.mReviewAcceptButton = (ToolbarButton) createToolbarButton(R.id.accept_button);
            this.mReviewRejectButton = (ToolbarButton) createToolbarButton(R.id.reject_button);
            this.mReviewNextButton = (ToolbarButton) createToolbarButton(R.id.next_button);
            this.mReviewPreviousButton = (ToolbarButton) createToolbarButton(R.id.previous_button);
            this.mReviewAuthorButton = (ToolbarButton) createToolbarButton(R.id.author_button);
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.mReviewShowChangesButton);
            arrayList.add(this.mReviewTrackChangesButton);
            arrayList.add(this.mReviewCommentButton);
            arrayList.add(this.mReviewDeleteCommentButton);
            arrayList.add(this.mReviewAcceptButton);
            arrayList.add(this.mReviewRejectButton);
            arrayList.add(this.mReviewNextButton);
            arrayList.add(this.mReviewPreviousButton);
            Objects.requireNonNull(this.mDocCfgOptions);
            if (this.mDocCfgOptions.mSettingsBundle.getBoolean("DocAuthEntryEnabledKey", true)) {
                arrayList.add(this.mReviewAuthorButton);
            } else {
                findViewById(R.id.author_button_divider).setVisibility(View.GONE);
                this.mReviewAuthorButton.setVisibility(View.GONE);
            }
            ToolbarButton.setAllSameSize((ToolbarButton[]) arrayList.toArray(new ToolbarButton[arrayList.size()]));
        }
    }

    public void doBold() {
        this.mNuiEditToolbar.doBold();
    }

    public void doCopy() {
        this.mNuiEditToolbar.doCopy();
    }

    public void doCut() {
        this.mNuiEditToolbar.doCut();
    }

    public void doInsertImage(String str) {
        if (!FileUtils.fileExists(str)) {
            Utilities.showMessage((Activity) getContext(), getContext().getString(R.string.sodk_editor_insert_image_gone_title), getContext().getString(R.string.sodk_editor_insert_image_gone_body));
            return;
        }
        String preInsertImage = Utilities.preInsertImage(getContext(), str);
        ((SODoc) getDoc()).insertImage(preInsertImage);
        if (!str.equalsIgnoreCase(preInsertImage)) {
            addDeleteOnClose(preInsertImage);
        }
    }

    public void doItalic() {
        this.mNuiEditToolbar.doItalic();
    }

    public void doPaste() {
        this.mNuiEditToolbar.doPaste();
    }

    public void doUnderline() {
        this.mNuiEditToolbar.doUnderline();
    }

    public int getBorderColor() {
        return ContextCompat.getColor(getContext(), R.color.sodk_editor_header_doc_color);
    }

    public int getLayoutId() {
        return R.layout.sodk_editor_document;
    }

    public boolean hasIndent() {
        return true;
    }

    public boolean hasReflow() {
        return true;
    }

    public boolean hasSelectionAlignment() {
        return true;
    }

    public final void nextTrackedChange(boolean z) {
        DocView docView = getDocView();
        SODoc sODoc = (SODoc) getDocView().getDoc();
        docView.preNextPrevTrackedChange();
        ArDkSelectionLimits selectionLimits = docView.getSelectionLimits();
        boolean isActive = selectionLimits != null ? selectionLimits.getIsActive() : false;
        if (z != isActive) {
            if (z) {
                docView.selectTopLeft();
            } else {
                sODoc.clearSelection();
            }
        }
        if (sODoc.nextTrackedChange()) {
            docView.onNextPrevTrackedChange();
            return;
        }
        if (!isActive) {
            sODoc.clearSelection();
        }
        Utilities.yesNoMessage(activity(), activity().getString(R.string.sodk_editor_no_more_found), activity().getString(R.string.sodk_editor_keep_searching), activity().getString(R.string.sodk_editor_str_continue), activity().getString(R.string.sodk_editor_stop), new Runnable() {
            public void run() {
                NUIDocViewDoc nUIDocViewDoc = NUIDocViewDoc.this;
                int i = NUIDocViewDoc.$r8$clinit;
                nUIDocViewDoc.nextTrackedChange(false);
            }
        }, (Runnable) null);
    }

    public void onAcceptButton(View view) {
        ((SODoc) getDocView().getDoc()).acceptTrackedChange();
    }

    public void onClick(View view) {
        if (!this.mFinished) {
            super.onClick(view);
            if (view == this.mReviewShowChangesButton) {
                onShowChangesButton(view);
            }
            if (view == this.mReviewTrackChangesButton) {
                onTrackChangesButton(view);
            }
            if (view == this.mReviewCommentButton) {
                onCommentButton(view);
            }
            if (view == this.mReviewDeleteCommentButton) {
                onDeleteCommentButton(view);
            }
            if (view == this.mReviewAcceptButton) {
                onAcceptButton(view);
            }
            if (view == this.mReviewRejectButton) {
                onRejectButton(view);
            }
            if (view == this.mReviewNextButton) {
                onNextButton(view);
            }
            if (view == this.mReviewPreviousButton) {
                onPreviousButton(view);
            }
            if (view == this.mReviewAuthorButton) {
                onAuthorButton(view);
            }
        }
    }

    public void onCommentButton(View view) {
        getDocView().addComment();
    }

    public void onDeleteCommentButton(View view) {
        getDocView().getDoc().deleteHighlightAnnotation();
    }

    public void onDocCompleted() {
        super.onDocCompleted();
        NUIView.DocStateListener docStateListener = this.mDocStateListener;
        if (docStateListener != null) {
            docStateListener.docLoaded();
        }
    }

    public void onFullScreen(View view) {
        getDocView().saveComment();
        super.onFullScreen(view);
    }

    public void onNextButton(View view) {
        nextTrackedChange(true);
    }

    public void onPreviousButton(View view) {
        previousTrackedChange(true);
    }

    public void onRejectButton(View view) {
        ((SODoc) getDocView().getDoc()).rejectTrackedChange();
    }

    public void onShowChangesButton(View view) {
        getDocView().saveComment();
        SODoc sODoc = (SODoc) getDocView().getDoc();
        sODoc.setShowingTrackedChanges(!sODoc.getShowingTrackedChanges());
        onSelectionChanged();
    }

    public void onTrackChangesButton(View view) {
        getDocView().saveComment();
        SODoc sODoc = (SODoc) getDocView().getDoc();
        sODoc.setTrackingChanges(!sODoc.getTrackingChanges());
        onSelectionChanged();
    }

    public void prepareToGoBack() {
        DocView docView = getDocView();
        super.prepareToGoBack();
        if (docView != null && this.mCompleted) {
            docView.preNextPrevTrackedChange();
            docView.saveInk();
            docView.setDrawModeOff();
        }
    }

    public final void previousTrackedChange(boolean z) {
        DocView docView = getDocView();
        SODoc sODoc = (SODoc) getDocView().getDoc();
        docView.preNextPrevTrackedChange();
        ArDkSelectionLimits selectionLimits = docView.getSelectionLimits();
        boolean isActive = selectionLimits != null ? selectionLimits.getIsActive() : false;
        if (z != isActive) {
            if (z) {
                docView.selectTopLeft();
            } else {
                sODoc.clearSelection();
            }
        }
        if (sODoc.previousTrackedChange()) {
            docView.onNextPrevTrackedChange();
            return;
        }
        if (!isActive) {
            sODoc.clearSelection();
        }
        Utilities.yesNoMessage(activity(), activity().getString(R.string.sodk_editor_no_more_found), activity().getString(R.string.sodk_editor_keep_searching), activity().getString(R.string.sodk_editor_str_continue), activity().getString(R.string.sodk_editor_stop), new Runnable() {
            public void run() {
                NUIDocViewDoc nUIDocViewDoc = NUIDocViewDoc.this;
                int i = NUIDocViewDoc.$r8$clinit;
                nUIDocViewDoc.previousTrackedChange(false);
            }
        }, (Runnable) null);
    }

    public void setInsertTabVisibility() {
        int indexOf;
        if (this.tabHost != null) {
            if (!Utilities.isPhoneDevice(getContext())) {
                TabWidget tabWidget = this.tabHost.getTabWidget();
                if (tabWidget != null && (indexOf = this.mAllTabHostTabs.indexOf(getContext().getString(R.string.sodk_editor_tab_insert))) != -1) {
                    TabData[] tabData = getTabData();
                    if (this.mDocCfgOptions.isImageInsertEnabled() || this.mDocCfgOptions.isPhotoInsertEnabled()) {
                        tabWidget.getChildTabViewAt(indexOf).setVisibility(View.VISIBLE);
                        return;
                    }
                    if (this.tabHost.getCurrentTab() == indexOf) {
                        this.tabHost.setCurrentTab(this.mAllTabHostTabs.indexOf(tabData[getInitialTab()].name));
                    }
                    tabWidget.getChildTabViewAt(indexOf).setVisibility(View.GONE);
                }
            } else if (this.mTabs != null && this.mDocCfgOptions.isEditingEnabled()) {
                TabData[] tabData2 = getTabData();
                if (this.mDocCfgOptions.isImageInsertEnabled() || this.mDocCfgOptions.isPhotoInsertEnabled()) {
                    ListPopupWindow listPopupWindow = this.mListPopupWindow;
                    if (!(listPopupWindow == null || !listPopupWindow.isShowing() || tabData2[2].visibility == 0)) {
                        this.mListPopupWindow.dismiss();
                    }
                    tabData2[2].visibility = 0;
                    return;
                }
                if (this.mCurrentTab.equals(this.mTabs[2].name)) {
                    String str = this.mTabs[getInitialTab()].name;
                    changeTab(str);
                    setSingleTabTitle(str);
                    this.tabHost.setCurrentTabByTag(str);
                    setOneTabColor(getSingleTabView(), true);
                }
                ListPopupWindow listPopupWindow2 = this.mListPopupWindow;
                if (!(listPopupWindow2 == null || !listPopupWindow2.isShowing() || tabData2[2].visibility == 8)) {
                    this.mListPopupWindow.dismiss();
                }
                tabData2[2].visibility = 8;
            }
        }
    }

    public void setupTabs() {
        super.setupTabs();
        Objects.requireNonNull(this.mDocCfgOptions);
        super.measureTabs();
    }

    public boolean showKeyboard() {
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        if (selectionLimits == null) {
            return false;
        }
        SODoc sODoc = (SODoc) getDoc();
        if (!selectionLimits.getIsActive() || sODoc.getSelectionCanBeAbsolutelyPositioned() || sODoc.selectionIsAutoshapeOrImage()) {
            return false;
        }
        super.showKeyboard();
        return true;
    }

    public void updateEditUIAppearance() {
        this.mNuiEditToolbar.updateEditUIAppearance();
    }

    public void updateReviewUIAppearance() {
        SODoc sODoc = (SODoc) getDocView().getDoc();
        boolean showingTrackedChanges = sODoc.getShowingTrackedChanges();
        boolean trackingChanges = sODoc.getTrackingChanges();
        if (showingTrackedChanges) {
            this.mReviewShowChangesButton.setImageResource(R.drawable.sodk_editor_icon_toggle_on);
        } else {
            this.mReviewShowChangesButton.setImageResource(R.drawable.sodk_editor_icon_toggle_off);
        }
        if (trackingChanges) {
            this.mReviewTrackChangesButton.setImageResource(R.drawable.sodk_editor_icon_toggle_on);
        } else {
            this.mReviewTrackChangesButton.setImageResource(R.drawable.sodk_editor_icon_toggle_off);
        }
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        boolean z = false;
        boolean isActive = selectionLimits != null ? selectionLimits.getIsActive() : false;
        boolean z2 = isActive && sODoc.getSelectionHasAssociatedPopup();
        boolean z3 = isActive && !z2 && showingTrackedChanges;
        if (z2) {
            this.mReviewDeleteCommentButton.setVisibility(View.VISIBLE);
            this.mReviewCommentButton.setVisibility(View.GONE);
            this.mReviewDeleteCommentButton.setEnabled(true);
        } else {
            this.mReviewDeleteCommentButton.setVisibility(View.GONE);
            this.mReviewCommentButton.setVisibility(View.VISIBLE);
            this.mReviewCommentButton.setEnabled(z3);
        }
        this.mReviewPreviousButton.setEnabled(showingTrackedChanges);
        this.mReviewNextButton.setEnabled(showingTrackedChanges);
        boolean selectionIsReviewable = sODoc.selectionIsReviewable();
        if (showingTrackedChanges && trackingChanges && selectionIsReviewable) {
            z = true;
        }
        this.mReviewAcceptButton.setEnabled(z);
        this.mReviewRejectButton.setEnabled(z);
    }

    public NUIDocViewDoc(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NUIDocViewDoc(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
