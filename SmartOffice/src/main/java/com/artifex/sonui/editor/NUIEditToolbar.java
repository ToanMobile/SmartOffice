package com.artifex.sonui.editor;

import android.content.Context;
import android.view.View;
import com.artifex.solib.ArDkLib;
import com.artifex.solib.ArDkSelectionLimits;
import com.artifex.solib.ConfigOptions;
import com.artifex.solib.SODoc;

public class NUIEditToolbar implements View.OnClickListener {
    public ToolbarButton mAlignCenterButton;
    public ToolbarButton mAlignJustifyButton;
    public ToolbarButton mAlignLeftButton;
    public ToolbarButton mAlignRightButton;
    public ToolbarButton mCopyButton;
    public ToolbarButton mCutButton;
    public ToolbarButton mDecreaseIndentButton;
    public ConfigOptions mDocCfgOptions;
    public ToolbarButton mFontBackgroundButton;
    public ToolbarButton mFontColorButton;
    public ToolbarButton mFontDownButton;
    public SOTextView mFontNameText;
    public SOTextView mFontSizeText;
    public ToolbarButton mFontUpButton;
    public ToolbarButton mIncreaseIndentButton;
    public ToolbarButton mListBulletsButton;
    public ToolbarButton mListNumbersButton;
    public NUIDocView mNuiDocView;
    public ToolbarButton mPasteButton;
    public ToolbarButton mSelDeleteButton;
    public ToolbarButton mStyleBoldButton;
    public ToolbarButton mStyleItalicButton;
    public ToolbarButton mStyleLinethroughButton;
    public ToolbarButton mStyleUnderlineButton;

    public void create(NUIDocView nUIDocView) {
        this.mNuiDocView = nUIDocView;
        this.mDocCfgOptions = nUIDocView.mDocCfgOptions;
        createEditButtons();
    }

    public void createEditButtons() {
        this.mListBulletsButton = (ToolbarButton) createToolbarButton(R.id.list_bullets_button);
        this.mListNumbersButton = (ToolbarButton) createToolbarButton(R.id.list_numbers_button);
        this.mAlignLeftButton = (ToolbarButton) createToolbarButton(R.id.align_left_button);
        this.mAlignCenterButton = (ToolbarButton) createToolbarButton(R.id.align_center_button);
        this.mAlignRightButton = (ToolbarButton) createToolbarButton(R.id.align_right_button);
        this.mAlignJustifyButton = (ToolbarButton) createToolbarButton(R.id.align_justify_button);
        this.mIncreaseIndentButton = (ToolbarButton) createToolbarButton(R.id.indent_increase_button);
        this.mDecreaseIndentButton = (ToolbarButton) createToolbarButton(R.id.indent_decrease_button);
        this.mFontSizeText = (SOTextView) createToolbarButton(R.id.font_size_text);
        this.mFontUpButton = (ToolbarButton) createToolbarButton(R.id.fontup_button);
        this.mFontDownButton = (ToolbarButton) createToolbarButton(R.id.fontdown_button);
        this.mFontNameText = (SOTextView) createToolbarButton(R.id.font_name_text);
        this.mFontColorButton = (ToolbarButton) createToolbarButton(R.id.font_color_button);
        this.mFontBackgroundButton = (ToolbarButton) createToolbarButton(R.id.font_background_button);
        this.mCutButton = (ToolbarButton) createToolbarButton(R.id.cut_button);
        this.mCopyButton = (ToolbarButton) createToolbarButton(R.id.copy_button);
        this.mPasteButton = (ToolbarButton) createToolbarButton(R.id.paste_button);
        this.mSelDeleteButton = (ToolbarButton) createToolbarButton(R.id.delete_button);
        this.mStyleBoldButton = (ToolbarButton) createToolbarButton(R.id.bold_button);
        this.mStyleItalicButton = (ToolbarButton) createToolbarButton(R.id.italic_button);
        this.mStyleUnderlineButton = (ToolbarButton) createToolbarButton(R.id.underline_button);
        this.mStyleLinethroughButton = (ToolbarButton) createToolbarButton(R.id.striketrough_button);
    }

    public View createToolbarButton(int i) {
        NUIDocView nUIDocView = this.mNuiDocView;
        View findViewById = nUIDocView != null ? nUIDocView.findViewById(i) : null;
        if (findViewById != null) {
            findViewById.setOnClickListener(this);
        }
        return findViewById;
    }

