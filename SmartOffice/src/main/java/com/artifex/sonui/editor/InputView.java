package com.artifex.sonui.editor;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;
import com.artifex.mupdf.fitz.PDFWidget;
import com.artifex.solib.ArDkDoc;
import com.artifex.solib.SODoc;

public class InputView extends View {
    public BIC bic;
    public ArDkDoc mDoc;
    public NUIDocView nuiDocView;

    public class BIC extends BaseInputConnection {
        public int composingRegionEnd;
        public int composingRegionStart;
        public int editableLen;
        public InputMethodManager imm;
        public SpannableStringBuilder myEditable;
        public int soSelectionEnd;
        public int soSelectionStart;
        public View view;

        public BIC(View view2, boolean z, InputMethodManager inputMethodManager) {
            super(view2, z);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            this.myEditable = spannableStringBuilder;
            spannableStringBuilder.clear();
            this.myEditable.clearSpans();
            Selection.setSelection(this.myEditable, 0);
            this.imm = inputMethodManager;
            this.view = view2;
        }

        public void closeConnection() {
            super.closeConnection();
        }

        public boolean commitCompletion(CompletionInfo completionInfo) {
            return super.commitCompletion(completionInfo);
        }

        public boolean commitContent(InputContentInfo inputContentInfo, int i, Bundle bundle) {
            return super.commitContent(inputContentInfo, i, bundle);
        }

        public boolean commitCorrection(CorrectionInfo correctionInfo) {
            return super.commitCorrection(correctionInfo);
        }

        public boolean commitText(CharSequence charSequence, int i) {
            int i2;
            int i3;
            int i4;
            DocView docView = InputView.this.nuiDocView.getDocView();
            if (docView != null) {
                docView.setUpdatesPaused(true);
            }
            getComposingRegion();
            InputView.this.nuiDocView.onTyping();
            String charSequence2 = charSequence.toString();
            int i5 = this.composingRegionStart;
            if (i5 == -1 && this.composingRegionEnd == -1) {
                int selectionStart = Selection.getSelectionStart(this.myEditable);
                int selectionEnd = Selection.getSelectionEnd(this.myEditable);
                int max = Math.max(0, Math.min(selectionStart, selectionEnd));
                i4 = Math.min(this.myEditable.length(), Math.max(selectionStart, selectionEnd));
                i2 = max - this.soSelectionStart;
                i3 = this.soSelectionEnd;
            } else {
                i2 = i5 - this.soSelectionStart;
                i4 = this.composingRegionEnd;
                i3 = this.soSelectionEnd;
            }
            ((SODoc) InputView.this.mDoc).adjustSelection(i2, i4 - i3, 0);
            ((SODoc) InputView.this.mDoc).setSelectionText(charSequence2);
            int length = charSequence.length() + this.soSelectionStart + i2;
            this.soSelectionStart = length;
            this.soSelectionEnd = length;
            boolean commitText = super.commitText(charSequence, i);
            if (this.editableLen <= this.myEditable.length() || this.soSelectionStart != 0) {
                if (docView != null) {
                    docView.setUpdatesPaused(false);
                }
                this.editableLen = this.myEditable.length();
                return commitText;
            }
            updateEditable(true);
            if (docView != null) {
                docView.setUpdatesPaused(false);
            }
            return true;
        }

