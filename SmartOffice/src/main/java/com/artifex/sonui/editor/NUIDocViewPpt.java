package com.artifex.sonui.editor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.SODoc;
import com.artifex.sonui.editor.NUIDocView;
import com.artifex.sonui.editor.NUIView;
import com.artifex.sonui.editor.ShapeDialog;

public class NUIDocViewPpt extends NUIDocView {
    public boolean arranging = false;
    public boolean insertingShape = false;
    public ToolbarButton mArrangeBackButton;
    public ToolbarButton mArrangeBackwardButton;
    public ToolbarButton mArrangeForwardButton;
    public ToolbarButton mArrangeFrontButton;
    public ToolbarButton mInsertShapeButton;
    public ToolbarButton mLineColorButton;
    public ToolbarButton mLineTypeButton;
    public ToolbarButton mLineWidthButton;
    public NUIEditToolbar mNuiEditToolbar = new NUIEditToolbar();
    public ToolbarButton mShapeColorButton;
    public ToolbarButton mSlideshowButton;

    public NUIDocViewPpt(Context context) {
        super(context);
    }

    public void afterFirstLayoutComplete() {
        super.afterFirstLayoutComplete();
        this.mNuiEditToolbar.create(this);
        this.mShapeColorButton = (ToolbarButton) createToolbarButton(R.id.shape_color);
        this.mLineColorButton = (ToolbarButton) createToolbarButton(R.id.line_color);
        this.mLineWidthButton = (ToolbarButton) createToolbarButton(R.id.line_width);
        this.mLineTypeButton = (ToolbarButton) createToolbarButton(R.id.line_type);
        this.mArrangeBackButton = (ToolbarButton) createToolbarButton(R.id.arrange_back);
        this.mArrangeBackwardButton = (ToolbarButton) createToolbarButton(R.id.arrange_backwards);
        this.mArrangeForwardButton = (ToolbarButton) createToolbarButton(R.id.arrange_forward);
        this.mArrangeFrontButton = (ToolbarButton) createToolbarButton(R.id.arrange_front);
        this.mInsertShapeButton = (ToolbarButton) createToolbarButton(R.id.insert_shape_button);
        this.mSlideshowButton = (ToolbarButton) createToolbarButton(R.id.slideshow_button);
    }

    public boolean canCanManipulatePages() {
        return this.mDocCfgOptions.isEditingEnabled();
    }

    public void createDrawButtons() {
        this.mDrawButton = (ToolbarButton) createToolbarButton(R.id.draw_button);
        this.mDrawLineColorButton = (ToolbarButton) createToolbarButton(R.id.line_color_button);
        this.mDrawLineThicknessButton = (ToolbarButton) createToolbarButton(R.id.line_thickness_button);
        this.mDeleteInkButton = (ToolbarButton) createToolbarButton(R.id.delete_ink_button);
    }

    public void createEditButtons() {
        super.createEditButtons();
    }

    public DocView createMainView(Activity activity) {
        return new DocPowerPointView(activity);
    }

    public void createPagesButtons() {
    }

    public void createReviewButtons() {
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
        String preInsertImage = Utilities.preInsertImage(getContext(), str);
        int targetPageNumber = getTargetPageNumber();
        ((SODoc) getDoc()).clearSelection();
        ((SODoc) getDoc()).insertImage(targetPageNumber, preInsertImage);
        getDocView().scrollToPage(targetPageNumber, false);
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
        return ContextCompat.getColor(getContext(), R.color.sodk_editor_header_ppt_color);
    }

    public int getLayoutId() {
        return R.layout.sodk_editor_powerpoint_document;
    }