    public void doBold() {
        SODoc sODoc = (SODoc) getSession().getDoc();
        boolean z = !sODoc.getSelectionIsBold();
        this.mStyleBoldButton.setSelected(z);
        sODoc.setSelectionIsBold(z);
    }

    public void doCopy() {
        boolean isExtClipboardOutEnabled = this.mDocCfgOptions.isExtClipboardOutEnabled();
        SODoc sODoc = (SODoc) getSession().getDoc();
        if (sODoc.getSelectionCanBeCopied()) {
            sODoc.selectionCopyToClip();
            if (isExtClipboardOutEnabled) {
                ArDkLib.putTextToClipboard(sODoc.getClipboardAsText());
            }
            sODoc.mExternalClipDataHash = ArDkLib.getClipboardText().hashCode();
        }
    }

    public void doCut() {
        boolean isExtClipboardOutEnabled = this.mDocCfgOptions.isExtClipboardOutEnabled();
        SODoc sODoc = (SODoc) getSession().getDoc();
        if (sODoc.getSelectionCanBeCopied() && sODoc.getSelectionCanBeDeleted()) {
            sODoc.selectionCutToClip();
            if (isExtClipboardOutEnabled) {
                ArDkLib.putTextToClipboard(sODoc.getClipboardAsText());
            }
            sODoc.mExternalClipDataHash = ArDkLib.getClipboardText().hashCode();
        }
        this.mNuiDocView.updateInputView();
        this.mNuiDocView.afterCut();
    }

    public void doItalic() {
        SODoc sODoc = (SODoc) getSession().getDoc();
        boolean z = !sODoc.getSelectionIsItalic();
        this.mStyleItalicButton.setSelected(z);
        sODoc.setSelectionIsItalic(z);
    }

    public final void doListFormat() {
        if (this.mListNumbersButton.isSelected()) {
            ((SODoc) getSession().getDoc()).setSelectionListStyleDecimal();
        } else if (this.mListBulletsButton.isSelected()) {
            ((SODoc) getSession().getDoc()).setSelectionListStyleDisc();
        } else {
            ((SODoc) getSession().getDoc()).setSelectionListStyleNone();
        }
    }

    public void doPaste() {
        boolean isExtClipboardInEnabled = this.mDocCfgOptions.isExtClipboardInEnabled();
        SODoc sODoc = (SODoc) getSession().getDoc();
        int targetPageNumber = getTargetPageNumber();
        if (sODoc.getSelectionCanBePasteTarget() && !sODoc.selectionIsAutoshapeOrImage()) {
            if (isExtClipboardInEnabled && ArDkLib.clipboardHasText()) {
                String clipboardText = ArDkLib.getClipboardText();
                if (clipboardText.hashCode() != sODoc.mExternalClipDataHash) {
                    sODoc.setSelectionText(clipboardText);
                }
            }
            if (sODoc.clipboardHasData()) {
                sODoc.selectionPaste(targetPageNumber);
            }
        }
        this.mNuiDocView.updateInputView();
        this.mNuiDocView.afterPaste();
    }

    public void doStrikethrough() {
        SODoc sODoc = (SODoc) getSession().getDoc();
        boolean z = !sODoc.getSelectionIsLinethrough();
        this.mStyleLinethroughButton.setSelected(z);
        sODoc.setSelectionIsLinethrough(z);
    }

    public void doUnderline() {
        SODoc sODoc = (SODoc) getSession().getDoc();
        boolean z = !sODoc.getSelectionIsUnderlined();
        this.mStyleUnderlineButton.setSelected(z);
        sODoc.setSelectionIsUnderlined(z);
    }

    public final Context getContext() {
        return this.mNuiDocView.getContext();
    }

    public final DocView getDocView() {
        return this.mNuiDocView.getDocView();
    }

    public final SODocSession getSession() {
        return this.mNuiDocView.getSession();
    }

    public int getTargetPageNumber() {
        return this.mNuiDocView.getTargetPageNumber();
    }

    public void hideButtonsPpt() {
        this.mIncreaseIndentButton.setVisibility(8);
        this.mDecreaseIndentButton.setVisibility(8);
        this.mListBulletsButton.setVisibility(8);
        this.mListNumbersButton.setVisibility(8);
    }

    public void onAlignCenterButton(View view) {
        ((SODoc) getSession().getDoc()).setSelectionAlignment(SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_CENTER.getAlignment());
    }