        public boolean deleteSurroundingText(int i, int i2) {
            getComposingRegion();
            InputView.this.nuiDocView.onTyping();
            int selectionStart = Selection.getSelectionStart(this.myEditable);
            int selectionEnd = Selection.getSelectionEnd(this.myEditable);
            int max = Math.max(0, Math.min(selectionStart, selectionEnd));
            int min = Math.min(this.myEditable.length(), Math.max(selectionStart, selectionEnd));
            if (max - i < 0) {
                i = max;
            }
            if (min + i2 > this.myEditable.length()) {
                i2 = this.myEditable.length() - min;
            }
            DocView docView = InputView.this.nuiDocView.getDocView();
            if (docView != null) {
                docView.setUpdatesPaused(true);
            }
            int i3 = (min + i2) - this.soSelectionEnd;
            ((SODoc) InputView.this.mDoc).adjustSelection(min - this.soSelectionStart, i3, 0);
            ((SODoc) InputView.this.mDoc).setSelectionText("");
            int i4 = max - min;
            int i5 = i4 - i;
            ((SODoc) InputView.this.mDoc).adjustSelection(i5, i4, 0);
            ((SODoc) InputView.this.mDoc).setSelectionText("");
            int i6 = this.soSelectionStart + i5;
            this.soSelectionStart = i6;
            this.soSelectionEnd = i6;
            if (docView != null) {
                docView.setUpdatesPaused(false);
            }
            boolean deleteSurroundingText = super.deleteSurroundingText(i, i2);
            if (this.soSelectionStart == 0) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        BIC bic = BIC.this;
                        if (bic.soSelectionStart == 0) {
                            bic.updateEditable(true);
                        }
                    }
                });
                return true;
            }
            this.editableLen = this.myEditable.length();
            return deleteSurroundingText;
        }

        public boolean finishComposingText() {
            getComposingRegion();
            return super.finishComposingText();
        }

        public final void getComposingRegion() {
            SpannableStringBuilder spannableStringBuilder = this.myEditable;
            Object[] spans = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), Object.class);
            this.composingRegionStart = -1;
            this.composingRegionEnd = -1;
            if (spans != null) {
                for (int length = spans.length - 1; length >= 0; length--) {
                    Object obj = spans[length];
                    if ((this.myEditable.getSpanFlags(obj) & 256) != 0) {
                        int spanStart = this.myEditable.getSpanStart(obj);
                        int spanEnd = this.myEditable.getSpanEnd(obj);
                        this.composingRegionStart = Math.max(0, Math.min(spanStart, spanEnd));
                        this.composingRegionEnd = Math.min(this.myEditable.length(), Math.max(spanStart, spanEnd));
                        return;
                    }
                }
            }
        }

        public Editable getEditable() {
            return this.myEditable;
        }

        public ExtractedText getExtractedText(ExtractedTextRequest extractedTextRequest, int i) {
            ExtractedText extractedText = new ExtractedText();
            SpannableStringBuilder spannableStringBuilder = this.myEditable;
            if (spannableStringBuilder == null) {
                return null;
            }
            int length = spannableStringBuilder.length();
            extractedText.partialEndOffset = -1;
            extractedText.partialStartOffset = -1;
            if ((extractedTextRequest.flags & 1) != 0) {
                extractedText.text = this.myEditable.subSequence(0, length);
            } else {
                extractedText.text = TextUtils.substring(this.myEditable, 0, length);
            }
            extractedText.flags = 0;
            extractedText.startOffset = 0;
            extractedText.selectionStart = Selection.getSelectionStart(this.myEditable);
            extractedText.selectionEnd = Selection.getSelectionEnd(this.myEditable);
            return extractedText;
        }

        public CharSequence getTextAfterCursor(int i, int i2) {
            return super.getTextAfterCursor(i, i2);
        }

        public CharSequence getTextBeforeCursor(int i, int i2) {
            return super.getTextBeforeCursor(i, i2);
        }

        public boolean sendKeyEvent(KeyEvent keyEvent) {
            int keyCode = keyEvent.getKeyCode();
            if (keyEvent.getAction() == 1) {
                if (keyCode == 21) {
                    InputView.this.nuiDocView.onTyping();
                    if (this.soSelectionStart == 0) {
                        updateEditable(false);
                    } else {
                        ((SODoc) InputView.this.mDoc).adjustSelection(-1, -1, 1);
                        int i = this.soSelectionStart - 1;
                        this.soSelectionStart = i;
                        this.soSelectionEnd--;
                        Selection.setSelection(this.myEditable, i);
                        this.imm.updateSelection(this.view, this.soSelectionStart, this.soSelectionEnd, 0, 0);
                    }
                } else if (keyCode == 22) {
                    InputView.this.nuiDocView.onTyping();
                    if (this.soSelectionStart == this.myEditable.length()) {
                        updateEditable(false);
                    } else {
                        ((SODoc) InputView.this.mDoc).adjustSelection(1, 1, 1);
                        int i2 = this.soSelectionStart + 1;
                        this.soSelectionStart = i2;
                        this.soSelectionEnd++;
                        Selection.setSelection(this.myEditable, i2);
                        this.imm.updateSelection(this.view, this.soSelectionStart, this.soSelectionEnd, 0, 0);
                    }
                } else if (keyCode == 66) {
                    InputView.this.nuiDocView.onTyping();
                    finishComposingText();
                    commitText("\n", 1);
                    this.imm.restartInput(this.view);
                } else if (keyCode == 67) {
                    InputView.this.nuiDocView.onTyping();
                    if (Selection.getSelectionStart(this.myEditable) == Selection.getSelectionEnd(this.myEditable)) {
                        deleteSurroundingText(1, 0);
                    } else {
                        setComposingText("", 1);
                    }
                } else if (keyCode != 112) {
                    NUIDocView nUIDocView = InputView.this.nuiDocView;
                    if (nUIDocView != null) {
                        nUIDocView.doKeyDown(keyCode, keyEvent);
                    }
                } else {
                    InputView.this.nuiDocView.onTyping();
                    if (Selection.getSelectionStart(this.myEditable) == Selection.getSelectionEnd(this.myEditable)) {
                        deleteSurroundingText(0, 1);
                    } else {
                        setComposingText("", 1);
                    }
                }
            }
            return true;
        }

        public boolean setComposingRegion(int i, int i2) {
            getComposingRegion();
            return super.setComposingRegion(i, i2);
        }

        public boolean setComposingText(CharSequence charSequence, int i) {
            int i2;
            int i3;
            int i4;
            DocView docView = InputView.this.nuiDocView.getDocView();
            if (docView != null) {
                docView.setUpdatesPaused(true);
            }
            getComposingRegion();
            InputView.this.nuiDocView.onTyping();
            String charSequence2 = charSequence.toString();
            int i5 = this.composingRegionStart;
            if (i5 == -1 && this.composingRegionEnd == -1) {
                int selectionStart = Selection.getSelectionStart(this.myEditable);
                int selectionEnd = Selection.getSelectionEnd(this.myEditable);
                int max = Math.max(0, Math.min(selectionStart, selectionEnd));
                i4 = Math.min(this.myEditable.length(), Math.max(selectionStart, selectionEnd));
                i2 = max - this.soSelectionStart;
                i3 = this.soSelectionEnd;
            } else {
                i2 = i5 - this.soSelectionStart;
                i4 = this.composingRegionEnd;
                i3 = this.soSelectionEnd;
            }
            ((SODoc) InputView.this.mDoc).adjustSelection(i2, i4 - i3, 0);
            ((SODoc) InputView.this.mDoc).setSelectionText(charSequence2);
            int length = this.soSelectionStart + i2 + charSequence.length();
            this.soSelectionStart = length;
            this.soSelectionEnd = length;
            boolean composingText = super.setComposingText(charSequence, i);
            if (this.editableLen <= this.myEditable.length() || this.soSelectionStart != 0) {
                if (docView != null) {
                    docView.setUpdatesPaused(false);
                }
                this.editableLen = this.myEditable.length();
                return composingText;
            }
            if (docView != null) {
                docView.setUpdatesPaused(false);
            }
            updateEditable(true);
            return true;
        }

        public boolean setSelection(int i, int i2) {
            int min = Math.min(i, i2);
            int max = Math.max(i, i2);
            int i3 = min - this.soSelectionStart;
            int i4 = max - this.soSelectionEnd;
            ((SODoc) InputView.this.mDoc).adjustSelection(i3, i4, 1);
            this.soSelectionStart += i3;
            this.soSelectionEnd += i4;
            return super.setSelection(i, i2);
        }

        public void updateEditable(boolean z) {
            ArDkDoc arDkDoc = InputView.this.mDoc;
            if (arDkDoc != null) {
                SODoc.SOSelectionContext selectionContext = ((SODoc) arDkDoc).getSelectionContext();
                if (selectionContext != null) {
                    this.composingRegionStart = 0;
                    this.composingRegionEnd = 0;
                    this.soSelectionStart = 0;
                    this.soSelectionEnd = 0;
                    this.editableLen = 0;
                    this.myEditable.clearSpans();
                    if (selectionContext.text == null) {
                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                        this.myEditable = spannableStringBuilder;
                        Selection.setSelection(spannableStringBuilder, selectionContext.start);
                    } else {
                        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder(selectionContext.text);
                        this.myEditable = spannableStringBuilder2;
                        int i = selectionContext.length;
                        if (i == 0) {
                            Selection.setSelection(spannableStringBuilder2, selectionContext.start);
                        } else {
                            int i2 = selectionContext.start;
                            Selection.setSelection(spannableStringBuilder2, i2, i + i2);
                        }
                    }
                    int i3 = selectionContext.start;
                    this.soSelectionStart = i3;
                    int i4 = i3 + selectionContext.length;
                    this.soSelectionEnd = i4;
                    this.imm.updateSelection(this.view, i3, i4, 0, 0);
                }
                this.editableLen = this.myEditable.length();
                if (z) {
                    this.imm.restartInput(this.view);
                }
            }
        }
    }

    public InputView(Context context, ArDkDoc arDkDoc, NUIDocView nUIDocView) {
        super(context);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.mDoc = arDkDoc;
        this.nuiDocView = nUIDocView;
        this.bic = new BIC(this, true, (InputMethodManager) context.getSystemService("input_method"));
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        Configuration configuration = getResources().getConfiguration();
        editorInfo.imeOptions = 0;
        if (configuration.keyboard != 3) {
            editorInfo.imeOptions = 268435456 | 0;
        }
        editorInfo.inputType = 1;
        int i = 1 | PDFWidget.PDF_BTN_FIELD_IS_NO_TOGGLE_TO_OFF;
        editorInfo.inputType = i;
        editorInfo.initialCapsMode = this.bic.getCursorCapsMode(i);
        editorInfo.privateImeOptions = null;
        editorInfo.initialSelStart = Selection.getSelectionStart(this.bic.myEditable);
        editorInfo.initialSelEnd = Selection.getSelectionEnd(this.bic.myEditable);
        editorInfo.actionLabel = null;
        editorInfo.actionId = 0;
        editorInfo.extras = null;
        editorInfo.hintText = null;
        return this.bic;
    }

    public void resetEditable() {
        BIC bic2 = this.bic;
        bic2.myEditable.clear();
        bic2.myEditable.clearSpans();
        bic2.composingRegionStart = 0;
        bic2.composingRegionEnd = 0;
        bic2.soSelectionStart = 0;
        bic2.soSelectionEnd = 0;
        bic2.editableLen = 0;
        bic2.imm.restartInput(bic2.view);
    }

    public void setFocus() {
        requestFocus();
    }

    public void updateEditable() {
        this.bic.updateEditable(true);
    }
}