    public TabData[] getTabData() {
        if (this.mTabs == null) {
            this.mTabs = new TabData[6];
            if (this.mDocCfgOptions.isEditingEnabled()) {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
                TabData[] tabDataArr = this.mTabs;
                String string = getContext().getString(R.string.sodk_editor_tab_edit);
                int i = R.id.editTab;
                int i2 = R.layout.sodk_editor_tab;
                tabDataArr[1] = new TabData(this, string, i, i2, 0);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_insert), R.id.insertTab, i2, 0);
                this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_format), R.id.formatTab, i2, 0);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_draw), R.id.drawTab, i2, 0);
                this.mTabs[5] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_slides), R.id.slidesTab, R.layout.sodk_editor_tab_right, 0);
            } else {
                this.mTabs[0] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_file), R.id.fileTab, R.layout.sodk_editor_tab_left, 0);
                TabData[] tabDataArr2 = this.mTabs;
                String string2 = getContext().getString(R.string.sodk_editor_tab_edit);
                int i3 = R.id.editTab;
                int i4 = R.layout.sodk_editor_tab;
                tabDataArr2[1] = new TabData(this, string2, i3, i4, 8);
                this.mTabs[2] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_insert), R.id.insertTab, i4, 8);
                this.mTabs[3] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_format), R.id.formatTab, i4, 8);
                this.mTabs[4] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_draw), R.id.drawTab, i4, 8);
                this.mTabs[5] = new TabData(this, getContext().getString(R.string.sodk_editor_tab_slides), R.id.slidesTab, R.layout.sodk_editor_tab_right, 0);
            }
        }
        return this.mTabs;
    }

    public int getTabSelectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tab_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color_selected);
        }
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_ppt_color);
    }

    public int getTabUnselectedColor() {
        if (getResources().getInteger(R.integer.sodk_editor_ui_doc_tabbar_color_from_doctype) == 0) {
            return ContextCompat.getColor(activity(), R.color.sodk_editor_header_color);
        }
        return ContextCompat.getColor(activity(), R.color.sodk_editor_header_ppt_color);
    }

    public void handlePagesTab(String str) {
        if (str.equals(getResources().getString(R.string.sodk_editor_tab_slides))) {
            showPages();
        } else {
            hidePages();
        }
    }

    public boolean hasSelectionAlignment() {
        return true;
    }

    public boolean isPagesTab() {
        return getCurrentTab().equals(activity().getString(R.string.sodk_editor_tab_slides));
    }

    public void onClick(View view) {
        if (!this.mFinished) {
            super.onClick(view);
            if (view == this.mShapeColorButton) {
                onClickFillColor(view);
            }
            if (view == this.mLineColorButton) {
                onClickLineColor(view);
            }
            if (view == this.mLineWidthButton) {
                onClickLineWidth(view);
            }
            if (view == this.mLineTypeButton) {
                onClickLineType(view);
            }
            if (view == this.mArrangeBackButton) {
                onClickArrangeBack(view);
            }
            if (view == this.mArrangeBackwardButton) {
                onClickArrangeBackwards(view);
            }
            if (view == this.mArrangeForwardButton) {
                onClickArrangeForwards(view);
            }
            if (view == this.mArrangeFrontButton) {
                onClickArrangeFront(view);
            }
            if (view == this.mInsertShapeButton) {
                onInsertShapeButton(view);
            }
            if (view == this.mSlideshowButton) {
                onClickSlideshow(view);
            }
        }
    }

    public void onClickArrangeBack(View view) {
        if (!this.arranging) {
            this.arranging = true;
            updateUIAppearance();
            ((SODoc) getDoc()).setSelectionArrangeBack();
        }
    }

    public void onClickArrangeBackwards(View view) {
        if (!this.arranging) {
            this.arranging = true;
            updateUIAppearance();
            ((SODoc) getDoc()).setSelectionArrangeBackwards();
        }
    }

    public void onClickArrangeForwards(View view) {
        if (!this.arranging) {
            this.arranging = true;
            updateUIAppearance();
            ((SODoc) getDoc()).setSelectionArrangeForwards();
        }
    }

    public void onClickArrangeFront(View view) {
        if (!this.arranging) {
            this.arranging = true;
            updateUIAppearance();
            ((SODoc) getDoc()).setSelectionArrangeFront();
        }
    }

    public void onClickFillColor(View view) {
        SODoc sODoc = (SODoc) getSession().getDoc();
        ColorDialog colorDialog = new ColorDialog(3, getContext(), sODoc, view, new ColorChangedListener() {
            public void onColorChanged(String str) {
                ((SODoc) NUIDocViewPpt.this.getDoc()).setSelectionFillColor(str);
            }
        }, sODoc.getSelectionFillColor(), sODoc.getBgColorList());
        colorDialog.setShowTitle(false);
        colorDialog.show();
    }

    public void onClickLineColor(View view) {
        final SODoc sODoc = (SODoc) getSession().getDoc();
        ColorDialog colorDialog = new ColorDialog(4, getContext(), sODoc, view, sODoc::setSelectionLineColor, sODoc.getSelectionLineColor(), sODoc.getBgColorList());
        colorDialog.setShowTitle(false);
        colorDialog.show();
    }

    public void onClickLineType(View view) {
        LineTypeDialog.show(activity(), view, getDoc());
    }

    public void onClickLineWidth(View view) {
        LineWidthDialog.show(activity(), view, getDoc());
    }

    public void onClickSlideshow(View view) {
        getDoc().clearSelection();
        getDoc().closeSearch();
        SlideShowActivity.setSession(this.mSession);
        Intent intent = new Intent(getContext(), SlideShowActivity.class);
        intent.setAction("android.intent.action.VIEW");
        activity().startActivity(intent);
    }

    public void onDocCompleted() {
        super.onDocCompleted();
        NUIView.DocStateListener docStateListener = this.mDocStateListener;
        if (docStateListener != null) {
            docStateListener.docLoaded();
        }
    }

    public void onInsertShapeButton(View view) {
        new ShapeDialog(getContext(), view, new ShapeDialog.onSelectShapeListener() {
            public void onSelectShape(ShapeDialog.Shape shape) {
                int targetPageNumber = NUIDocViewPpt.this.getTargetPageNumber();
                NUIDocViewPpt nUIDocViewPpt = NUIDocViewPpt.this;
                nUIDocViewPpt.insertingShape = true;
                nUIDocViewPpt.getDoc().clearSelection();
                ((SODoc) NUIDocViewPpt.this.getDoc()).insertAutoShape(targetPageNumber, shape.shape, shape.properties);
                NUIDocViewPpt.this.getDocView().scrollToPage(targetPageNumber, false);
            }
        }).show();
    }

    public void onSelectionChanged() {
        super.onSelectionChanged();
        this.arranging = false;
        updateUIAppearance();
        if (this.insertingShape) {
            getDocView().scrollSelectionIntoView();
        }
        this.insertingShape = false;
        if (((SODoc) getDoc()).selectionIsAutoshapeOrImage()) {
            resetInputView();
        }
    }

    public void prepareToGoBack() {
        DocView docView = getDocView();
        if (docView != null && this.mCompleted) {
            docView.saveInk();
            docView.setDrawModeOff();
        }
    }

    public void setInsertButtonsClickable(boolean z) {
        super.setInsertButtonsClickable(z);
        ToolbarButton toolbarButton = this.mInsertShapeButton;
        if (toolbarButton != null) {
            toolbarButton.setClickable(z);
        }
    }

    public void setInsertTabVisibility() {
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

    public void showPagesTab() {
        Context context = getContext();
        int i = R.string.sodk_editor_tab_slides;
        showPagesTab(context.getString(i), i);
    }

    public void updateEditUIAppearance() {
        this.mNuiEditToolbar.updateEditUIAppearance();
        this.mNuiEditToolbar.updateEditUIAppearancePpt();
        this.mNuiEditToolbar.hideButtonsPpt();
    }

    public void updateInsertUIAppearance() {
        if (this.mInsertImageButton != null && this.mDocCfgOptions.isImageInsertEnabled()) {
            this.mInsertImageButton.setEnabled(true);
        }
        if (this.mInsertPhotoButton != null && this.mDocCfgOptions.isPhotoInsertEnabled()) {
            this.mInsertPhotoButton.setEnabled(true);
        }
    }

    public void updateReviewUIAppearance() {
    }

    public void updateUIAppearance() {
        super.updateUIAppearance();
        boolean selectionIsAutoshapeOrImage = ((SODoc) getDoc()).selectionIsAutoshapeOrImage();
        this.mShapeColorButton.setEnabled(selectionIsAutoshapeOrImage);
        this.mLineColorButton.setEnabled(selectionIsAutoshapeOrImage);
        this.mLineWidthButton.setEnabled(selectionIsAutoshapeOrImage);
        this.mLineTypeButton.setEnabled(selectionIsAutoshapeOrImage);
        boolean z = true;
        this.mArrangeBackButton.setEnabled(selectionIsAutoshapeOrImage && !this.arranging);
        this.mArrangeBackwardButton.setEnabled(selectionIsAutoshapeOrImage && !this.arranging);
        this.mArrangeForwardButton.setEnabled(selectionIsAutoshapeOrImage && !this.arranging);
        ToolbarButton toolbarButton = this.mArrangeFrontButton;
        if (!selectionIsAutoshapeOrImage || this.arranging) {
            z = false;
        }
        toolbarButton.setEnabled(z);
    }

    public NUIDocViewPpt(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NUIDocViewPpt(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