    public void onAlignJustifyButton(View view) {
        ((SODoc) getSession().getDoc()).setSelectionAlignment(SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_JUSTIFY.getAlignment());
    }

    public void onAlignLeftButton(View view) {
        ((SODoc) getSession().getDoc()).setSelectionAlignment(SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_LEFT.getAlignment());
    }

    public void onAlignRightButton(View view) {
        ((SODoc) getSession().getDoc()).setSelectionAlignment(SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_RIGHT.getAlignment());
    }

    public void onClick(View view) {
        if (view != null) {
            if (view == this.mFontUpButton) {
                onFontUpButton(view);
            }
            if (view == this.mFontDownButton) {
                onFontDownButton(view);
            }
            if (view == this.mFontSizeText || (view.getParent() != null && view.getParent() == this.mFontSizeText)) {
                new EditFont(getContext(), view, this.mNuiDocView.getDoc()).show();
            }
            if (view == this.mFontNameText || (view.getParent() != null && view.getParent() == this.mFontNameText)) {
                new EditFont(getContext(), view, this.mNuiDocView.getDoc()).show();
            }
            if (view == this.mFontColorButton) {
                onFontColorButton(view);
            }
            if (view == this.mFontBackgroundButton) {
                onFontBackgroundButton(view);
            }
            if (view == this.mCutButton) {
                onCutButton(view);
            }
            if (view == this.mCopyButton) {
                onCopyButton(view);
            }
            if (view == this.mPasteButton) {
                onPasteButton(view);
            }
            if (view == this.mSelDeleteButton) {
                onSelDeleteButton(view);
            }
            if (view == this.mStyleBoldButton) {
                doBold();
            }
            if (view == this.mStyleItalicButton) {
                doItalic();
            }
            if (view == this.mStyleUnderlineButton) {
                doUnderline();
            }
            if (view == this.mStyleLinethroughButton) {
                doStrikethrough();
            }
            if (view == this.mListBulletsButton) {
                onListBulletsButton(view);
            }
            if (view == this.mListNumbersButton) {
                onListNumbersButton(view);
            }
            if (view == this.mAlignLeftButton) {
                onAlignLeftButton(view);
            }
            if (view == this.mAlignCenterButton) {
                onAlignCenterButton(view);
            }
            if (view == this.mAlignRightButton) {
                onAlignRightButton(view);
            }
            if (view == this.mAlignJustifyButton) {
                onAlignJustifyButton(view);
            }
            if (view == this.mIncreaseIndentButton) {
                onIndentIncreaseButton(view);
            }
            if (view == this.mDecreaseIndentButton) {
                onIndentDecreaseButton(view);
            }
        }
    }

    public void onCopyButton(View view) {
        doCopy();
    }

    public void onCutButton(View view) {
        doCut();
    }

    public void onFontBackgroundButton(View view) {
        final SODoc sODoc = (SODoc) getSession().getDoc();
        String selectionBackgroundColor = sODoc.getSelectionBackgroundColor();
        if (selectionBackgroundColor.equalsIgnoreCase("NONE")) {
            selectionBackgroundColor = "TRANSPARENT";
        }
        new ColorDialog(2, getContext(), sODoc, getDocView(), new ColorChangedListener(this) {
            public void onColorChanged(String str) {
                if (str.equalsIgnoreCase("transparent")) {
                    sODoc.setSelectionBackgroundTransparent();
                } else {
                    sODoc.setSelectionBackgroundColor(str);
                }
            }
        }, selectionBackgroundColor, sODoc.getBgColorList()).show();
    }

    public void onFontColorButton(View view) {
        final SODoc sODoc = (SODoc) getSession().getDoc();
        new ColorDialog(1, getContext(), sODoc, getDocView(), new ColorChangedListener(this) {
            public void onColorChanged(String str) {
                sODoc.setSelectionFontColor(str);
            }
        }, sODoc.getSelectionFontColor().toUpperCase(), (String[]) null).show();
    }

    public void onFontDownButton(View view) {
        SODoc sODoc = (SODoc) getSession().getDoc();
        double selectionFontSize = sODoc.getSelectionFontSize();
        if (((int) selectionFontSize) > 6) {
            sODoc.setSelectionFontSize(selectionFontSize - 1.0d);
        }
    }

    public void onFontUpButton(View view) {
        SODoc sODoc = (SODoc) getSession().getDoc();
        double selectionFontSize = sODoc.getSelectionFontSize();
        if (((int) selectionFontSize) < 72) {
            sODoc.setSelectionFontSize(selectionFontSize + 1.0d);
        }
    }

    public void onIndentDecreaseButton(View view) {
        int[] indentationLevel = ((SODoc) getSession().getDoc()).getIndentationLevel();
        if (indentationLevel != null && indentationLevel[0] > 0) {
            ((SODoc) getSession().getDoc()).setIndentationLevel(indentationLevel[0] - 1);
        }
    }

    public void onIndentIncreaseButton(View view) {
        int[] indentationLevel = ((SODoc) getSession().getDoc()).getIndentationLevel();
        if (indentationLevel != null && indentationLevel[0] < indentationLevel[1]) {
            ((SODoc) getSession().getDoc()).setIndentationLevel(indentationLevel[0] + 1);
        }
    }

    public void onListBulletsButton(View view) {
        if (this.mListBulletsButton.isSelected()) {
            this.mListBulletsButton.setSelected(false);
        } else {
            this.mListBulletsButton.setSelected(true);
            this.mListNumbersButton.setSelected(false);
        }
        doListFormat();
    }

    public void onListNumbersButton(View view) {
        if (this.mListNumbersButton.isSelected()) {
            this.mListNumbersButton.setSelected(false);
        } else {
            this.mListNumbersButton.setSelected(true);
            this.mListBulletsButton.setSelected(false);
        }
        doListFormat();
    }

    public void onPasteButton(View view) {
        doPaste();
    }

    public void onSelDeleteButton(View view) {
        getSession().getDoc().selectionDelete();
        this.mNuiDocView.getDocView().onSelectionDelete();
        this.mNuiDocView.updateInputView();
    }

    /* JADX WARNING: Removed duplicated region for block: B:103:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:104:0x01a8  */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01b1  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x01ce  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x0205  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0207  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x021f  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0221  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x0229 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0152  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateEditUIAppearance() {
        /*
            r12 = this;
            com.artifex.sonui.editor.DocView r0 = r12.getDocView()
            com.artifex.solib.ArDkSelectionLimits r0 = r0.getSelectionLimits()
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0027
            boolean r3 = r0.getIsActive()
            if (r3 == 0) goto L_0x001a
            boolean r4 = r0.getIsCaret()
            if (r4 != 0) goto L_0x001a
            r4 = 1
            goto L_0x001b
        L_0x001a:
            r4 = 0
        L_0x001b:
            if (r3 == 0) goto L_0x0025
            boolean r0 = r0.getIsCaret()
            if (r0 == 0) goto L_0x0025
            r0 = 1
            goto L_0x002a
        L_0x0025:
            r0 = 0
            goto L_0x002a
        L_0x0027:
            r0 = 0
            r3 = 0
            r4 = 0
        L_0x002a:
            com.artifex.sonui.editor.SODocSession r5 = r12.getSession()
            com.artifex.solib.ArDkDoc r5 = r5.getDoc()
            com.artifex.solib.SODoc r5 = (com.artifex.solib.SODoc) r5
            boolean r6 = r5.selectionIsAutoshapeOrImage()
            if (r4 == 0) goto L_0x003e
            if (r6 != 0) goto L_0x003e
            r4 = 1
            goto L_0x003f
        L_0x003e:
            r4 = 0
        L_0x003f:
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleBoldButton
            r7.setEnabled(r4)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleBoldButton
            if (r4 == 0) goto L_0x0050
            boolean r8 = r5.getSelectionIsBold()
            if (r8 == 0) goto L_0x0050
            r8 = 1
            goto L_0x0051
        L_0x0050:
            r8 = 0
        L_0x0051:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleItalicButton
            r7.setEnabled(r4)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleItalicButton
            if (r4 == 0) goto L_0x0065
            boolean r8 = r5.getSelectionIsItalic()
            if (r8 == 0) goto L_0x0065
            r8 = 1
            goto L_0x0066
        L_0x0065:
            r8 = 0
        L_0x0066:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleUnderlineButton
            r7.setEnabled(r4)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleUnderlineButton
            if (r4 == 0) goto L_0x007a
            boolean r8 = r5.getSelectionIsUnderlined()
            if (r8 == 0) goto L_0x007a
            r8 = 1
            goto L_0x007b
        L_0x007a:
            r8 = 0
        L_0x007b:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleLinethroughButton
            r7.setEnabled(r4)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mStyleLinethroughButton
            if (r4 == 0) goto L_0x008f
            boolean r8 = r5.getSelectionIsLinethrough()
            if (r8 == 0) goto L_0x008f
            r8 = 1
            goto L_0x0090
        L_0x008f:
            r8 = 0
        L_0x0090:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignLeftButton
            r7.setEnabled(r3)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignLeftButton
            if (r3 == 0) goto L_0x00aa
            int r8 = r5.getSelectionAlignment()
            com.artifex.solib.SODoc$HorizontalAlignment r9 = com.artifex.solib.SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_LEFT
            int r9 = r9.getAlignment()
            if (r8 != r9) goto L_0x00aa
            r8 = 1
            goto L_0x00ab
        L_0x00aa:
            r8 = 0
        L_0x00ab:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignCenterButton
            r7.setEnabled(r3)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignCenterButton
            if (r3 == 0) goto L_0x00c5
            int r8 = r5.getSelectionAlignment()
            com.artifex.solib.SODoc$HorizontalAlignment r9 = com.artifex.solib.SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_CENTER
            int r9 = r9.getAlignment()
            if (r8 != r9) goto L_0x00c5
            r8 = 1
            goto L_0x00c6
        L_0x00c5:
            r8 = 0
        L_0x00c6:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignRightButton
            r7.setEnabled(r3)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignRightButton
            if (r3 == 0) goto L_0x00e0
            int r8 = r5.getSelectionAlignment()
            com.artifex.solib.SODoc$HorizontalAlignment r9 = com.artifex.solib.SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_RIGHT
            int r9 = r9.getAlignment()
            if (r8 != r9) goto L_0x00e0
            r8 = 1
            goto L_0x00e1
        L_0x00e0:
            r8 = 0
        L_0x00e1:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignJustifyButton
            r7.setEnabled(r3)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mAlignJustifyButton
            if (r3 == 0) goto L_0x00fb
            int r8 = r5.getSelectionAlignment()
            com.artifex.solib.SODoc$HorizontalAlignment r9 = com.artifex.solib.SODoc.HorizontalAlignment.HORIZONTAL_ALIGN_JUSTIFY
            int r9 = r9.getAlignment()
            if (r8 != r9) goto L_0x00fb
            r8 = 1
            goto L_0x00fc
        L_0x00fb:
            r8 = 0
        L_0x00fc:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mListBulletsButton
            r7.setEnabled(r3)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mListBulletsButton
            if (r3 == 0) goto L_0x0110
            boolean r8 = r5.getSelectionListStyleIsDisc()
            if (r8 == 0) goto L_0x0110
            r8 = 1
            goto L_0x0111
        L_0x0110:
            r8 = 0
        L_0x0111:
            r7.setSelected(r8)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mListNumbersButton
            r7.setEnabled(r3)
            com.artifex.sonui.editor.ToolbarButton r7 = r12.mListNumbersButton
            if (r3 == 0) goto L_0x0125
            boolean r5 = r5.getSelectionListStyleIsDecimal()
            if (r5 == 0) goto L_0x0125
            r5 = 1
            goto L_0x0126
        L_0x0125:
            r5 = 0
        L_0x0126:
            r7.setSelected(r5)
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mIncreaseIndentButton
            if (r3 == 0) goto L_0x014a
            com.artifex.sonui.editor.SODocSession r7 = r12.getSession()
            com.artifex.solib.ArDkDoc r7 = r7.getDoc()
            com.artifex.solib.SODoc r7 = (com.artifex.solib.SODoc) r7
            int[] r7 = r7.getIndentationLevel()
            if (r7 == 0) goto L_0x0145
            r8 = r7[r2]
            r7 = r7[r1]
            if (r8 >= r7) goto L_0x0145
            r7 = 1
            goto L_0x0146
        L_0x0145:
            r7 = 0
        L_0x0146:
            if (r7 == 0) goto L_0x014a
            r7 = 1
            goto L_0x014b
        L_0x014a:
            r7 = 0
        L_0x014b:
            r5.setEnabled(r7)
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mDecreaseIndentButton
            if (r3 == 0) goto L_0x016d
            com.artifex.sonui.editor.SODocSession r7 = r12.getSession()
            com.artifex.solib.ArDkDoc r7 = r7.getDoc()
            com.artifex.solib.SODoc r7 = (com.artifex.solib.SODoc) r7
            int[] r7 = r7.getIndentationLevel()
            if (r7 == 0) goto L_0x0168
            r7 = r7[r2]
            if (r7 <= 0) goto L_0x0168
            r7 = 1
            goto L_0x0169
        L_0x0168:
            r7 = 0
        L_0x0169:
            if (r7 == 0) goto L_0x016d
            r7 = 1
            goto L_0x016e
        L_0x016d:
            r7 = 0
        L_0x016e:
            r5.setEnabled(r7)
            com.artifex.sonui.editor.SOTextView r5 = r12.mFontNameText
            r5.setEnabled(r4)
            com.artifex.sonui.editor.SOTextView r5 = r12.mFontSizeText
            r5.setEnabled(r4)
            com.artifex.sonui.editor.SODocSession r5 = r12.getSession()
            com.artifex.solib.ArDkDoc r5 = r5.getDoc()
            com.artifex.solib.SODoc r5 = (com.artifex.solib.SODoc) r5
            double r7 = r5.getSelectionFontSize()
            long r7 = java.lang.Math.round(r7)
            r9 = 0
            int r5 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r5 <= 0) goto L_0x01a8
            com.artifex.sonui.editor.SOTextView r5 = r12.mFontSizeText
            java.lang.Object[] r9 = new java.lang.Object[r1]
            int r10 = (int) r7
            java.lang.Integer r10 = java.lang.Integer.valueOf(r10)
            r9[r2] = r10
            java.lang.String r10 = "%d"
            java.lang.String r9 = java.lang.String.format(r10, r9)
            r5.setText((java.lang.CharSequence) r9)
            goto L_0x01af
        L_0x01a8:
            com.artifex.sonui.editor.SOTextView r5 = r12.mFontSizeText
            java.lang.String r9 = ""
            r5.setText((java.lang.CharSequence) r9)
        L_0x01af:
            if (r4 == 0) goto L_0x01ce
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mFontUpButton
            r9 = 72
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 >= 0) goto L_0x01bb
            r9 = 1
            goto L_0x01bc
        L_0x01bb:
            r9 = 0
        L_0x01bc:
            r5.setEnabled(r9)
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mFontDownButton
            r9 = 6
            int r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r11 <= 0) goto L_0x01c9
            r7 = 1
            goto L_0x01ca
        L_0x01c9:
            r7 = 0
        L_0x01ca:
            r5.setEnabled(r7)
            goto L_0x01d8
        L_0x01ce:
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mFontUpButton
            r5.setEnabled(r2)
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mFontDownButton
            r5.setEnabled(r2)
        L_0x01d8:
            com.artifex.sonui.editor.SODocSession r5 = r12.getSession()
            com.artifex.solib.ArDkDoc r5 = r5.getDoc()
            java.lang.String r5 = com.artifex.sonui.editor.Utilities.getSelectionFontName(r5)
            com.artifex.sonui.editor.SOTextView r7 = r12.mFontNameText
            r7.setText((java.lang.CharSequence) r5)
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mFontColorButton
            r5.setEnabled(r4)
            com.artifex.sonui.editor.ToolbarButton r5 = r12.mFontBackgroundButton
            r5.setEnabled(r4)
            com.artifex.sonui.editor.ToolbarButton r4 = r12.mCutButton
            if (r3 == 0) goto L_0x0207
            com.artifex.sonui.editor.SODocSession r5 = r12.getSession()
            com.artifex.solib.ArDkDoc r5 = r5.getDoc()
            boolean r5 = r5.getSelectionCanBeDeleted()
            if (r5 == 0) goto L_0x0207
            r5 = 1
            goto L_0x0208
        L_0x0207:
            r5 = 0
        L_0x0208:
            r4.setEnabled(r5)
            com.artifex.sonui.editor.ToolbarButton r4 = r12.mCopyButton
            if (r3 == 0) goto L_0x0221
            com.artifex.sonui.editor.SODocSession r5 = r12.getSession()
            com.artifex.solib.ArDkDoc r5 = r5.getDoc()
            com.artifex.solib.SODoc r5 = (com.artifex.solib.SODoc) r5
            boolean r5 = r5.getSelectionCanBeCopied()
            if (r5 == 0) goto L_0x0221
            r5 = 1
            goto L_0x0222
        L_0x0221:
            r5 = 0
        L_0x0222:
            r4.setEnabled(r5)
            com.artifex.sonui.editor.ToolbarButton r4 = r12.mPasteButton
            if (r6 != 0) goto L_0x0245
            if (r0 != 0) goto L_0x022d
            if (r3 == 0) goto L_0x0245
        L_0x022d:
            com.artifex.sonui.editor.SODocSession r0 = r12.getSession()
            com.artifex.solib.ArDkDoc r0 = r0.getDoc()
            com.artifex.solib.SODoc r0 = (com.artifex.solib.SODoc) r0
            com.artifex.solib.ConfigOptions r5 = r12.mDocCfgOptions
            boolean r5 = r5.isExtClipboardInEnabled()
            boolean r0 = r0.androidClipboardHasData(r5)
            if (r0 == 0) goto L_0x0245
            r0 = 1
            goto L_0x0246
        L_0x0245:
            r0 = 0
        L_0x0246:
            r4.setEnabled(r0)
            com.artifex.sonui.editor.ToolbarButton r0 = r12.mSelDeleteButton
            if (r3 == 0) goto L_0x025c
            com.artifex.sonui.editor.SODocSession r3 = r12.getSession()
            com.artifex.solib.ArDkDoc r3 = r3.getDoc()
            boolean r3 = r3.getSelectionCanBeDeleted()
            if (r3 == 0) goto L_0x025c
            goto L_0x025d
        L_0x025c:
            r1 = 0
        L_0x025d:
            r0.setEnabled(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.artifex.sonui.editor.NUIEditToolbar.updateEditUIAppearance():void");
    }

    public void updateEditUIAppearancePpt() {
    }

    public void updateEditUIAppearanceXls() {
        boolean z;
        boolean z2;
        ArDkSelectionLimits selectionLimits = getDocView().getSelectionLimits();
        boolean z3 = true;
        if (selectionLimits != null) {
            z = selectionLimits.getIsActive();
            z2 = z && !selectionLimits.getIsCaret();
        } else {
            z2 = false;
            z = false;
        }
        SODoc sODoc = (SODoc) getSession().getDoc();
        this.mStyleBoldButton.setEnabled(z2);
        this.mStyleBoldButton.setSelected(z2 && sODoc.getSelectionIsBold());
        this.mStyleItalicButton.setEnabled(z2);
        this.mStyleItalicButton.setSelected(z2 && sODoc.getSelectionIsItalic());
        this.mStyleUnderlineButton.setEnabled(z2);
        this.mStyleUnderlineButton.setSelected(z2 && sODoc.getSelectionIsUnderlined());
        this.mStyleLinethroughButton.setEnabled(z2);
        this.mStyleLinethroughButton.setSelected(z2 && sODoc.getSelectionIsLinethrough());
        long round = Math.round(((SODoc) getSession().getDoc()).getSelectionFontSize());
        if (round > 0) {
            this.mFontSizeText.setText((CharSequence) String.format("%d", new Object[]{Integer.valueOf((int) round)}));
            this.mFontUpButton.setEnabled(round < 72);
            this.mFontDownButton.setEnabled(round > 6);
            this.mFontSizeText.setEnabled(true);
        } else {
            this.mFontSizeText.setText((CharSequence) "");
            this.mFontUpButton.setEnabled(false);
            this.mFontDownButton.setEnabled(false);
            this.mFontSizeText.setEnabled(false);
        }
        this.mFontNameText.setText((CharSequence) Utilities.getSelectionFontName(getSession().getDoc()));
        this.mFontNameText.setEnabled(z2);
        this.mFontColorButton.setEnabled(z2);
        this.mFontBackgroundButton.setEnabled(z2);
        this.mCutButton.setEnabled(z && getSession().getDoc().getSelectionCanBeDeleted());
        this.mCopyButton.setEnabled(z && ((SODoc) getSession().getDoc()).getSelectionCanBeCopied());
        this.mPasteButton.setEnabled(z && ((SODoc) getSession().getDoc()).androidClipboardHasData(this.mDocCfgOptions.isExtClipboardInEnabled()));
        ToolbarButton toolbarButton = this.mSelDeleteButton;
        if (!z || !getSession().getDoc().getSelectionCanBeDeleted()) {
            z3 = false;
        }
        toolbarButton.setEnabled(z3);
    }
}
